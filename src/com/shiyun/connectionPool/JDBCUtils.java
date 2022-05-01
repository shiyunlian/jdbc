package com.shiyun.connectionPool;

import java.sql.Connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JDBCUtils {
	
	public static Connection getConnection1() throws Exception {
		
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		Connection conn = cpds.getConnection();
		
		return conn;
	}

}
