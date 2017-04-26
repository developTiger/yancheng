package com.sunesoft.seera.yc.webapp.wxoauth;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bing on 16/8/25.
 */
public class ErrorResponse {

    @SerializedName("errcode")
    private String errCode;

    @SerializedName("errmsg")
    private String errMsg;

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
