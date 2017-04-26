package com.sunesoft.seera.yc.core.activity;

import com.sunesoft.seera.fr.msg.ChannelType;
import com.sunesoft.seera.fr.msg.MessageService;
import com.sunesoft.seera.fr.msg.email.EmailMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by temp on 2016/10/12.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ddd {

    @Autowired
    MessageService messageService;

    @Test
    public void msgTest(){
        EmailMessage message = new EmailMessage();
        message.setMessage("给习大大发邮件！！！");
        messageService.sendMessage(ChannelType.Email, message);
    }
}
