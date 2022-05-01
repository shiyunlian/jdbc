package com.shiyun.connection;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.junit.Test;

import com.shiyun.bean.Customer;
import com.shiyun.bean.Order;
import com.shiyun.util.JDBCUtils;

public class OrderForQuery {
	
	public Order queryForOrder(String sql, Object ...args) {
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
			
			//get the number of columns from ResultSetMetaData
			int columnCount = rsmd.getColumnCount();
			
			//get one record from resultSet
			if(rs.next()) {
				Order order = new Order();
				
				//get each column data
				for (int i = 0; i < columnCount; i++) {
					//get column value
					Object columnValue = rs.getObject(i+1);
					
					//get column name
					//String columnName = rsmd.getColumnName(i+1);
					
					//get column alias name
					String columnName = rsmd.getColumnLabel(i+1);
					
					//set column value to the corresponding customer property by means of reflection
					Field field = Customer.class.getDeclaredField(columnName);
					field.setAccessible(true);
					field.set(order, columnValue);
				}
				return order;
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
			
			String sql = "select * from orders where id = ?";
			
			ps = conn.prepareStatement(sql);
			ps.setObject(1, 1);
			
			rs = ps.executeQuery();
			
			//handle resultSet
			if(rs.next()) {
				int id = (int) rs.getObject(1);
				String name = (String) rs.getObject(2);
				Date date = (Date) rs.getObject(3);
				
				Order order = new Order(id, name, date);
				System.out.println(order);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.closeResource(conn, ps, rs);
		}
	}
}
