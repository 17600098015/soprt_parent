package com.mysql.util;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConnectionUtil {

	private ConnectionUtil() {
	}


	// 加载数据驱动类 第一次运行时加载一次即可
	static {


	}
	/**
	 * 获取连接
	 */
	public static Connection getConnection(Map dataSourceMap) throws SQLException {
		String driver = (String) dataSourceMap.get("driver");
		String url = (String) dataSourceMap.get("url");
		String username = (String) dataSourceMap.get("username");
		String pass = (String) dataSourceMap.get("pass");
		Connection connection=null;
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		 DriverManager.getConnection(url, username, pass);

		 return connection;

	}

	/**
	 * 关闭连接
	 */
	public static void closeConnection(Connection connection) {
		try {
			if (null != connection) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println("关闭连接时发生异常");
		}
	}

	/**
	 * 测试
	 */
//	public static void main(String[] args) throws SQLException {
////		Map map=new HashMap();
////		map.put("driver", "com.mysql.jdbc.Driver");
////		map.put("url", "jdbc:mysql://127.0.0.1:3306/lianxi?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true");
////		map.put("username", "root");
////		map.put("pass", "root");
//		try {
//			Connection connection = getConnection(map);
//			System.out.println(connection);
//			System.out.println(connection.isClosed());
//			closeConnection(connection);
//			System.out.println(connection.isClosed());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}


}