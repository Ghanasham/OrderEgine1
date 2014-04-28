package com.pip.orders.engine.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import com.google.gson.JsonSyntaxException;
import com.pip.orders.engine.core.OrderEngine;

/**
 * This class starts {@code OrderEngine}, submit Orders and stops it after receiving EOF on console
 * @author lavand
 *
 */
public class RunOrderEngine {
	
	/** 
	 * Listens to console to receive orders and updates and submit them to {@code OrderEngine}. 
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("Welcome to Pete’s Interplanetary Pizza Order Engine! Please enter your order in JSON format");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		OrderEngine orderEngine = OrderEngine.getInstance();
		
		String jsonStringOrder;
		try {
			//Accept orders till EOF is received
			while(!(jsonStringOrder = br.readLine()).equals("EOF")){
			
				try{
					//Submit order to engine
					orderEngine.submitOrder(jsonStringOrder);
				}catch(InterruptedException e){
					System.err.println("Your Order cannot be Submitted at this time. Please try again later");
				}catch(JsonSyntaxException e){
					System.err.println("Invalid Order format. " + e.getMessage());
				}catch(Exception e){
					System.err.println(e.getMessage());
				}
			}
			
			//Print order report
			orderEngine.printReport();
			
			//Stop engine and exit
			orderEngine.stopEngine();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
