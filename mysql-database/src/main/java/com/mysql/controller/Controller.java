package com.mysql.controller;

import com.mysql.bean.User;
import com.mysql.service.Service;
import com.mysql.util.JdbcUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Controller
public class Controller {
    @Autowired
    private Service service;

    private JdbcUtils jdbcUtilss;


    public Map getBase(int id){
        User byId = service.getById(id);
        String username = byId.getDataname();
        String driver = byId.getDriver();
        String pass = byId.getPassword();
        String url = byId.getUrl();

        Map map = new HashMap();
        map.put("driver", driver);
        map.put("url", url);
        map.put("username", username);
        map.put("pass", pass);

        return map;
    }

    @RequestMapping("list")
    public String list(Model model){
        List<User> database = service.getDatabase();
        for (Object base: database
             ) {
        }

        model.addAttribute("database",database);
        return "list";
    }


    @RequestMapping("queryBase")
    @ResponseBody
    public List<Map> queryBase(int id) {
        Map map = getBase(id);
        List<Map> queryData = null;
        try {
            queryData = jdbcUtilss.queryData(map, "SHOW DATABASES");
            //System.out.println(queryData.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queryData;
    }

    @RequestMapping("queryDatabase")
    @ResponseBody
    public List<String> queryDatabase(int id,String dataTableName) {
        Map map = getBase(id);
        List<String> tableName = jdbcUtilss.getDataTableName(map, dataTableName);
        System.out.println(tableName.toString());
        return tableName;
    }
}
