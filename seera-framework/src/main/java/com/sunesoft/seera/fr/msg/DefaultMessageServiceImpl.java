package com.sunesoft.seera.fr.msg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhouz on 2016/5/18.
 */
@Service("messageService")
public class DefaultMessageServiceImpl implements MessageService {
    @Autowired
    private MessageChannelManager messageChannelManager;

    @Override
    public void sendMessage(ChannelType type, Msg msg) {
        MessageChannel channel =  messageChannelManager.getMessageChannel(type);
        if(channel!=null){
            channel.process(msg);
        }
    }
}
