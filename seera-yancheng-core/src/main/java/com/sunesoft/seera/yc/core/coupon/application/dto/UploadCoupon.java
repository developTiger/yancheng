package com.sunesoft.seera.yc.core.coupon.application.dto;

import java.util.Date;

/**
 * Created by zwork on 2016/11/14.
 */
public class UploadCoupon {

    private String tuName;

    private String phoneNo;


    private Long couponCode;

    private Integer couponCount;


    private Date  endDate;


    public String getTuName() {
        return tuName;
    }

    public void setTuName(String tuName) {
        this.tuName = tuName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }


    public Long getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(Long couponCode) {
        this.couponCode = couponCode;
    }

    public Integer getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(Integer couponCount) {
        this.couponCount = couponCount;
    }



    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
