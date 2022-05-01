package com.shiyun.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.junit.jupiter.api.Test;

public class ConnectionTest {
/*
	//method 1
	@Test
	public void testConnection1() throws SQLException {
		//step1: get Driver object
		Driver driver = new com.mysql.cj.jdbc.Driver();
		
		//step2: provide database url
		//jdbc:mysql: protocol
		//localhost: ip address
		//3306: default port
		//test: test schema
		String url = "jdbc:mysql://localhost:3306/test";
		
		//step3: provide username and password to connect database
		//set username and password in properties
		Properties info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "seven");
		
		//step4: get connection
		Connection conn = driver.connect(url, info);

		System.out.println(conn);
	}
	
	//method 2
	@Test
	public void testConnection2() throws Exception {
		//step1: get Driver object by means of relection
		Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
		Driver driver = (Driver) clazz.newInstance();
		
		//step2: provide database url
		String url = "jdbc:mysql://localhost:3306/test";
		
		//step3: provide username and password to connect database
		//set username and password in properties
		Properties info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "seven");
		
		//step4: get connection
		Connection conn = driver.connect(url, info);

		System.out.println(conn);
	}
	
	//method 3: replace Driver with DriverManager
	@Test
	public void testConnection3() throws Exception {
		//get Driver object
		Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
		Driver driver = (Driver) clazz.newInstance();
		
		//get connection info
		String url = "jdbc:mysql://localhost:3306/test";
		String user = "root";
		String password = "seven";
		
		//register driver
		DriverManager.registerDriver(driver);
		
		//get connection
		Connection conn = DriverManager.getConnection(url, user, password);
		
		System.out.println(conn);
	}
	
	//method 4
	@Test
	public void testConnection4() throws Exception {
		
		String url = "jdbc:mysql://localhost:3306/test";
		String user = "root";
		String password = "seven";
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		//get connection
		Connection conn = DriverManager.getConnection(url, user, password);
		
		System.out.println(conn);
	}	
*/	
	//method 5
	@Test
	public void testConnection5(){
		
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
			
			String sql = "insert into customers(name,email) values(?,?)";
			ps = conn.prepareStatement(sql);
			
			ps.setString(1, "Elsa");
			ps.setString(2, "elsa@sjsu.edu");
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
