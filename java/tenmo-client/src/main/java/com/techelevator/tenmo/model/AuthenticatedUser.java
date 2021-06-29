package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class AuthenticatedUser {
	
	private String token;
	private User user;
	private double balance;
	private double amount;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}


	public double getBalance(double balance) {
		return this.balance = balance;
	}

	public double getTransferAmount(double amount){
		return this.amount = amount;

	}
}
