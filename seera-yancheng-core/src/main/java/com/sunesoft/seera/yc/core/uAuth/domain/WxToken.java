package com.sunesoft.seera.yc.core.uAuth.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by zwork on 2016/12/9.
 */
@Entity
public class WxToken extends BaseEntity {

    public WxToken(){
        this.setCreateDateTime(new Date());
        this.setIsActive(true);
        this.setLastUpdateTime(new Date());
    }

    private String token;

    private String ticket;

    private Long timeStamp;

    private String attr1;

    private String attr2;


    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getAttr1() {
        return attr1;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    public String getAttr2() {
        return attr2;
    }

    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }
}
