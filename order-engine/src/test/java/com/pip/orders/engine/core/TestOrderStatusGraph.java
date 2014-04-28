package com.pip.orders.engine.core;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TestOrderStatusGraph {

	@Test
	public void testIsValidChangeValidChanges(){
		assertTrue(OrderStatusGraph.isValidChange(OrderStatus.NEW, OrderStatus.COOKING));
		assertTrue(OrderStatusGraph.isValidChange(OrderStatus.COOKING, OrderStatus.DELIVERING));
		assertTrue(OrderStatusGraph.isValidChange(OrderStatus.DELIVERING, OrderStatus.DELIVERED));
		assertTrue(OrderStatusGraph.isValidChange(OrderStatus.DELIVERED, OrderStatus.REFUNDED));
		
		assertTrue(OrderStatusGraph.isValidChange(OrderStatus.NEW, OrderStatus.CANCELED));
		assertTrue(OrderStatusGraph.isValidChange(OrderStatus.COOKING, OrderStatus.CANCELED));
		assertTrue(OrderStatusGraph.isValidChange(OrderStatus.DELIVERING, OrderStatus.CANCELED));
	}
	
	@Test
	public void testIsValidChangeInValidChanges(){
		assertFalse(OrderStatusGraph.isValidChange(OrderStatus.NEW, OrderStatus.NEW));
		assertFalse(OrderStatusGraph.isValidChange(OrderStatus.COOKING, OrderStatus.NEW));
		assertFalse(OrderStatusGraph.isValidChange(OrderStatus.CANCELED, OrderStatus.REFUNDED));
		assertFalse(OrderStatusGraph.isValidChange(OrderStatus.REFUNDED, OrderStatus.CANCELED));
		assertFalse(OrderStatusGraph.isValidChange(OrderStatus.DELIVERED, OrderStatus.DELIVERING));
		
	}
	
}
