package com.sunesoft.seera.fr.msg.email;

import com.sunesoft.seera.fr.msg.Msg;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


/**
 * Created by zhouz on 2016/5/18.
 */
@Service("smtpChennel")
public class SmtpMessageChannelImpl implements SmtpChennel {

    @Override
    public void process(Msg message) {
       /* System.out.println(((EmailMessage)message).getMessage());*/
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        EmailMessage emailMessage=(EmailMessage)message;
        mailSender.setUsername(emailMessage.getSender());
        mailSender.setPassword(emailMessage.getPassword());   //qq邮箱开启smtp服务后使用16位授权码在第三方登录
        mailSender.setHost(emailMessage.getHost());
        mailSender.setPort(465);
        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding(emailMessage.ENCODEING);
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true);
        mailSender.setJavaMailProperties(javaMailProperties);

        MimeMessage mail = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail);

        try {
            helper.setTo(emailMessage.getReceiver()); // 发送给谁
            helper.setSubject(emailMessage.getSubject()); // 标题
            helper.setFrom(mailSender.getUsername()); // 来自
            helper.setText(emailMessage.getMessage(),true);
            mailSender.send(mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
