package com.sunesoft.seera.yc.webapp.wx;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by bing on 16/9/5.
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

    /**
     * 用户刷新access_token
     */
    @SerializedName("refresh_token")
    private String refreshToken;

    /**
     * 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
     */
    @SerializedName("openid")
    private String openId;

    /**
     * 用户授权的作用域，使用逗号（,）分隔
     */
    @SerializedName("scope")
    private String scope;

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

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
