package com.shiyun.transaction;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.Test;

import com.shiyun.bean.User;
import com.shiyun.util.JDBCUtils;

public class TransactionTest {
	
//	@Test
//	public void testUpdateWithoutTransaction() {
//		String sql1 = "update user_table set balance = balance - 100 where user = ?";
//		update(sql1, "A");
//		
//		System.out.println(10/0);//mimic network issue
//		
//		String sql2 = "update user_table set balance = balance + 100 where user = ?";
//		update(sql2, "B");
//		
//		System.out.println("Money has been transferred");
//	}

	public int update(String sql, Object ...args){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JDBCUtils.getConnection();
			ps = conn.prepareStatement(sql);
			
			for(int i = 0; i<args.length; i++) {
				ps.setObject(i+1, args[i]);
			}
			
	
			return ps.executeUpdate(); 
		}  catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.closeResource(conn, ps);
		}

		return 0;
	}
	
	
	@Test
	public void testUpdateWithTransaction() {
		Connection conn = null;
		
		try {
			conn = JDBCUtils.getConnection();
			
			conn.setAutoCommit(false);
			
			String sql1 = "update user_table set balance = balance - 100 where user = ?";
			update(conn, sql1, "A");
			
			//System.out.println(10/0);//mimic network issue
			
			String sql2 = "update user_table set balance = balance + 100 where user = ?";
			update(conn, sql2, "B");
			
			System.out.println("Money has been transferred");
			conn.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			
			//roll back if there is any exception
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			JDBCUtils.closeResource(conn, null);
		}
	}
	
	@Test
	public void testTransactionSelect(){
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			//get isolation
			//TRANSACTION_REPEATABLE_READ  = 4
			//dirty reads, non-repeatable reads and phantom reads are prevented.
			System.out.println(conn.getTransactionIsolation());
			
			conn.setAutoCommit(false);
			
			String sql = "select * from user_table where user = ?";
			User user = getInstance(conn, User.class, sql, "A");
			System.out.println(user);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.closeResource(conn, null);
		}
	}
	
	//generic way to insert/update/delete data, using connection from outside
	public int update(Connection conn, String sql, Object ...args){
		
		PreparedStatement ps = null;
		try {
			conn = JDBCUtils.getConnection();
			ps = conn.prepareStatement(sql);
			
			for(int i = 0; i<args.length; i++) {
				ps.setObject(i+1, args[i]);
			}
			
			return ps.executeUpdate(); 
		}  catch (Exception e) {
			e.printStackTrace();
		}finally {
			
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			JDBCUtils.closeResource(null, ps);
		}

		return 0;
	}
	
	//generic way to query data, return one record
	public <T> T getInstance(Connection conn, Class<T> clazz, String sql, Object ...args) {
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
			JDBCUtils.closeResource(null, ps, rs);
		}
		return null; //return null if data is not found
	}
	
}
