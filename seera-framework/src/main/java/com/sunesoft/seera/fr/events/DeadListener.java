package com.sunesoft.seera.fr.events;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import com.sunesoft.seera.fr.annotations.EventListener;

/**
 * Created by zhouz on 2016/5/17.
 */
@EventListener
public class DeadListener {

    @Subscribe
    public void onEvent(DeadEvent de) {
        System.out.println("ERROR!!!发布了错误的事件:" + de.getEvent());
    }
}
