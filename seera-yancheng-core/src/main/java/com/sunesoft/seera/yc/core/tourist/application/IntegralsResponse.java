package com.sunesoft.seera.yc.core.tourist.application;

/**
 * Created by zhaowy on 2016/10/27.
 */
public class IntegralsResponse {
    private Obj obj;
    private String msg;
    private Boolean success;
    private String msgCode;

    public String getErrMsgCode() {
        return msgCode;
    }

    public void setErrMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public Obj getObj() {
        return obj;
    }

    public void setObj(Obj obj) {
        this.obj = obj;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}

class Obj {
    private String userId;
    private String channelId;
    private String integral;
    private String lastModifyTime;

    public String getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

}