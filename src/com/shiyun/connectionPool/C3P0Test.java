package com.shiyun.connectionPool;

import java.sql.Connection;

import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;


/*
 * CEP0 documentation: https://www.mchange.com/projects/c3p0/#quickstart
 * 
 */
public class C3P0Test {
	
	@Test
	public void testGetConnection1() throws Exception {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setDriverClass( "com.mysql.jdbc.Driver" ); //loads the jdbc driver            
		cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/test" );
		cpds.setUser("root");                                  
		cpds.setPassword("seven"); 
		
		cpds.setInitialPoolSize(10);
		
		Connection conn = cpds.getConnection();
		System.out.println(conn);
		
		//destroy connection pool (normally don't do that)
		//DataSources.destroy(cpds);
	}
	
	
	@Test
	public void testGetConnection2() throws Exception {
		ComboPooledDataSource cpds = new ComboPooledDataSource("c3p0App"); //the name in c3p0-config.xml
		Connection conn = cpds.getConnection();
		System.out.println(conn);
	}

	
}
