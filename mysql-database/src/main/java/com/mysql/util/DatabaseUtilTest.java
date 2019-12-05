package com.mysql.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.sql.*;
import java.util.List;

public class DatabaseUtilTest {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtilTest.class);
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://127.0.0.1:3306/lianxi";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String SQL = "SELECT * FROM ";// 数据库操作
    private static final String tableName = "book";
    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            logger.error("can not load jdbc driver", e);
        }
    }

    /**
     * 获取数据库连接
     *
     * @return
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     *
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("close connection failure", e);
            }
        }
    }


    public static void main(String[] args) {
        List<String> tableNames = getTableNames();
        System.out.println("表m:" + getTableNames());

       //for (String tableName : tableName) {
//            System.out.println("================start==========================");
//            System.out.println("==============================================");
//            System.out.println("字段类型:" + getColumnTypes(tableName));
//            System.out.println("注释:" + getColumnComments(tableName));
//            System.out.println("==============================================");
//            System.out.println("=================end=======================");
        //}

    }

    /**
     * 获取数据库下的所有表名
     */
    public static List<String> getTableNames() {
        List<String> tableNames = new ArrayList<>();
        Connection conn = getConnection();
        ResultSet rs = null;
        try {
            //获取数据库的元数据
            DatabaseMetaData db = conn.getMetaData();
            System.out.println(db.getDriverName());
            System.out.println(db.getURL());
            System.out.println(db.getUserName());
            //从元数据中获取到所有的表名
            rs = db.getTables(null, null, null, new String[]{"TABLE"});
            while (rs.next()) {
                tableNames.add(rs.getString(3));
            }
        } catch (SQLException e) {
            logger.error("getTableNames failure", e);
        } finally {
            try {
                rs.close();
                closeConnection(conn);
            } catch (SQLException e) {
                logger.error("close ResultSet failure", e);
            }
        }
        return tableNames;
    }

//    /**
//     * 获取表中所有字段名称
//     *
//     * @param tableName 表名
//     * @return
//     */
//    public static List<String> getColumnNames(String tableName) {
//        List<String> columnNames = new ArrayList<>();
//        //与数据库的连接
//        Connection conn = getConnection();
//        PreparedStatement pStemt = null;
//        String tableSql = SQL + tableName;
//        try {
//            pStemt = conn.prepareStatement(tableSql);
//            System.out.println("???1"+pStemt);
//            //结果集元数据
//            ResultSetMetaData rsmd = pStemt.getMetaData();
//            System.out.println("???2"+rsmd);
//            //表列数
//            int size = rsmd.getColumnCount();
//            for (int i = 0; i < size; i++) {
//                columnNames.add(rsmd.getColumnName(i + 1));
//            }
//        } catch (SQLException e) {
//            logger.error("getColumnNames failure", e);
//        } finally {
//            if (pStemt != null) {
//                try {
//                    pStemt.close();
//                    closeConnection(conn);
//                } catch (SQLException e) {
//                    logger.error("getColumnNames close pstem and connection failure", e);
//                }
//            }
//        }
//        return columnNames;
//    }
//
//    /**
//     * 获取表中所有字段类型
//     *
//     * @param tableName
//     * @return
//     */
//    public static List<String> getColumnTypes(String tableName) {
//        List<String> columnTypes = new ArrayList<>();
//        //与数据库的连接
//        Connection conn = getConnection();
//        PreparedStatement pStemt = null;
//        String tableSql = SQL + tableName;
//        try {
//            pStemt = conn.prepareStatement(tableSql);
//            //结果集元数据
//            ResultSetMetaData rsmd = pStemt.getMetaData();
//
//            //表列数
//            int size = rsmd.getColumnCount();
//            for (int i = 0; i < size; i++) {
//                columnTypes.add(rsmd.getColumnTypeName(i + 1));
//            }
//        } catch (SQLException e) {
//            logger.error("getColumnTypes failure", e);
//        } finally {
//            if (pStemt != null) {
//                try {
//                    pStemt.close();
//                    closeConnection(conn);
//                } catch (SQLException e) {
//                    logger.error("getColumnTypes close pstem and connection failure", e);
//                }
//            }
//        }
//        return columnTypes;
//    }
//
//    /**
//     * 获取表中字段的所有注释
//     *
//     * @param tableName
//     * @return
//     */
//    public static List<String> getColumnComments(String tableName) {
//        List<String> columnTypes = new ArrayList<>();
//        //与数据库的连接
//        Connection conn = getConnection();
//        PreparedStatement pStemt = null;
//        String tableSql = SQL + tableName;
//        List<String> columnComments = new ArrayList<>();//列名注释集合
//        ResultSet rs = null;
//        try {
//            pStemt = conn.prepareStatement(tableSql);
//            rs = pStemt.executeQuery("show full columns from " + tableName);
//            while (rs.next()) {
//                columnComments.add(rs.getString("Comment"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            if (rs != null) {
//                try {
//                    rs.close();
//                    closeConnection(conn);
//                } catch (SQLException e) {
//                    logger.error("getColumnComments close ResultSet and connection failure", e);
//                }
//            }
//        }
//        return columnComments;
//    }


}