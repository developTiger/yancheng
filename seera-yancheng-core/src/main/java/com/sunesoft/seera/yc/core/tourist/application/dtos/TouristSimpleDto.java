package com.sunesoft.seera.yc.core.tourist.application.dtos;

import com.sunesoft.seera.yc.core.tourist.domain.TouristStatus;

import java.util.Date;
import java.util.List;

/**
 * Created by zhaowy on 2016/7/12.
 */
public class TouristSimpleDto {

    // region Filed

    private Long id;

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
     * 微信号
     */
    private String wxName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 积分
     */
    private Integer integrals;

    /**
     * 创建时间
     */
    private Date createDateTime;


    private Boolean isActive;
    /**
     * 是否禁用
     */
    private TouristStatus status;

    private String headimgurl;
    /**
     * 年卡信息
     */
    private String yearCardInfo;

    /**
     * 年卡过期时间
     */
    private Date yearCardExpireDate;


    //endregion

    //region Property Get&Set

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getIntegrals() {
        return integrals;
    }

    public void setIntegrals(Integer integrals) {
        this.integrals = integrals;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public TouristStatus getStatus() {
        return status;
    }

    public void setStatus(TouristStatus status) {
        this.status = status;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

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


    //endregion
}
