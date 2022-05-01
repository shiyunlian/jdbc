package com.shiyun.connection;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import com.shiyun.util.JDBCUtils;

public class PreparedStatementUpdateTest {
	
	@Test
	public void testCommonUpdate() {
//		String sql = "delete from customers where id = ?";
//		update(sql, 3);
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter name:");
		String name = scanner.next();
		System.out.println("Enter id:");
		int id = scanner.nextInt();
		
		String sql = "update customers set name = ? where id = ?";
		int updateCount = update(sql, name, id);
		if (updateCount>0) {
			System.out.println("Update success!");
		}else {
			System.out.println("Update failure!");
		}
		
	}
	
	
	//generic way to return the number of create/update/delete a record
	public int update(String sql, Object ...args){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JDBCUtils.getConnection();
			ps = conn.prepareStatement(sql);
			
			for(int i = 0; i<args.length; i++) {
				ps.setObject(i+1, args[i]);
			}
			
			//ps.execute();
			
			/*
			 * ps.execute(): 
			 * if it is search operation, ps.execute() has result and return True
			 * if it is add/update/delete operation, ps.execute() has no result and return False
			 */
			// return the number of updated record, should return 1 record
			return ps.executeUpdate(); 
		}  catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.closeResource(conn, ps);
		}
		//return 0 if no updated record 
		return 0;
	}
	

	//update a record
	@Test
	public void testUpdate(){

		Connection conn = null;
		PreparedStatement ps = null;
		try {
			//step1: get connection
			conn = JDBCUtils.getConnection();
			
			//step2:
			String sql = "update customers set name = ? where id = ?";
			ps = conn.prepareStatement(sql);
			
			ps.setObject(1, "Ja");
			ps.setObject(2, 2);
			
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtils.closeResource(conn, ps);
		}
	}
	
	//insert a record
	@Test
	public void testInsert() {

		Connection conn = null;
		PreparedStatement ps = null;
		try {
			//InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
			InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
			Properties pros = new Properties();
			pros.load(is);
			
			String user = pros.getProperty("user");
			String password = pros.getProperty("password");
			String url = pros.getProperty("url");
			String driverClass = pros.getProperty("driverClass");
			
			Class.forName(driverClass);
			
			conn = DriverManager.getConnection(url, user, password);
			
			System.out.println(conn);
			
			String sql = "insert into customers(id, name,email) values(?,?,?)";
			ps = conn.prepareStatement(sql);
			
			ps.setObject(1, 10);
			ps.setString(2, "Mike");
			ps.setString(3, "mike@sjsu.edu");
			ps.execute();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
	}
}
