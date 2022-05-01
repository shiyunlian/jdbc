package com.shiyun.blob;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.Test;

import com.shiyun.util.JDBCUtils;

/*
 * use PreparedStatement to add batch data
 * update and delete can manipulate batch data
 * insert can manipulate one data
 * 
 * insert 20,000 records into goods table
 * create table goods{
 	id int primary key auto_increment,
 	name varchar(25)
 	);
 */
public class InsertTest {

	//Time requires 122951
//	@Test
//	public void testInsert1() {
//		Connection conn = null;
//		PreparedStatement ps = null;
//		
//		try {
//			long startTime = System.currentTimeMillis();
//			
//			conn = JDBCUtils.getConnection();
//			String sql = "insert into goods(id, name) values(?, ?)";
//			ps = conn.prepareStatement(sql);
//			
//			for(int i = 1; i<=20000; i++) {
//				ps.setObject(1, i);
//				ps.setObject(2, "name_" + i);
//				ps.execute(); // execute immediately
//			}
//			
//			long endTime = System.currentTimeMillis();
//			
//			System.out.println("Time requires "+(endTime-startTime));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally {
//			JDBCUtils.closeResource(conn, ps);
//		}
//	}
	
	
	//Time requires 951
//	@Test 
//	public void testInsert2() {
//		Connection conn = null;
//		PreparedStatement ps = null;
//		
//		try {
//			long startTime = System.currentTimeMillis();
//			
//			conn = JDBCUtils.getConnection();
//			String sql = "insert into goods(id, name) values(?, ?)";
//			ps = conn.prepareStatement(sql);
//			
//			for(int i = 1; i<=20000; i++) {
//				ps.setObject(1, i);
//				ps.setObject(2, "name_" + i);
//				
//				//accumulate sqls
//				ps.addBatch();
//				if(i % 500 == 0) {
//					//execute batch
//					ps.executeBatch();
//					
//					//clear batch
//					ps.clearBatch();
//				}
//			}
//			
//			long endTime = System.currentTimeMillis();
//			
//			System.out.println("Time requires "+(endTime-startTime));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally {
//			JDBCUtils.closeResource(conn, ps);
//		}
//	}
	
	//Time requires 876
	@Test
	public void testInsert3() {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			long startTime = System.currentTimeMillis();
			
			conn = JDBCUtils.getConnection();
			
			//do not auto commit every time
			conn.setAutoCommit(false);
			
			String sql = "insert into goods(id, name) values(?, ?)";
			ps = conn.prepareStatement(sql);
			
			for(int i = 1; i<=20000; i++) {
				ps.setObject(1, i);
				ps.setObject(2, "name_" + i);
				
				//accumulate sqls
				ps.addBatch();
				if(i % 500 == 0) {
					//execute batch
					ps.executeBatch();
					
					//clear batch
					ps.clearBatch();
				}
			}
			// commit after all the executions
			conn.commit();
			long endTime = System.currentTimeMillis();
			
			System.out.println("Time requires "+(endTime-startTime));
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.closeResource(conn, ps);
		}
	}
}
