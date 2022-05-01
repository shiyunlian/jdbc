package com.shiyun.dao.improvement;

import java.sql.Connection;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.shiyun.bean.Customer;
import com.shiyun.util.JDBCUtils;

class CustomerDAOImplTest {

	private CustomerDAOImpl dao = new CustomerDAOImpl();
	
	@Test
	void testInsert() {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			Customer customer = new Customer(14, "hazel", "hazel@gmail.com");
			dao.insert(conn, customer);
			System.out.println("Insert Success!");
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally {
			JDBCUtils.closeResource(conn, null);
		}
	}

	@Test
	void testDeleteById() {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			
			dao.deleteById(conn, 14);
			System.out.println("Delete Success!");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.closeResource(conn, null);
		}
	}

	@Test
	void testUpdateById() {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			Customer customer = new Customer(1, "hazel2", "hazel@gmail.com");
			dao.updateById(conn, 14, customer);
			System.out.println("Update Success!");
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally {
			JDBCUtils.closeResource(conn, null);
		}
	}

	@Test
	void testGetCustomerById() {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			Customer customer = dao.getCustomerById(conn, 12);
			//System.out.println(customer);
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally {
			JDBCUtils.closeResource(conn, null);
		}
	}

	@Test
	void testGetAll() {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			List<Customer> list = dao.getAll(conn);
			list.forEach(System.out::println);
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally {
			JDBCUtils.closeResource(conn, null);
		}
	}

	@Test
	void testGetCount() {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			int count = dao.getCount(conn);
			System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.closeResource(conn, null);
		}
	}

}
