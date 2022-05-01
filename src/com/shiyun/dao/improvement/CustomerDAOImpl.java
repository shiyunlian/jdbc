package com.shiyun.dao.improvement;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.util.List;

import com.shiyun.bean.Customer;

public class CustomerDAOImpl extends BaseDAO<Customer> implements CustomerDAO{
	


	@Override
	public void insert(Connection conn, Customer customer) {
		String sql = "insert into customers(id, name, email) values(?,?,?)";
		update(conn, sql, customer.getId(), customer.getName(), customer.getEmail());
	}


	@Override
	public void deleteById(Connection conn, int id) {
		String sql = "delete from customers where id = ?";
		update(conn, sql, id);
	}

	@Override
	public void updateById(Connection conn, int id, Customer customer) {
		String sql = "update customers set name = ?, email = ? where id = ?";
		update(conn, sql, customer.getName(), customer.getEmail(), customer.getId());
	}

	@Override
	public Customer getCustomerById(Connection conn, int id) {
		String sql = "select * from customers where id = ?";
		Customer customer = getInstance(conn, sql, id);
		return customer;
	}

	@Override
	public List<Customer> getAll(Connection conn) {
		String sql = "select * from customers";
		List<Customer> list = getForList(conn, sql);
		return list;
	}

	@Override
	public int getCount(Connection conn) {
		String sql = "select count(*) from customers";
		return getValue(conn, sql);
	}

}
