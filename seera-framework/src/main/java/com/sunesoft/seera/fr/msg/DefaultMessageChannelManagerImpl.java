package com.sunesoft.seera.fr.msg;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhouz on 2016/5/18.
 */
@Service("messageChannelManager")
public class DefaultMessageChannelManagerImpl implements MessageChannelManager {

    @Resource
    private List<MessageChannelSelector> messageChannelSelectors;

    @Override
    public MessageChannel getMessageChannel(ChannelType type) {
        for (MessageChannelSelector selector:messageChannelSelectors){
            if(selector.getChannel(type)!=null){
                System.out.println(selector.getChannel(type));
                return selector.getChannel(type);
            }
        }
        return null;
    }
}
