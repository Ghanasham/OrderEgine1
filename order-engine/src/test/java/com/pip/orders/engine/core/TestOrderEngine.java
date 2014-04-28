package com.pip.orders.engine.core;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;

import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;
import com.pip.orders.engine.domain.Order;

public class TestOrderEngine {
	
	@Test
	public void testSingletonInstance(){
		OrderEngine e1 = OrderEngine.getInstance();
		OrderEngine e2 = OrderEngine.getInstance();
		assertTrue(e1 == e2);
	}

	@Test
	public void testIsValidOrder(){
		OrderEngine engine = OrderEngine.getInstance();
		Gson gson = new Gson();
		Order order;
		
		order = gson.fromJson("{\"updateId\":1,\"status\":\"NEW\"}", Order.class);
		assertFalse(engine.isValidOrder(order));
		
		order = gson.fromJson("{\"orderId\":1,\"status\":\"COOKING\"}", Order.class);
		assertFalse(engine.isValidOrder(order));
		
		order = gson.fromJson("{\"orderId\":1,\"updateId\":1}", Order.class);
		assertFalse(engine.isValidOrder(order));
		
		order = gson.fromJson("{\"orderId\":1,\"updateId\":1,\"status\":\"DELIVERED\",\"amount\":-15}", Order.class);
		assertTrue(engine.isValidOrder(order));
		
		order = gson.fromJson("{\"orderId\":1,\"updateId\":1,\"status\":\"DELIVERED\"}", Order.class);
		assertTrue(engine.isValidOrder(order));
		
		order = gson.fromJson("{\"orderId\":1,\"updateId\":1,\"status\":\"DELIVERED\",\"amount\":15}", Order.class);
		assertTrue(engine.isValidOrder(order));
		
		order = gson.fromJson("{\"orderId\":1,\"updateId\":1,\"status\":\"DELIVERED\",\"time\":15}", Order.class);
		assertTrue(engine.isValidOrder(order));
	}
	
	/**Submits 999968 orders which starts in NEW state and end in REFUNDED state.
	 * @throws Exception
	 */
	//@Ignore
	@Test
	public void testSubmitOrder() throws Exception{
		BufferedReader reader = new BufferedReader(new FileReader("src\\test\\java\\com\\pip\\orders\\engine\\core\\TestFile1.txt"));
		OrderEngine engine = OrderEngine.getInstance();
		String order;
		while((order = reader.readLine())!=null){
			engine.submitOrder(order);
		}
		Thread.sleep(30000);
		for(int i=0;i<999967;i++)
			assertEquals(OrderStatus.REFUNDED, engine.getOrderStore().getOrder(i).getStatus());
		
		engine.printReport();
	}
}
