package com.pip.orders.engine.domain;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

import com.pip.orders.engine.core.OrderStatus;

public class TestOrder {
	
	Order first = new Order(1,1,OrderStatus.NEW);
	Order second = new Order(1,1,OrderStatus.NEW);
	Order third = new Order(1,2,OrderStatus.NEW);
	Order fourth = new Order(2,1,OrderStatus.REFUNDED);
	Order fifth = new Order(1,2,OrderStatus.COOKING);
	Order sixth = new Order(1,3,OrderStatus.NEW);
	Order seventh = new Order(1,2,OrderStatus.DELIVERED);
	
	@Test
	public void testOrderUpdateWithEqualOrders(){
		
		try{
			first.update(second);
			fail("Expected exception not thrown");
			
		}catch(Exception e){
			assertThat(e.getMessage(), containsString("Both Orders are equal"));
		}
	}

	@Test
	public void testOrderUpdateWithDifferentOrderId(){
		
		try{
			first.update(fourth);
			fail("Expected exception not thrown");
			
		}catch(Exception e){
			assertThat(e.getMessage(), containsString("orderId must match"));
		}
	}
	
	@Test
	public void testOrderUpdateWithSmallerUpdateId(){
		
		try{
			third.update(first);
			fail("Expected exception not thrown");
			
		}catch(Exception e){
			assertThat(e.getMessage(), containsString("please provide greater updateId"));
		}
	}
	
	@Test
	public void testOrderUpdateWithSmallerStatus(){
		
		try{
			fifth.update(sixth);
			fail("Expected exception not thrown");
			
		}catch(Exception e){
			assertThat(e.getMessage(), containsString("Can't change Order status"));
		}
	}
	
	@Test
	public void testOrderUpdateWithInvalidStatus(){
		
		try{
			first.update(seventh);
			fail("Expected exception not thrown");
			
		}catch(Exception e){
			assertThat(e.getMessage(), containsString("Can't change Order status"));
		}
	}
	
	@Test
	public void testOrderUpdateWithValidOrder(){
		
		try{
			first.update(fifth);
		}catch(Exception e){
			fail("Unexpected exception thrown");
		}
	}


	
}
