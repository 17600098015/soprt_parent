package com.mysql.data;

import com.mysql.dao.Dao;
import com.mysql.util.JdbcUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MoveData implements Runnable{
    private JdbcUtils jdbcUtils;
    private Dao dao;
    private Map dataSource;

    private Map tarSource;

    private String sql;

    private String taleName;
    String taskid="rw201912031040";
    public MoveData(Map dataSource, Map tarSource, String sql, String taleName) {
        super();
        this.dataSource = dataSource;
        this.tarSource = tarSource;
        this.sql = sql;
        this.taleName = taleName;
    }

    public void run(){
        dao.addOneTenth(taskid);

        List<Map> queryListData = queryListData();
        List<String> checkSql = jdbcUtils.checkSql(queryListData, taleName);
        try {
            jdbcUtils.createSqls(tarSource,checkSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dao.delTask(taskid);
    }

    public List<Map> queryListData(){
        List<Map> queryData = null;
        try {
            queryData = jdbcUtils.queryData(dataSource,sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return queryData;
    }
}
