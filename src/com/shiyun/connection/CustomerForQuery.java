package com.shiyun.connection;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.junit.Test;

import com.shiyun.bean.Customer;
import com.shiyun.util.JDBCUtils;

public class CustomerForQuery {
	
	@Test
	public void testQueryForCustomers() {
		String sql = "select * from customers where id = ?";
		Customer customer = queryForCustomers(sql, 2);
		System.out.println(customer);
	}
	
	public Customer queryForCustomers(String sql, Object ...args){
		Connection conn = null;
		PreparedStatement ps = null;
		//get resultSet
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConnection();
			
			ps = conn.prepareStatement(sql);
			
			for(int i = 0; i < args.length; i ++) {
				ps.setObject(i+1, args[i]);
			}
			
			rs = ps.executeQuery();
			
			//get data from resultSet
			ResultSetMetaData rsmd = rs.getMetaData();
			
			//get columns from ResultSetMetaData
			int columnCount = rsmd.getColumnCount();
			
			//get one record from resultSet
			if(rs.next()) {
				Customer customer = new Customer();
				
				//get each column data
				for (int i = 0; i < columnCount; i++) {
					//get column value
					Object columnValue = rs.getObject(i+1);
					
					//get column name
					String columnName = rsmd.getColumnName(i+1);
					
					//set column value to the corresponding customer property by means of reflection
					Field field = Customer.class.getDeclaredField(columnName);
					field.setAccessible(true);
					field.set(customer, columnValue);
				}
				return customer;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.closeResource(conn, ps, rs);
		}
		return null; //return null if data is not found
	}
	

	@Test
	public void testQuery1() {
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		//get resultSet
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConnection();
			
			String sql = "select * from customers where id = ?";
			
			ps = conn.prepareStatement(sql);
			ps.setObject(1, 1);
			
			rs = ps.executeQuery();
			
			//handle resultSet
			if(rs.next()) {
				int id = rs.getInt(1);
				String name = rs.getString(2);
				String email = rs.getString(3);
				System.out.println("id: "+id+" ,name: "+name+" ,email: "+email);
				
				Object[] data = new Object[] {id, name, email};
				System.out.println(data);
				
				Customer customer = new Customer(id, name, email);
				System.out.println(customer);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.closeResource(conn, ps, rs);
		}
		
	}
}
