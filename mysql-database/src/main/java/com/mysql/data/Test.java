package com.mysql.data;

import com.mysql.util.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mysql.util.JdbcUtils.*;

public class Test {
    private JdbcUtils jdbcUtils;

    //预迁移数据表
    private static final String tableName = "shujubiao";
    private static final String PAGE = "select COUNT(1)  as count from ";
    private static final String SQL = "SELECT * FROM ";
    private static final String SQL2 = "show create table  ";
    private static final String SQL3 ="describe ";
    //查询sql
    private static final String selectSql = SQL + tableName;
    //查询表结构
    private static final String structureSql = SQL2 + tableName;
    //获取表结构部分字段
    private static final String parTialSql = SQL3 + tableName;
    //查询数据总条数
    private static final String countSql = PAGE+ tableName ;
    //查询所有数据库
    private static final String dbNameSql = "SHOW DATABASES";


    public  void  test() throws SQLException {

        //源数据库
        Map map=new HashMap();
        map.put("driver", "com.mysql.jdbc.Driver");
        map.put("url", "jdbc:mysql://127.0.0.1:3306/shuju?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true");
        map.put("username", "root");
        map.put("pass", "root");

        //目标数据库
        Map targetMap=new HashMap();
        targetMap.put("driver", "com.mysql.jdbc.Driver");
        targetMap.put("url", "jdbc:mysql://127.0.0.1:3306/1702a?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true");
        targetMap.put("username", "root");
        targetMap.put("pass", "root");

        //获得总数
        List<Map> queryData = jdbcUtils.queryData(map, countSql);
        Map map1 = queryData.get(0);
        int parseInt = Integer.parseInt(map1.get("count").toString());
        int pageSize = 10000;
        long pageCount=parseInt%pageSize==0?parseInt/pageSize:parseInt/pageSize+1;
        int count = 1;
        for (int i = 0; i <pageCount ; i++) {
            String pageSql="select * from "+tableName+" limit "+i*pageSize+","+pageSize;
            count++;
            if(count==10){
                count=0;
            }
            new Thread(new MoveData(map,targetMap,pageSql,tableName)).start();
        }

    }

    public static void main(String[] args) {
        Map map=new HashMap();
        map.put("driver", "com.mysql.jdbc.Driver");
        map.put("url", "jdbc:mysql://127.0.0.1:3306/lianxi?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true");
        map.put("username", "root");
        map.put("pass", "root");
        try {
            List<Map> queryData = queryData(map, dbNameSql);
            System.out.println(queryData.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    //创建新表并插入数据
    public  void  test2() throws SQLException {
        //源数据库
        Map map=new HashMap();
        map.put("driver", "com.mysql.jdbc.Driver");
        map.put("url", "jdbc:mysql://127.0.0.1:3306/lianxi?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true");
        map.put("username", "root");
        map.put("pass", "root");
        List<Map> queryTable = jdbcUtils.queryTable(map,structureSql);
		List<String> sql = jdbcUtils.createSql(queryTable, tableName);

        //目标数据库
        Map targetMap=new HashMap();
        targetMap.put("driver", "com.mysql.jdbc.Driver");
        targetMap.put("url", "jdbc:mysql://127.0.0.1:3306/1702a?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true");
        targetMap.put("username", "root");
        targetMap.put("pass", "root");
        jdbcUtils.createSqls(targetMap, sql);
        List<Map> queryData = queryData(map, selectSql);
        List<String> checkSql = checkSql(queryData, tableName);
        createSqls(targetMap,checkSql);
    }
}
