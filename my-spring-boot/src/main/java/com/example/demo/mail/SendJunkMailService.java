package com.example.demo.mail;

import com.example.demo.model.AyUser;
import java.util.List;

/**
 * 发送用户邮件服务
 *
 */
public interface SendJunkMailService {

    Boolean sendJunkMail(List<AyUser> ayUser);
}
