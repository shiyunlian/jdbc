package com.shiyun.dao.improvement;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shiyun.util.JDBCUtils;


//DAO: data(base) access object
public abstract class BaseDAO <T>{
	
	private Class<T> clazz = null;
	
	public BaseDAO() {}
	
	{
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		ParameterizedType paramType = (ParameterizedType) genericSuperclass;
		Type[] typeArguments = paramType.getActualTypeArguments();
		clazz = (Class<T>) typeArguments[0];
	}

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
	
	
	public T getInstance(Connection conn, String sql, Object ...args) {
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
	
	
	public List<T> getForList(Connection conn, String sql, Object ...args){
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
			JDBCUtils.closeResource(null, ps, rs);
		}
		return null; //return null if data is not found
	}
	
	
	public <E>E getValue(Connection conn, String sql, Object ...args) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i+1, args[i]);
			}
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				return (E) rs.getObject(1);
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally {
			JDBCUtils.closeResource(null, ps, rs);
		}
		return null;
	}
}
