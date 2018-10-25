package com.example.demo.repository;

import com.example.demo.model.AyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AyUserRepository extends JpaRepository<AyUser,String> {


    /**
     * 通过名字相等来查询，参数为name
     */
    List<AyUser> findByName(String name);

    /**
     * 通过名字like查询，参数为name
     */
    List<AyUser> findByNameLike(String name);

    /**
     * 通过主键集合查询
     */
    List<AyUser> findByIdIn(Collection<String> ids);

}
