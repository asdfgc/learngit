package com.example.demo.mail.impl;

import com.example.demo.mail.SendJunkMailService;
import com.example.demo.model.AyUser;
import com.example.demo.service.AyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * 发送用户邮件服务
 */
@Service
public class SendJunkMailServiceImpl implements SendJunkMailService {

    //邮件发送接口，通过Autowired注入进来就可以
   @Autowired
   JavaMailSender mailSender;

   @Resource
   private AyUserService ayUserService;

   //可以将application.properties配置文件中的配置设置到属性中，可以将username的值，设置给from属性
   @Value("${spring.mail.username}")
   private String from;

   public static final Logger logger= LogManager.getLogger(SendJunkMailServiceImpl.class);

    @Override
    public Boolean sendJunkMail(List<AyUser> ayUserList) {

        try {
            if(ayUserList==null||ayUserList.size()<0){
                return false;
            }else{
                for(AyUser ayUser:ayUserList){

                    MimeMessage mimeMessage=this.mailSender.createMimeMessage();
                    MimeMessageHelper message=new MimeMessageHelper(mimeMessage);
                    //邮件发送方
                    message.setFrom(from);
                    //邮件主题
                    message.setSubject("人呢人呢");
                    //邮件接收方
                    message.setTo("zzfms0424@163.com");
                    //邮件内容
                    message.setText(ayUser.getName()+",成功啦成功");
                    //发送邮件
                    this.mailSender.send(mimeMessage);
                }
            }
        }catch (Exception e){
            logger.error("sendJunkMail error and ayUser=%s",ayUserList,e);
            return false;
        }
            return  true;
    }
}
