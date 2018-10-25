package com.example.demo.service;

import com.example.demo.model.AyUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

public interface AyUserService {

    public AyUser findById(String id);

    public List<AyUser> findAll();

    public AyUser save(AyUser ayUser);

    public void delete(String id);

    //分页
    Page<AyUser> findAll(Pageable pageable);
    List<AyUser> findByName(String name);
    List<AyUser> findByNameLike(String name);
    List<AyUser> findByNameIdIn(Collection<String> ids);

    /*
     *根据名字和密码查询对象
     */
    AyUser findByNameAndPassword(String name,String password);

    //异步查询
    Future<List<AyUser>> findAsynAll();


}
