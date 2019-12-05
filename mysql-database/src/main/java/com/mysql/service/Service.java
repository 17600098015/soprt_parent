package com.mysql.service;

import com.mysql.bean.User;
import com.mysql.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class Service {
    @Autowired
    private Dao dao;
    public List<User> getDatabase() {
        return dao.getDatabase();
    }

    public User getById(int id){

        return dao.getById(id);
    }
}
