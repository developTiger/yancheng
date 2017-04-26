package com.sunesoft.seera.fr.msg.innerNotice;

import com.sunesoft.seera.fr.msg.Msg;

/**
 * Created by zhouz on 2016/5/18.
 */
public class InnerNotice extends Msg{
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
