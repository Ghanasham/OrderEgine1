package com.pip.orders.engine.core;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.pip.orders.engine.domain.Order;

public class TestOrderProcessor {

	private static OrderStore store;
	
	@BeforeClass
	public static void setupClass(){
		store = new OrderStore();
	}
	
	@Test
	public void testSingleton(){
		OrderEngine engine1 = OrderEngine.getInstance();
		OrderEngine engine2 = OrderEngine.getInstance();
		
		assertTrue(engine1 == engine2);
	}
	
	@Test
	public void testSubmitOrderNewOrderWithoutAmount(){
		
		Order order = new Order(1,1,OrderStatus.NEW);
		OrderProcessor processor = new OrderProcessor(store, order); 
		Executors.newSingleThreadExecutor().execute(processor);
		assertNull(store.getOrder(order.getOrderId()));
	}
	
	//@Ignore
	@Test
	public void testSubmitOrderNewOrderWithAmount(){
		
		Order order = new Order(1,1,OrderStatus.NEW,10);
		OrderProcessor processor = new OrderProcessor(store, order);
		Executors.newSingleThreadExecutor().execute(processor);
		assertNotNull(store.getOrder(order.getOrderId()));
	}
	
	@Test
	public void testSubmitOrderInvalidUpdateOrder(){
		
		Order order = new Order(2,1,OrderStatus.COOKING,10);
		OrderProcessor processor = new OrderProcessor(store, order);
		Executors.newSingleThreadExecutor().execute(processor);
		assertNull(store.getOrder(order.getOrderId()));
	}
	
	@Test
	public void testSubmitOrderValidUpdateOrder(){
		
		
		ExecutorService exe = Executors.newSingleThreadExecutor();
		
		Order order1 = new Order(3,1,OrderStatus.NEW,10);
		OrderProcessor processor1 = new OrderProcessor(store, order1);
		Order order2 = new Order(3,2,OrderStatus.COOKING);
		OrderProcessor processor2 = new OrderProcessor(store, order2);
		Future<?> f1 = exe.submit(processor1);
		Future<?> f2 = exe.submit(processor2);
		try {
			f1.get();
			f2.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(order2, store.getOrder(3));
	
	}
}
