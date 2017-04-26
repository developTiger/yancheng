package com.sunesoft.seera.fr.msg;

/**
 * Created by zhouz on 2016/5/18.
 */
public interface MessageChannel {
    public void process(Msg message);
}
