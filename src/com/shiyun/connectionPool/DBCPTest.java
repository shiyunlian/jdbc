package com.shiyun.connectionPool;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

public class DBCPTest {

//	@Test
//	public void testGetConnection1() throws Exception {
//		//create DBCP connection pool
//		BasicDataSource source = new BasicDataSource();
//		
//		source.setDriverClassName("com.mysql.jdbc.Driver");
//		source.setUrl("jdbc:mysql://localhost:3306/test");
//		source.setUsername("root");
//		source.setPassword("seven");
//		
//		source.setInitialSize(10);
//		source.setMaxActive(10);
//		
//		Connection conn = source.getConnection();
//		System.out.println(conn);
//	}
	
	@Test
	public void testGetConnection2() throws Exception {
		Properties props = new Properties();
		
		//method 1
		InputStream is1 = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
		
		//method 2
		FileInputStream is2 = new FileInputStream(new File("src/dbcp.properties"));
		
		props.load(is1);//same as props.load(is2)
		DataSource source = BasicDataSourceFactory.createDataSource(props);
		Connection conn = source.getConnection();
		System.out.println(conn);
	
	}
}
