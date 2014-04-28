package com.pip.orders.engine.core;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pip.orders.engine.domain.Order;

/** This singleton order engine maintains all submitted Orders in valid state. It can print summary of all Orders. 
 * @author lavand
 *
 */
public class OrderEngine {
	
	/**
	 * Fixed size of the order queue
	 */
	private static final int QUEUE_SIZE = 1048576;
	
	/**
	 * Number of executers to process orders
	 */
	private static final int EXECUTORS_SIZE = 32;
	
	
	private ArrayList<ExecutorService> executors = null;
	
	/**
	 * Singleton instance of {@code OrderEngine}
	 */
	private static OrderEngine engine = new OrderEngine();

	/**
	 * Order Queue listener 
	 */
	private OrderQueueListener listener = null;
	
	/**
	 * Queue to hold Orders before processing them
	 */
	private ArrayBlockingQueue<Order> queue = null;
	
	/**
	 * {@code OrderStore} to store orders
	 */
	private OrderStore orderStore = null;
	
	
	/**
	 * gson object to convert json and java objects
	 */
	Gson gson = new Gson();
	
	/**
	 * Private constructor for singleton engine
	 */
	private OrderEngine(){
		
		orderStore = new OrderStore();
		
		//Initialize predefined number of SingleThreadExecutors to process queue Orders
		 
		executors = new ArrayList<ExecutorService>(EXECUTORS_SIZE);
		for(int i=0; i<EXECUTORS_SIZE;i++){
			executors.add(Executors.newSingleThreadExecutor());
		}
		
		/**
		 * Bounded queue with fixed size and fairness policy set to true.
		 * Fairness policy makes sure that waiting {@code Order} producers will get Queue access in FIFO order.
		 * This will eventually guarantee that {@code Order} updates are added to the queue in the same order they are produced.
		 */
		
		queue = new ArrayBlockingQueue<Order>(QUEUE_SIZE, true);
		
		listener = new OrderQueueListener();
		listener.start();
	}
	
	/**Returns singleton instance of {@code OrderEngine}
	 * @return
	 */
	public static OrderEngine getInstance(){
		return engine;
	}
	
	/** 
	 * Submits json format {@code Order} for processing. 
	 * Client programs should call this method to submit NEW {@code Order} or any {@code Order} update.
	 * If the {@code Order} queue of the {@code OrderEngine} is full, this thread will wait till queue becomes available.
	 * @param jsonOrder which can be a NEW {@code Order} or any {@code Order} update
	 * @throws InterruptedException  
	 * 				If Order queue is full, thread will wait till queue becomes available. Thread may be interrupted while waiting
	 */
	public void submitOrder(String jsonOrder) throws InterruptedException{
		try{
			Order order = gson.fromJson(jsonOrder, Order.class);
			submitOrder(order);
		}catch(JsonSyntaxException e){
			System.err.println("Invalid json format");
		}
	}
	
	/** 
	 * Submits java {@code Order} for processing.
	 * @param order which can be a NEW {@code Order} or any {@code Order} update
	 * @throws InterruptedException  
	 * 				if interrupted while waiting
	 */
	public void submitOrder(Order order) throws InterruptedException{
		if(isValidOrder(order))
			queue.put(order);
		else
			System.err.println("Order must contain orderId, updateId and Status");
	}
	
	/** Checks if Order contains required fields like orderId, updateId and Status and if amount is non-zero for NEW orders
	 *  updateId should always be greater than zero. If int variables are missing in json string,
	 *  gson library sets them to zero in deserialization process. So zero is reserved for absent fields.
	 * @param order
	 * @return
	 */
	public boolean isValidOrder(Order order){
		return !(order.getOrderKey() == null || order.getUpdateId() == 0 || 
				order.getStatus() == null || (order.getStatus() == OrderStatus.NEW && order.getAmount() <= 0));
	}
	
	/**
	 *  Prints report of all Orders as in two columns. First column is Order Status and second is number of Orders.
	 *  It prints total amount charged in USD at the end of the report.
	 */
	public void printReport(){
		orderStore.printReport();
	}
	
	/**
	 * Stops the engine
	 */
	public void stopEngine(){
		listener.stop();
	}
	
	public OrderStore getOrderStore() {
		return orderStore;
	}
	
	private class OrderQueueListener implements Runnable {
		 
		private boolean stayOn = true;
		private Thread listenerThread = null;
		
		/**
		 * Keeps on listening to ArrayBlockingQueue and retrieves elements from it as soon as they are available.
		 */
		public void run(){
			
			while(stayOn){
				try{
					//take one order from queue
					Order order = queue.take();
					
					/**
					 * Find the right executor for this order. orderId % EXECUTORS_SIZE always finds the same executor for the given orderId.
					 * Thus same executor processes all the updates for a given order. 
					 * Since executors are SingleThreadExecutors, all updates for a given order are processed sequentially and in the same order in which they are received.
					 * This establishes "happens before" relationship between updates for the same order.
					 * We still achieve concurrency by using multiple executers who process orders with different orderId "concurrently"
					 */
					ExecutorService executor = executors.get(order.getOrderId() % EXECUTORS_SIZE);
					
					//execute new order processing task
					executor.execute(new OrderProcessor(orderStore, order));
					
				}catch(InterruptedException e){
					//Don't worry about other interrupts
				}
			}
		}
		
		/**
		 * Starts listener only if it is not already running  
		 */
		private void start(){
			
			if(listenerThread == null){
				listenerThread = new Thread(this);
				listenerThread.start();
			}
		}
		
		/**
		 * Stops the listener even if queue contains elements to be processed
		 */
		private void stop(){
			stayOn = false;
			listenerThread.interrupt();
		}
	}
	
}
