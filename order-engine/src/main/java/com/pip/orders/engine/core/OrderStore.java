package com.pip.orders.engine.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pip.orders.engine.domain.Order;

/** 
 * Stores all Orders and their updates
 * @author lavand
 *
 */
public class OrderStore {

	/**
	 * A Map to store all orders
	 */
	private Map<String,Order> orders = null;
	
	
	OrderStore(){
		//ConcurrentHashMap will handle concurrent requests from multiple Executors
		orders = new ConcurrentHashMap<String,Order>();
	}
	
	/**Returns {@code Order} for given orderId 
	 * @param orderId
	 * @return Order
	 */
	public Order getOrder(int orderId){
		return orders.get(String.valueOf(orderId));
	}
	
	/** Adds given {@code Order} in HashMap
	 * @param order
	 */
	public void putOrderInMap(Order order){
		orders.put(order.getOrderKey(), order);
	}
	
	public boolean containsOrder(int orderId){
		return orders.containsKey(String.valueOf(orderId));
	}
	
	/**
	 *  Prints report of all Orders as in two columns. First column is Order Status and second is number of Orders.
	 *  It prints total amount charged in USD at the end of the report.
	 */
	public void printReport(){
		int newOrders=0, cooking=0, delivering=0, delivered=0, canceled=0, refunded=0, total=0;
		
		for(Order order : orders.values()){
			switch(order.getStatus()){
				case NEW:
					newOrders++;
					break;
				case COOKING:
					cooking++;
					total+=order.getAmount();
					break;
				case DELIVERING:
					delivering++;
					total+=order.getAmount();
					break;
				case DELIVERED:
					delivered++;
					total+=order.getAmount();
					break;
				case CANCELED:
					canceled++;
					break;
				case REFUNDED:
					refunded++;					
			}
		}
		
		System.out.println("New: " + newOrders);
		System.out.println("Cooking: " + cooking);
		System.out.println("Delivering: " + delivering);
		System.out.println("Delivered: " + delivered);
		System.out.println("Canceled: " + canceled);
		System.out.println("Refunded: " + refunded);
		System.out.println("Total amount charged: $" + total);
		
	}
	
	
	
	
	
}
