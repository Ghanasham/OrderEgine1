package com.pip.orders.engine.core;

import com.pip.orders.engine.domain.Order;

/**
 * A Runnable class which processes supplied {@code Order} 
 * @author lavand
 *
 */
public class OrderProcessor implements Runnable{

	private OrderStore orderStore;
	private Order order;
	
	public OrderProcessor(OrderStore orderStore, Order order){
		this.orderStore = orderStore; 
		this.order = order;
	}
	
	public void run() {
		
		//If order is NEW, check if it's already present. If present, print error else add it to order store
		if(order.getStatus() == OrderStatus.NEW){
			if(orderStore.containsOrder(order.getOrderId()))
				System.err.println("Order " + order.getOrderId() + " already exists");
			else{
				orderStore.putOrderInMap(order);
				//System.out.println("Order " + order.getOrderId() + " is " + order.getStatus());
			}
		}else{
			//if order is update, get existing order from store and update it if not null
			Order existingOrder = orderStore.getOrder(order.getOrderId());
			if(existingOrder == null){
				System.err.println("Matching order not found for orderId " + order.getOrderId());
			}else{
				try{
					existingOrder.update(order);
				}catch(OrderException e){
					System.err.println(e.getMessage());
				}
			}
		}
		
	}

}
