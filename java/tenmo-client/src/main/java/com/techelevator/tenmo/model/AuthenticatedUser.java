package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class AuthenticatedUser {
	
	private String token;
	private User user;
	private BigDecimal balance;
	private BigDecimal amount;
	
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


	public BigDecimal getBalance(BigDecimal balance) {
		return this.balance = balance;
	}

	public BigDecimal getTransferAmount(BigDecimal amount){
		return this.amount = amount;

	}
}
