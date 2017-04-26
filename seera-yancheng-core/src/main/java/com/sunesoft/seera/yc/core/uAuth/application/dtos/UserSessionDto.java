package com.sunesoft.seera.yc.core.uAuth.application.dtos;

import java.util.Date;

/**
 * Created by zhouz on 2016/7/9.
 */
public class UserSessionDto {
    private Long id;//用户id

    private int levels; // 等级

    private String loginName; // 登录用户帐号

    private String openId;

    private String name; // 真实姓名

    private String mobile; // 手机


    private String headimgurl;
    /**
     * 积分
     */
    private Integer integrals;
    /**
     * 年卡信息
     */
    private String yearCardInfo;

    /**
     * 年卡过期时间
     */
    private Date yearCardExpireDate;

    public String getYearCardInfo() {
        return yearCardInfo;
    }

    public void setYearCardInfo(String yearCardInfo) {
        this.yearCardInfo = yearCardInfo;
    }

    public Date getYearCardExpireDate() {
        return yearCardExpireDate;
    }

    public void setYearCardExpireDate(Date yearCardExpireDate) {
        this.yearCardExpireDate = yearCardExpireDate;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getLevels() {
        return levels;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getIntegrals() {
        return integrals;
    }

    public void setIntegrals(Integer integrals) {
        this.integrals = integrals;
    }
}
