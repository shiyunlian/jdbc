package com.shiyun.dao.improvement;

import java.sql.Connection;
import java.util.List;

import com.shiyun.bean.Customer;

public interface CustomerDAO {
	
	//insert customer into customers table
	void insert(Connection conn, Customer customer);
	
	//delete customer based on id
	void deleteById(Connection conn, int id);
	
	//update customer based on id
	void updateById(Connection conn, int id, Customer customer);
	
	//get customer based on id
	Customer getCustomerById(Connection conn, int id);
	
	//get all customers
	List<Customer> getAll(Connection conn);
	
	//get number of customers
	int getCount(Connection conn);
}
