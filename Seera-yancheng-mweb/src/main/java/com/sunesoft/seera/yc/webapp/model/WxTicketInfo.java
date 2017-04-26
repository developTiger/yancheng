package com.sunesoft.seera.yc.webapp.model;

import com.sunesoft.seera.yc.webapp.wx.Error;

/**
 * Created by jade on 2016/12/7.
 */
public class WxTicketInfo extends Error{

    private String access_token;

    private long expires_in;

    private String jsapi_ticket;

    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getJsapi_ticket() {
        return jsapi_ticket;
    }

    public void setJsapi_ticket(String jsapi_ticket) {
        this.jsapi_ticket = jsapi_ticket;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }
}
