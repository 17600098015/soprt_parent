package com.mysql.util;


import java.sql.*;
import java.util.*;


public class JdbcUtils {
//	//查询sql
//	private static final String SQL = "SELECT * FROM ";
//
//	//查询表结构
//	private static final String SQL2 = "show create table  ";
//	//获取表结构部分字段
//	private static final String SQL3 ="describe ";
//	//预迁移数据表
//	private static final String tableName = "shujubiao";
//	private static final String PAGE = "select COUNT(0) from ";
//	private static final String tableSql = SQL + tableName;
//	private static final String tableSql2 = SQL2 + tableName;
//	private static final String tableSql3 = SQL3 + tableName;
//	private static final String tableSql4 = PAGE+ tableName ;
	/**
	 * 链接jdbc
	 * @return
	 */
	public static  Connection getConnection(Map dataSourceMap){
		String driver = (String) dataSourceMap.get("driver");
		String url = (String) dataSourceMap.get("url");
		String username = (String) dataSourceMap.get("username");
		String pass = (String) dataSourceMap.get("pass");
		Connection connection=null;
		 try {
			 Class.forName(driver);
			 connection = DriverManager.getConnection(url,username,pass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

	public static void main(String[] args) {
		Map targetMap=new HashMap();
		targetMap.put("driver", "com.mysql.jdbc.Driver");
		targetMap.put("url", "jdbc:mysql://127.0.0.1:3306");
		targetMap.put("username", "root");
		targetMap.put("pass", "root");
		List<String> dataTableName = getDataTableName(targetMap,"1702a");
		System.out.println(dataTableName.toString());
	}

	//获取数据表名
	public static List<String> getDataTableName(Map dataSourceMap, String database) {
		Connection connection = getConnection(dataSourceMap);
		List<String> list = new ArrayList<>();
		try {
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet resultSet = databaseMetaData.getTables(database, null, null, new String[]{"TABLE"});
			while (resultSet.next()) {
				list.add(resultSet.getString("TABLE_NAME"));
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	//查询
	public static List<Map> queryData(Map dataSourceMap,String sql) throws SQLException{
		Connection connection = getConnection(dataSourceMap);
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		ResultSet executeQuery = prepareStatement.executeQuery();
		ResultSetMetaData metaData = executeQuery.getMetaData();
		int columnCount = metaData.getColumnCount();
		Map resultmap=null;

		List<Map> resullist=new ArrayList();
		while(executeQuery.next()){
			resultmap =new HashMap();
			for(int i = 1;i<=columnCount;i++){
				String key = metaData.getColumnLabel(i);
				//System.out.println(key+""+executeQuery.getObject(i));
				resultmap.put(key, executeQuery.getObject(i));
			}
			resullist.add(resultmap);
		}
		close(executeQuery, prepareStatement, connection);
		return resullist;
	}
	//分页查询
	public static int  pageData(Map dataSourceMap,String sql) throws SQLException{
		Connection connection = getConnection(dataSourceMap);
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		int count=0;
		while(rs.next()){
				count = Integer.parseInt(rs.getString(1));
			System.out.println(Integer.parseInt(rs.getString(1)));
		}
		return count;
	}

	/**
	 * 查询表数据结构
	 * @param dataSourceMap
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static List<Map> queryTable(Map dataSourceMap,String sql) throws SQLException{
		Connection connection = getConnection(dataSourceMap);
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		ResultSet executeQuery = prepareStatement.executeQuery();
		ResultSetMetaData metaData = executeQuery.getMetaData();
		int columnCount = metaData.getColumnCount();
		Map resultmap=null;
		List<Map> tablelist=new ArrayList();
		while(executeQuery.next()){
			resultmap =new HashMap();
			for(int i = 2;i<=columnCount;i++){
				String key = metaData.getColumnLabel(i);
				//System.out.println(key+"表结构22"+executeQuery.getObject(i));
				System.out.println("key:"+key);
				resultmap.put(key, executeQuery.getObject(i));
			}
			tablelist.add(resultmap);
		}
		close(executeQuery, prepareStatement, connection);
		return tablelist;
	}
	//查询部分字段
	public static List<Map> partTable(Map dataSourceMap,String sql) throws SQLException{
		Connection connection = getConnection(dataSourceMap);
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		ResultSet executeQuery = prepareStatement.executeQuery();
		ResultSetMetaData metaData = executeQuery.getMetaData();
		int columnCount = metaData.getColumnCount();
		Map partMap=null;
		List<Map> partTablelist=new ArrayList();
		while(executeQuery.next()){
			partMap =new HashMap();
			for(int i = 1;i<=columnCount;i++){
				String key = metaData.getColumnLabel(i);
				//System.out.println(key+"表结构22"+executeQuery.getObject(i));
				System.out.println("key:"+key);
				System.out.println("value"+executeQuery.getObject(i));
				partMap.put(key, executeQuery.getObject(i));
			}
			partTablelist.add(partMap);
		}
		close(executeQuery, prepareStatement, connection);
		return partTablelist;
	}

	public static void affair(Map dataSourceMap,String sql) throws SQLException {
		Connection connection = getConnection(dataSourceMap);
		connection.setAutoCommit(false);
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		int executeUpdate = prepareStatement.executeUpdate();
		connection.commit();
		close(null, prepareStatement, connection);
	}


	//连接数据库提交事务
	public static void createSqls(Map dataSourceMap,List<String> sqltList) throws SQLException {
		Connection connection = getConnection(dataSourceMap);
		connection.setAutoCommit(false);
		Statement createStatement = connection.createStatement();
		for (String string : sqltList) {
			createStatement.addBatch(string);
		}
		int[] executeBatch = createStatement.executeBatch();

		connection.commit();
		createStatement.close();
		close(null, null, connection);
	}



	//生成插入sql语句
	public static List<String> checkSql(List<Map> list,String tableName){
		StringBuffer sbuf=new StringBuffer();
		List<String> sqllist =new ArrayList();
		for (Map map : list) {
			sbuf.append("INSERT INTO ");
			sbuf.append(tableName);
			sbuf.append("(");
			Set<String> keySet = map.keySet();
			for (String key : keySet) {
				sbuf.append(key);
				sbuf.append(",");
			}
			String newString = sbuf.substring(0, sbuf.length()-1);
			sbuf.setLength(0);
			sbuf.append(newString);
			sbuf.append(") values (");
			for (String key : keySet) {
				sbuf.append("'");
				sbuf.append(map.get(key));
				sbuf.append("'");
				sbuf.append(",");
			}
			String newStringVlues = sbuf.substring(0, sbuf.length()-1);
			sbuf.setLength(0);
			sbuf.append(newStringVlues);
			sbuf.append(")");
			sqllist.add(sbuf.toString());
			sbuf.setLength(0);
		}
		return sqllist;
	}

	/**
	 * 拼接建表sql语句
	 * 动态获取数据库表结构,并插入指定数据库
	 * @param list
	 * @param tableName
	 * @return
	 */
	public static List<String> createSql(List<Map> list,String tableName){
		StringBuffer sbuf=new StringBuffer();
		List<String> sqltlist =new ArrayList();
		for (Map map : list) {
			System.out.println("---------------"+map.toString());
			for (Object key : map.keySet()) {
				Object value = map.get(key);
				sbuf.append(value+",");
				System.out.println(sbuf);
			}
			String newStringVlues = sbuf.substring(0, sbuf.length()-1);
			sbuf.setLength(0);
			sbuf.append(newStringVlues);
			sqltlist.add(sbuf.toString());
			sbuf.setLength(0);
		}
		return sqltlist;
	}


		/**
		 * jdbc关流
		 * @param ex
		 * @param pr
		 * @param co
		 */
		public static void close(ResultSet ex,PreparedStatement pr,Connection co){
			try {
				if (ex!=null) ex.close();
				if (pr!=null) pr.close();
				if (co!=null) co.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		
		}


	public static List<String> getTableNames(Map dataSourceMap) {
        List<String> tableNames = new ArrayList<>();
        Connection conn = getConnection(dataSourceMap);
        ResultSet rs = null;
        try {
            //获取数据库的元数据
            DatabaseMetaData db = conn.getMetaData();
            //从元数据中获取到所有的表名
            rs = db.getTables(null, null, null, new String[]{"TABLE"});
            while (rs.next()) {
                tableNames.add(rs.getString(3));
            }
        } catch (SQLException e) {
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
            }
        }
        return tableNames;
    }
	
}
