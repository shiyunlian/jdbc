package com.shiyun.blob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;

import com.shiyun.bean.Customer;
import com.shiyun.util.JDBCUtils;

/*
 * use PreparedStatement to manipulate blob data
 */
public class BlobTest {
	
	//insert blob data into customers table
	@Test
	public void testInsertBlob() {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = JDBCUtils.getConnection();
			String sql = "insert into customers(id, name, email, photo) values(?,?,?,?)";
			
			ps = conn.prepareStatement(sql);
			ps.setObject(1, 12);
			ps.setObject(2, "Lily");
			ps.setObject(3, "lily@gmail.com");
			
			FileInputStream inputStream = new FileInputStream(new File("trail.jpg"));
			ps.setBlob(4, inputStream);
			
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResource(conn, ps);
		}
		System.out.println("Insert success!");
	}
	
	//query blob data from customers table
	@Test
	public void testQueryBlob() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		InputStream inputStream  = null;
		FileOutputStream fileOutputStream = null;
		
		try {
			conn = JDBCUtils.getConnection();
			String sql = "select * from customers where id = ?";
			ps = conn.prepareStatement(sql);
			ps.setObject(1, 12);
			rs = ps.executeQuery();
			if(rs.next()) {
//			// method 1 to get the data corresponding to column index in the table
//			int id = rs.getInt(1);
//			String name = rs.getString(2);
//			String email = rs.getString(3);
				
				//method 2 to get the data corresponding to column name in the table
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				
				Customer customer = new Customer(id, name, email);
				
				Blob photo = rs.getBlob("photo");
				inputStream = photo.getBinaryStream();
				fileOutputStream = new FileOutputStream("downloaded_trail.jpg");
				byte[] buffer = new byte[1024];
				int len;
				while ((len = inputStream.read(buffer)) != -1) {
					fileOutputStream.write(buffer, 0, len);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			JDBCUtils.closeResource(conn, ps, rs);
		}
	}
}
