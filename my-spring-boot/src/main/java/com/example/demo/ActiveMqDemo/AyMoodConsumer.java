package com.example.demo.ActiveMqDemo;

import com.example.demo.model.AyMood;
import com.example.demo.service.AyMoodService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 消费者
 */
@Component
public class AyMoodConsumer {


    @Resource
    private AyMoodService ayMoodService;

    //使用JmsListener配置消费者监听的队列ay.queue，其中text是接收到的消息
    @JmsListener(destination = "ay.queue")
    public void receoveQueue(String text){

        System.out.println("用户发表说说{"+text+"}成功");
    }


    //异步队列
    @JmsListener(destination = "ay.queue.asyn.save")
    public void receoveQueue(AyMood ayMood){

        ayMoodService.save(ayMood);
    }

}
