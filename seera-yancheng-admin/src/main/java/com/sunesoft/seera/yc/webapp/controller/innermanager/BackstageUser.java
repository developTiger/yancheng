package com.sunesoft.seera.yc.webapp.controller.innermanager;

/**
 * Created by admin on 2016/7/20.
 */
public class BackstageUser {


    private String userName;//用户名

    private String mobilePhone;//手机号

    private Boolean isActive;//状态，是否禁用
    private String userType;//用户类型
    private String businessName;//商家名称

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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
}
