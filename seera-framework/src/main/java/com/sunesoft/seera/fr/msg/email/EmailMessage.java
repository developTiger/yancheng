package com.sunesoft.seera.fr.msg.email;

import com.sunesoft.seera.fr.msg.Msg;

/**
 * Created by zhouz on 2016/5/18.
 */
public class EmailMessage extends Msg {

    public static final String ENCODEING = "UTF-8";

    private String host="smtp.mxhichina.com"; // 服务器地址

/*    private String sender; // 发件人的邮箱

    private String receiver; // 收件人的邮箱*/

    private String sender="service@sunesoft.com"; // 账号 发件人的邮箱

    private String password="Sy123456"; // 密码

    private String subject; // 主题

    private String message; // 信息(支持HTML)

    private String receiver; // 收件人的邮箱

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
