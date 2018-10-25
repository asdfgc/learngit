package com.example.demo.service.impl;

import com.example.demo.dao.AyUserDao;
import com.example.demo.model.AyUser;
import com.example.demo.repository.AyUserRepository;
import com.example.demo.service.AyUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

//开启事物，意味着此类的所有public方法都是开启事物的我
@Transactional
@Service("ayUserService")
public class AyUserServiceImpl implements AyUserService {


    @Resource(name="ayUserRepository")
     private AyUserRepository ayUserRepository;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private AyUserDao ayUserDao;

    private static final String ALL_USER="ALL_USER_LIST";

    @Override
    public AyUser findById(String id) {

        /**
         * 老版本有findOne(id)这个方法，新版本参数改了，用getOne
         * ayUserRepository.findOne(id);
         */

        //step 1 查询redis缓存中所有数据
        List<AyUser> ayUserList=redisTemplate.opsForList().range(ALL_USER,0,-1);
        if(ayUserList!=null &&ayUserList.size()>0){
            for(AyUser user:ayUserList){
                if(user.getId().equals(id)){

                    return user;
                }
            }
        }
        //查询数据库中的数据
        AyUser ayUser=ayUserRepository.getOne(id);
        if(ayUser!=null){
            //将数据插入到Redis缓存中
            redisTemplate.opsForList().leftPush(ALL_USER,ayUser);
        }
        return ayUserRepository.getOne(id);

    }

    @Override
    public List<AyUser> findAll() {

        Logger logger = LogManager.getLogger(this.getClass());

        try {
            System.out.println("开始做任务");
            long start = System.currentTimeMillis();
            List<AyUser> ayUserList = ayUserRepository.findAll();
            long end = System.currentTimeMillis();
            System.out.println("完成任务，耗时：" + (end - start) + "毫秒");
            return ayUserList;

        }catch (Exception e){

            logger.error("method [findAll] error",e);
            return Collections.EMPTY_LIST;
        }
    }

    @Transactional
    @Override
    public AyUser save(AyUser ayUser) throws NullPointerException {

       AyUser saveUser= ayUserRepository.save(ayUser);
       //出现空指针异常
        String error=null;
        error.split("/");
        System.out.println(error);
        return saveUser;

    }

    @Override
    public void delete(String id) {

        Logger logger = LogManager.getLogger(this.getClass());

        ayUserRepository.deleteById(id);

        logger.info("userId:"+id+"用户被删除");
    }

    /**
     * 实现分页接口
     * @param pageable
     * @return
     */
    @Override
    public Page<AyUser> findAll(Pageable pageable) {
        return ayUserRepository.findAll(pageable);
    }

    @Override
    public List<AyUser> findByName(String name) {
        return ayUserRepository.findByName(name);
    }

    @Override
    public List<AyUser> findByNameLike(String name) {
        return ayUserRepository.findByNameLike(name);
    }

    @Override
    public List<AyUser> findByNameIdIn(Collection<String> ids) {
        return ayUserRepository.findByIdIn(ids);
    }

    @Override
    public AyUser findByNameAndPassword(String name, String password) {

        return ayUserDao.findByNameAndPassword(name,password);
    }

    @Override
    @Async
    public Future<List<AyUser>> findAsynAll() {

        Logger logger = LogManager.getLogger(this.getClass());
        try{
            System.out.println("开始做任务");
            long start = System.currentTimeMillis();
            List<AyUser> ayUserList = ayUserRepository.findAll();
            long end = System.currentTimeMillis();
            System.out.println("完成任务，耗时：" + (end - start) + "毫秒");
            return new AsyncResult<List<AyUser>>(ayUserList) ;
        }catch (Exception e){
            logger.error("method [findAll] error",e);
            return new AsyncResult<List<AyUser>>(null);
        }
    }


}
