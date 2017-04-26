package com.sunesoft.seera.yc.webapp.model;

import com.google.gson.annotations.SerializedName;
import com.sunesoft.seera.yc.webapp.wx.Error;

import java.io.Serializable;

/**
 * Created by jade on 2016/12/7.
 */
public class JsApiTicket extends Error implements Serializable {

    /**
     * jsapi——ticket获取
     */
    @SerializedName("ticket")
    private String ticket;

    /**
     * 有效期
     */
    @SerializedName("expires_in")
    private long expiresIn;


    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
