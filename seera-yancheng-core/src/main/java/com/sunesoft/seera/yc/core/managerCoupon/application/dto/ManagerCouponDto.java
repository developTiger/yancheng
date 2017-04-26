package com.sunesoft.seera.yc.core.managerCoupon.application.dto;

import java.math.BigDecimal;

/**
 * Created by xiazl on 2016/9/8.
 */
public class ManagerCouponDto {
    /**
     * 优惠券的面额
     */
    private BigDecimal quota;
    /**
     * 优惠券的使用条件
     */
    private BigDecimal useCondition;
    /**
     * 优惠券的数量
     */
    private Integer count;
    /**
     * 优惠券的截止日期
     */
    private String postTime;
    /**
     * 员工id
     */
    private Long staffId;
    /**
     * 员工用户名
     */
    private String userName;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public BigDecimal getQuota() {
        return quota;
    }

    public void setQuota(BigDecimal quota) {
        this.quota = quota;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public BigDecimal getUseCondition() {
        return useCondition;
    }

    public void setUseCondition(BigDecimal useCondition) {
        this.useCondition = useCondition;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
