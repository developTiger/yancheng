package com.sunesoft.seera.yc.webapi.tourist;

/**
 * Created by zhaowy on 2016/12/6.
 */
public class TouristInfo {

    /**
     * 微信openId
     */
    private String openid;//用户的唯一标识

    /**
     * 用户名
     */
    private String userName;

    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 微信昵称
     */
    private String wxName;

    /**
     * 积分
     */
    private Integer integrals;

    /**
     * 微信头像
     */
    private String headimgurl;

//    /**
//     * 微信号UnionId
//     * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
//     */
//    private String unionid;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public Integer getIntegrals() {
        return integrals;
    }

    public void setIntegrals(Integer integrals) {
        this.integrals = integrals;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }
//
//    public String getUnionid() {
//        return unionid;
//    }
//
//    public void setUnionid(String unionid) {
//        this.unionid = unionid;
//    }
}
