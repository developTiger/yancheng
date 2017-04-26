package com.sunesoft.seera.yc.core.eHr.application;

import com.sunesoft.seera.fr.events.BaseEvent;

import java.util.UUID;

public class Eventtset extends BaseEvent {


    public Eventtset(UUID eId) {
        super(eId);
    }


    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
