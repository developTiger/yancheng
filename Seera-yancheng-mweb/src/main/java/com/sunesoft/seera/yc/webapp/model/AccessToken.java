package com.sunesoft.seera.yc.webapp.model;

import com.google.gson.annotations.SerializedName;
import com.sunesoft.seera.yc.webapp.wx.Error;

import java.io.Serializable;

/**
 * Created by jade on 2016/12/7.
 */
public class AccessToken extends Error implements Serializable {

    /**
     * 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
     */
    @SerializedName("access_token")
    private String accessToken;

    /**
     * access_token接口调用凭证超时时间，单位（秒）
     */
    @SerializedName("expires_in")
    private String expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }
}
