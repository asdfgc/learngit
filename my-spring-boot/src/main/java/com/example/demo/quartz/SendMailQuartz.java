package com.example.demo.quartz;

import com.example.demo.mail.SendJunkMailService;
import com.example.demo.model.AyUser;
import com.example.demo.service.AyUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


/**
 * 描述：定时器类
 *
 */
@Component
//配置此注解的类相当于XML配置文件，可以被Spring Boot扫描初始化
@Configurable
//开启对计划任务的支持
@EnableScheduling
public class SendMailQuartz {

    //日志对象
    private static final Logger logger= LogManager.getLogger(SendMailQuartz.class);

    @Resource
    private SendJunkMailService sendJunkMailService;

    @Resource
    private AyUserService ayUserService;

    //每5秒执行一次
    //注解为定时任务，在cron表达式里写执行的时机
    @Scheduled(cron="*/5 * *  * * * ")
    public void reportCurrentByCron(){


        List<AyUser> list=ayUserService.findAll();
        if(list==null||list.size()<0){
            return;
        }else{
            sendJunkMailService.sendJunkMail(list);
        }
        logger.info("定时器运行了！！！");
    }




}
