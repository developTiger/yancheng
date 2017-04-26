package com.sunesoft.seera.yc.core.tourist.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;

import javax.persistence.Entity;

/**
 * 取件人信息
 * Created by zhaowy on 2016/7/21.
 */
@Entity
public class Fetcher extends BaseEntity {

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 身份证号
     */
    private String idCardNo;

    /**
     * 是默认取件人
     */
    private Boolean isDefault;


    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public Boolean getIsDefault() {
        return isDefault==null?false:isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
}
