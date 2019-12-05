package com.mysql.dao;

import com.mysql.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
@Mapper
public interface Dao {
    List<Map> queryAddress();

    Map queryIpById(Integer id);

    int countTask(String taskid);

    void delTask(String  taskid);

    void addTask(String  taskid);

    void addOneTenth(String taskid);

    void minusOne(String taskid);

    @Select("SELECT * FROM  user")
    List<User>  getDatabase();

    @Select("SELECT * FROM  user where id= #{id}")
    User getById(int id);



}
