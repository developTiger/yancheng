package com.sunesoft.seera.yc.webapp.controller.tourist;

import java.util.Date;

/**
 * Created by admin on 2016/7/16.
 */
public class Tourist {

    private String userName;//用户名
    private String idCardNo;//身份证
    private String email;//注册邮箱
    private String mobilePhone;//手机号
    private String yearCardInfo;//年卡信息
    private Date createDateTime;//注册时间
    private Boolean isActive;//状态，是否禁用
    // region 微信账号信息
    private String openid;//用户的唯一标识
    private String province;//用户个人资料填写的省份
    private String city;//普通用户个人资料填写的城市
    private String country;//国家，如中国为CN
    private String headimgurl;//用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
    private String privilege;//用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
    private String unionid;//只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
    //endregion
    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getYearCardInfo() {
        return yearCardInfo;
    }

    public void setYearCardInfo(String yearCardInfo) {
        this.yearCardInfo = yearCardInfo;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
