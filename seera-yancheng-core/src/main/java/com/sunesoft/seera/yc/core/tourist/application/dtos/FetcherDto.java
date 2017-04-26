package com.sunesoft.seera.yc.core.tourist.application.dtos;

/**
 * Created by zhaowy on 2016/7/21.
 */
public class FetcherDto {

    /**
     * 标识
     */
    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
}
