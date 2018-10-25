package com.example.demo.quartz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 描述:定时器类
 *
 */
public class TestTask {

    //日志对象
    private static final Logger logger= LogManager.getLogger(String.valueOf(TestTask.class));

    public void run(){
        logger.info("定时器运行了!");
    }


}
