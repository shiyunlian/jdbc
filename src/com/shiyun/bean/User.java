package com.shiyun.bean;

public class User {
	
	private String user;
	private int balance;
	
	
	public User() {
		super();
	}

	public User(String user, int balance) {
		super();
		this.user = user;
		this.balance = balance;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "User [user=" + user + ", balance=" + balance + "]";
	}
	
	

}
