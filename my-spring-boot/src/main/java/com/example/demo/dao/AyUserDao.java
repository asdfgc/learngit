package com.example.demo.dao;

import com.example.demo.model.AyUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户dao
 */
//重要注解，MyBatis根据接口定义与Mapper文件中的SQL语句动态创建接口实现
@Mapper
public interface AyUserDao {

    /**
     * 通过用户名和密码查询用户
     * @param name
     * @param password
     */
    AyUser findByNameAndPassword(@Param("name") String name, @Param("password") String password);



}
