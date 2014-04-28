package com.pip.orders.engine.core;

/** Possible status that an {@code Order} can have
 * @author lavand
 *
 */
public enum OrderStatus{
	
	NEW, 
	COOKING, 
	DELIVERING, 
	DELIVERED, 
	REFUNDED, 
	CANCELED
	
}
