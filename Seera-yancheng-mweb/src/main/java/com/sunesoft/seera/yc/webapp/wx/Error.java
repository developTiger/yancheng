package com.sunesoft.seera.yc.webapp.wx;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by bing on 16/9/5.
 */
public class Error implements Serializable {

    /**
     * 错误代码.
     */
    @SerializedName("errcode")
    private int errCode;

    /**
     * 错误信息.
     */
    @SerializedName("errmsg")
    private String errMsg;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
