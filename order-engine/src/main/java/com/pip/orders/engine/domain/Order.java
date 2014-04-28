package com.pip.orders.engine.domain;

import com.pip.orders.engine.core.OrderException;
import com.pip.orders.engine.core.OrderStatus;
import com.pip.orders.engine.core.OrderStatusGraph;

/**The Order class represents pizza order.
 * @author lavand
 *
 */
public class Order{
	
	
	/** The orderId is unique id for the order */
	private String orderId = null;
	
	/** 
	 * Updates are uniquely identified by (orderId, updateId); 
	 * that is, an update should not be applied to an order 
	 * if you already applied an update to that order with the same updateId. 
	 * Valid updateIds (for a given orderId) are provided in an increasing order
	 */
	private int updateId;
	
	/** The status indicates current {@code OrderStatus} of the order */
	private OrderStatus status = null;
	
	/** New orders will have non negative amount*/
	private int amount;
	 

	/**Initializes a newly created {@code Order} object with given parameters
	 * @param orderId
	 * 		  unique id for this order 
	 * 
	 * @param updateId
	 * 		  current update id for this order
	 * 
	 * @param status
	 * 		  current {@code Status} for this order 
	 */
	public Order(int orderId, int updateId, OrderStatus status){
		this.orderId = String.valueOf(orderId);
		this.updateId = updateId;
		this.status = status;
	}
	
	/**Initializes a newly created {@code Order} object with given parameters
	 * @param orderId
	 * 		  unique id for this order 
	 * 
	 * @param updateId
	 * 		  current update id for this order
	 * 
	 * @param status
	 * 		  current {@code Status} for this order
	 * 
	 *  @param amount
	 * 		  amount in USD for this order
	 */
	public Order(int orderId, int updateId, OrderStatus status, int amount){
		this.orderId = String.valueOf(orderId);
		this.updateId = updateId;
		this.status = status;
		this.amount = amount;
	}
	
	
	/** This method updates current {@code Order} with given {@code Order}. Update is done only if it is a valid update.
	 * @param other
	 * @throws OrderException if update is not valid
	 */
	public void update(Order other) throws OrderException{
		if(this.equals(other)) 
			throw new OrderException("Both Orders are equal. Ignoring update call to orderId " + other.getOrderId());
		
		if(!this.orderId.equals(other.orderId))
			throw new OrderException("To update an Order, orderId must match");
		
		if(this.updateId >= other.updateId)
			throw new OrderException("To update an Order, please provide greater updateId for orderId " + other.getOrderId());
		
		if(!OrderStatusGraph.isValidChange(this.status, other.status))
			throw new OrderException("Can't change Order status from " + this.status.toString() + " to " + other.status.toString() + " for orderId " + other.getOrderId());
		
		this.updateId = other.updateId;
		this.status = other.status;
	
		//System.out.println("Order " + other.getOrderId() + " is " + this.status);
	}
	
	
	/** 
	 *	Orders are considered equal if their orderId, updateId and {@code OrderStatus} are equal 
	 */
	@Override
	public boolean equals(Object otherObject){
		if(otherObject == null || !(otherObject instanceof Order))	return false;
		
		if(this == otherObject)	return true;
		
		Order other = (Order)otherObject;
		
		return this.orderId.equals(other.orderId) && this.updateId == other.updateId && this.status.equals(other.status);
			
	}
	
	/** 
	 *	hash code implementation 
	 */
	@Override
	public int hashCode(){
		
		int result = 11;
		result = 31 * result + (orderId!=null ? orderId.hashCode() : 0);
		result = 31 * result + updateId;
		result = 31 * result + (orderId!=null ? orderId.hashCode() : 0);
		return result;
	}
	
	/** 
	 *	toString implementation 
	 */
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder(50);
		sb.append("{orderId:");
		sb.append(orderId);
		sb.append(",updateId:");
		sb.append(updateId);
		sb.append(",status:");
		sb.append(status);
		sb.append("}"); 
		
		return sb.toString();
	}

	/** Returns String orderId which can be used as a key
	 * @return orderId
	 */
	public String getOrderKey(){
		return orderId;
	}
	
	/**Returns int orderId of this {@code Order}
	 * @return orderId
	 */
	public int getOrderId() {
		return Integer.parseInt(orderId);
	}

	/** Returns updateId of this {@code Order}
	 * @return updateId
	 */
	public int getUpdateId() {
		return updateId;
	}

	
	/** Returns {@code OrderStatus} of this {@code Order}
	 * @return
	 */
	public OrderStatus getStatus() {
		return status;
	}

	/** Returns amount of this {@code Order}
	 * @return
	 */
	public int getAmount() {
		return amount;
	}
	
}
