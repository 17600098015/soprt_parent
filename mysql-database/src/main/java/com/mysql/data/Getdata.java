package com.mysql.data;

import com.mysql.util.ConnectionUtil;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Getdata {


    public Getdata() throws SQLException {
    }

    public static Connection getConn(){
        Map map=new HashMap();
        map.put("driver", "com.mysql.jdbc.Driver");
        map.put("url", "jdbc:mysql://127.0.0.1:3306/lianxi?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true");
        map.put("username", "root");
        map.put("pass", "root");
        Connection conn = null;//获取conn的工具类
        try {
            conn = ConnectionUtil.getConnection(map);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    // DatabaseMetaData类的操作演示
    public void DBMetaDemo() throws Exception {

        DatabaseMetaData dm = getConn().getMetaData();
        System.out.println(dm.getDriverName());
        System.out.println(dm.getDriverVersion());
        System.out.println(dm.getDriverMajorVersion());
        System.out.println(dm.getDriverMinorVersion());
        System.out.println(dm.getMaxStatements());
        System.out.println(dm.getJDBCMajorVersion());
        System.out.println("---------1--------");
        //获取数据库名
        ResultSet rs = dm.getCatalogs();
        while (rs.next()) {
            String name = rs.getString("TABLE_CAT");
            System.out.println(name);
        }
        System.out.println("---------1--------");
        //获取表名
        ResultSet rs3 = dm.getTables("lianxi", "lianxi", null, new String[] { "TABLE" });// 类型"TABLE"可从API文档中的getTableTypes()方法中查到
        while (rs3.next()) {
            String str = rs3.getString("TABLE_NAME"); // 字段名"TABLE_NAME"可从API文档中的getTables()方法中查到
            System.out.println(str);

        }
        System.out.println("---------1--------");
        // 获取一个数据库中表信息---写死的---如果把数据库名和表名写成活的就可获取任意数据库与数据表
        Statement st = getConn().createStatement();
        st.execute("use maosha"); // execute()方法可以执行任意sql，包含: use aa, create
        // database, create table, alter...
        ResultSet rs2 = st.executeQuery(" select * from miaosha_goods");
        while (rs2.next()) {
            String str = rs2.getString(4);
            System.out.println(str);

        }

        }

    // DatabaseMetaData类的操作演示
    public void DBMetaDemo2() throws Exception {
        DatabaseMetaData dm = getConn().getMetaData();
        ResultSet rs = dm.getCatalogs();
        while (rs.next()) {
            String name = rs.getString("TABLE_CAT");
            System.out.println(name);
        }
        ResultSet rs3 = dm.getTables("maosha", "maosha", null,
                new String[] { "TABLE" });// 类型"TABLE"可从API文档中的getTableTypes()方法中查到
        while (rs3.next()) {
            String str = rs3.getString("TABLE_NAME"); // 字段名"TABLE_NAME"可从API文档中的getTables()方法中查到
            System.out.println(str);
        }
        // 获取一个数据库中表信息---写死的---如果把数据库名和表名写成活的就可获取任意数据库与数据表
        Statement st = getConn().createStatement();
        st.execute("use maosha"); // execute()方法可以执行任意sql，包含: use aa, create
        // database, create table, alter...
        ResultSet rs2 = st.executeQuery(" select * from miaosha_goods");
        while (rs2.next()) {
            String str = rs2.getString(4);
            System.out.println(str);

        }

    }

    // ResultSetMetaData类的操作演示
    public void RSMetaDemo() throws Exception {
        Statement st = getConn().createStatement();
        String sql = "select * from lianxi.book"; // 跨库查询用"数据库名.表名",这部分可用DBMetadata把它做活
        ResultSet rs = st.executeQuery(sql);

        // ※※※
        ResultSetMetaData rsmd = rs.getMetaData();// 结果集元数据
        int columns = rsmd.getColumnCount(); // 字段数量/列数

        // ///输出表头////
        for (int i = 1; i <=columns; i++) {
            String fieldName=rsmd.getColumnName(i);//jdbc中的列序号都是从1开始
            System.out.print(fieldName+"\t");
        }

        System.out.println();
        System.out.println("--------------------------");

        while(rs.next()){
            //输出一行数据
            for(int i=0;i<columns;i++){
                String str = rs.getString(i+1);
                System.out.print(str+"\t");
            }
            System.out.println();
        }
    }

}
