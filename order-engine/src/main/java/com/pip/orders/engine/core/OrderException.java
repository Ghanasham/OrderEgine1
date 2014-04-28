package com.pip.orders.engine.core;

/** Specialized exception class for {@code Order} module
 * @author lavand
 *
 */
public class OrderException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public OrderException(){
		super();
	}
	public OrderException(String msg){
		super(msg);
	}
	public OrderException(Throwable cause){
		super(cause);
	}
	public OrderException(String message, Throwable cause){
		super(message, cause);
	}
}
