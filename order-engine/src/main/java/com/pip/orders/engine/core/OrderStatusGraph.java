package com.pip.orders.engine.core;

/** This class maintains a digraph of {@code OrderStatus}.  
 * @author lavand
 *
 */
public class OrderStatusGraph {

	/**
	 * A boolean adjacency matrix for {@code OrderStatus} digraph
	 */
	private static final boolean[][] graph = new boolean[OrderStatus.values().length][OrderStatus.values().length];
	
	/**
	 * This block initializes digraph as per business rules. It's easy to add, modify or delete business rules for {@code OrderStatus} 
	 * e.g. To add a new {@code OrderStatus} in pizza order system, just add a new field in {@code OrderStatus} enum and add digraph edges in this block
	 * 
	 * 					NEW		COOKING		DELIVERING		DELIVERED		REFUNDED		CANCELED					
	 *					(0)		(1)			(2)				(3)				(4)				(5) 					
	 * 	NEW			(0)	false	true		false			false			false			true
	 * 	COOKING		(1)	false	false		true			false			false			true
	 * 	DELIVERING	(2)	false	false		false			true			false			true
	 * 	DELIVERED	(3)	false	false		false			false			true			false
	 *	REFUNDED	(4)	false	false		false			false			false			false
	 *	CANCELED	(5)	false	false		false			false			false			false
	 */
	static{
		graph[OrderStatus.NEW.ordinal()][OrderStatus.COOKING.ordinal()] = true;
		graph[OrderStatus.COOKING.ordinal()][OrderStatus.DELIVERING.ordinal()] = true;
		graph[OrderStatus.DELIVERING.ordinal()][OrderStatus.DELIVERED.ordinal()] = true;
		graph[OrderStatus.DELIVERED.ordinal()][OrderStatus.REFUNDED.ordinal()] = true;
		
		graph[OrderStatus.NEW.ordinal()][OrderStatus.CANCELED.ordinal()] = true;
		graph[OrderStatus.COOKING.ordinal()][OrderStatus.CANCELED.ordinal()] = true;
		graph[OrderStatus.DELIVERING.ordinal()][OrderStatus.CANCELED.ordinal()] = true;
	}
	
	/**
	 * Checks if {@code OrderStatus} can be changed from "from" to "to" {@code OrderStatus}.
	 * Time complexity is constant time O(1).
	 * @param from
	 * @param to
	 * @return true if "from" {@code OrderStatus} can be changed to "to" {@code OrderStatus}
	 */
	public static boolean isValidChange(OrderStatus from, OrderStatus to){
		return graph[from.ordinal()][to.ordinal()];
	}
}
