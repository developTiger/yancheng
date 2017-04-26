package com.sunesoft.seera.yc.core.coupon.application.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xiazl on 2016/8/31.
 */
public class CouponTypeSimpleDto {

    private Long id;

    /**
     * 该优惠券的领取的开始日期
     */
    private Date startTime;
    /**
     * 该优惠券的领取的截止日期
     */

    private Date overTime;



    /**
     * 该优惠券的开始日期
     */
    private Date couponStart;
    /**
     * 该优惠券的截止日期
     */
    private Date couponOver;


    /**
     * 满足条件
     */
    private BigDecimal useCondition;

    /**
     * 优惠券金额
     */
    private BigDecimal quota;

    public BigDecimal getUseCondition() {
        return useCondition;
    }

    public void setUseCondition(BigDecimal useCondition) {
        this.useCondition = useCondition;
    }

    public Date getCouponOver() {
        return couponOver;
    }

    public void setCouponOver(Date couponOver) {
        this.couponOver = couponOver;
    }

    public Date getCouponStart() {
        return couponStart;
    }

    public void setCouponStart(Date couponStart) {
        this.couponStart = couponStart;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getOverTime() {
        return overTime;
    }

    public void setOverTime(Date overTime) {
        this.overTime = overTime;
    }

    public BigDecimal getQuota() {
        return quota;
    }

    public void setQuota(BigDecimal quota) {
        this.quota = quota;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
