package org.jeecg.modules.message.handle.impl;

import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.message.handle.ISendMsgHandle;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailSendMsgHandle implements ISendMsgHandle {
    static String emailFrom;

    public static void setEmailFrom(String emailFrom) {
        EmailSendMsgHandle.emailFrom = emailFrom;
    }

    @Override
    public void sendMsg(String receiver, String title, String content) {
        JavaMailSender mailSender = (JavaMailSender) SpringContextUtils.getBean("mailSender");
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置发送方邮箱地址
        message.setFrom(emailFrom);
        message.setTo(receiver);
        message.setSubject(title);
        message.setText(content);
        mailSender.send(message);

    }
}
