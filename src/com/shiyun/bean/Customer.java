package com.shiyun.bean;
/*
 * ORM: object relational mapping
 * each table represents a java class
 * each record in the table represents an object of the java class
 * each property in the table represents a property of the java class
 */
public class Customer {
	
	private int id;
	private String name;
	private String email;
	
	public Customer() {
		super();
	}
	public Customer(int id, String name, String email) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "id: "+id+" ,name: "+name+" ,email: "+email;
	}
}
