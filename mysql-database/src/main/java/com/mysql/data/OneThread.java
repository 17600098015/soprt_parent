package com.mysql.data;

import com.mysql.dao.Dao;
import com.mysql.util.JdbcUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OneThread implements Runnable {
    private Dao dao;
    String taskid="rw201912031040";
    private JdbcUtils jdbcUtils;
    @Override
    public void run() {
        dao.delTask(taskid);
        dao.addTask(taskid);

        String tableName = "shujubiao";
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
        List<Map> queryData = null;
        try {
            queryData = jdbcUtils.queryData(map, "select COUNT(1)  as count from shujubiao");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Map map1 = queryData.get(0);
        int parseInt = Integer.parseInt(map1.get("count").toString());
        int pageSize = 10000;
        long pageCount=parseInt%pageSize==0?parseInt/pageSize:parseInt/pageSize+1;
        int count = 1;
        for (int i = 0; i <pageCount ; i++) {
            String pageSql="select * from "+tableName+" limit "+i*pageSize+","+pageSize;
            count++;
            if(count==10){
                checktask();
                count=0;
            }
            new Thread(new MoveData(map,targetMap,pageSql,tableName)).start();
        }

    }

    public void checktask(){

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int countTask = dao.countTask(taskid);
        if(countTask==0){
            return;
        }
        checktask();
    }




    }

