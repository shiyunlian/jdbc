package com.shiyun.connection;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.shiyun.bean.Customer;
import com.shiyun.util.JDBCUtils;

public class PreparedStatementQueryTest {
	
	@Test
	public void testGetForList() {
		String sql = "select * from customers where id < ?";
		List<Customer> list = getForList(Customer.class, sql, 6);
		list.forEach(System.out::println);
	}
	
	
	//a generic way to get multiple object records
	public <T> List<T> getForList(Class<T> clazz, String sql, Object ...args){
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
			
			//create an array to store data
			ArrayList<T> list = new ArrayList<T>();
			
			//get multiple records from resultSet
			while(rs.next()) {
				
				T t = clazz.newInstance();
				
				//get each column data
				for (int i = 0; i < columnCount; i++) {
					//get column value
					Object columnValue = rs.getObject(i+1);
					
					//get column name
					String columnName = rsmd.getColumnName(i+1);
					
					//set column value to the corresponding t property by means of reflection
					Field field = clazz.getDeclaredField(columnName);
					field.setAccessible(true);
					field.set(t, columnValue);
				}
				list.add(t);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.closeResource(conn, ps, rs);
		}
		return null; //return null if data is not found
	}
	
	
	@Test
	public void testGetInstance() {
		String sql = "select * from customers where id = ?";
		Customer customer = getInstance(Customer.class, sql, 2);
		System.out.println(customer);
	}

	//a generic way to get an instance of the object
	public <T> T getInstance(Class<T> clazz, String sql, Object ...args) {
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
				
				T t = clazz.newInstance();
				
				//get each column data
				for (int i = 0; i < columnCount; i++) {
					//get column value
					Object columnValue = rs.getObject(i+1);
					
					//get column name
					String columnName = rsmd.getColumnLabel(i+1);
					
					//set column value to the corresponding t property by means of reflection
					Field field = clazz.getDeclaredField(columnName);
					field.setAccessible(true);
					field.set(t, columnValue);
				}
				return t;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.closeResource(conn, ps, rs);
		}
		return null; //return null if data is not found
	}
}
