package com.sunesoft.seera.yc.core.activity;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Created by temp on 2016/10/9.
 */
public class MailTest {

    public static void sendMail(String fromMail, String user, String password,
                                String toMail,
                                String mailTitle,
                                String mailContent) throws Exception {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setUsername("service@sunesoft.com");
        mailSender.setPassword("Sy123456");   //qq邮箱开启smtp服务后使用16位授权码在第三方登录
//        mailSender.setHost("smtp.exmail.qq.com");
        mailSender.setHost("smtp.mxhichina.com");
        mailSender.setPort(465);


//        mailSender.setHost("smtp.163.com");      //163邮箱
//        mailSender.setPort(25);
        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true);
        //javaMailProperties.put("mail.smtp.auth", true);
        //javaMailProperties.put("mail.smtp.starttls.enable", true);
        mailSender.setJavaMailProperties(javaMailProperties);
        SimpleMailMessage smm = new SimpleMailMessage();
        // 设定邮件参数
        smm.setFrom(mailSender.getUsername());
        smm.setTo("1783709028@qq.com");
        smm.setSubject("Hello world奋斗开始打电话d");
        smm.setText("nice大大大 !");
        // 发送邮件
        mailSender.send(smm);

    }

    public static void main(String[] args) {

        try {
            sendMail("599283258@qq.com", "599283258@qq.com", "rfsdhdztgyhhdafb",
                    "1783709028@qq.com",
                    "Java Mail 测试邮件",
                    "<a>html 元素</a>：<b>邮件内容</b>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



   /*
   // 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
    public static String myEmailAccount = "599283258@qq.com";
    public static String myEmailPassword = "caocao..1176711";

    // 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般格式为: smtp.xxx.com
    // 网易163邮箱的 SMTP 服务器地址为: smtp.163.com
    public static String myEmailSMTPHost = "smtp.qq.com";

    // 收件人邮箱（替换为自己知道的有效邮箱）
    public static String receiveMailAccount = "1783709028@qq.com";

    public static void main(String[] args) throws Exception {
        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.host", myEmailSMTPHost);        // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 请求认证，参数名称与具体实现有关

        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);                                 // 设置为debug模式, 可以查看详细的发送 log

        // 3. 创建一封邮件
        MimeMessage message = createMimeMessage(session, myEmailAccount, receiveMailAccount);

        // 也可以保持到本地查看
        // message.writeTo(file_out_put_stream);

        // 4. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();

        // 5. 使用 邮箱账号 和 密码 连接邮件服务器
        //    这里认证的邮箱必须与 message 中的发件人邮箱一致，否则报错
        transport.connect(myEmailAccount, myEmailPassword);

        // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(message, message.getAllRecipients());

        // 7. 关闭连接
        transport.close();
    }

    *//**
     * 创建一封复杂邮件（文本+图片+附件）
     *//*
    public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail) throws Exception {
        // 1. 创建邮件对象
        MimeMessage message = new MimeMessage(session);

        // 2. From: 发件人
        message.setFrom(new InternetAddress(sendMail, "我的测试邮件_发件人昵称", "UTF-8"));

        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "我的测试邮件_收件人昵称", "UTF-8"));

        // 4. Subject: 邮件主题
        message.setSubject("TEST邮件主题（文本+图片+附件）", "UTF-8");


        // 11. 设置整个邮件的关系（将最终的混合“节点”作为邮件的内容添加到邮件对象）
        message.setContent("Hello World!!!","text/html;charset=gbk");

        // 12. 设置发件时间
        message.setSentDate(new Date());

        // 13. 保存上面的所有设置
        message.saveChanges();

        return message;
    }*/
}
