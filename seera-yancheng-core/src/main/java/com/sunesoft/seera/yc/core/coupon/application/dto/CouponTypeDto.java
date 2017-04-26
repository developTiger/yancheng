package com.sunesoft.seera.yc.core.coupon.application.dto;

import com.sunesoft.seera.yc.core.coupon.domain.CouponTypeStatus;

import java.util.Date;

/**
 * Created by xiazl on 2016/8/31.
 */
public class CouponTypeDto extends CouponTypeSimpleDto {

    private String couponTypeName;

    private Boolean isActive ;

    private Date createDateTime; // 创建时间


    private Date lastUpdateTime; // 最后修改时间


    /**
     * 设定该类优惠券的总数量
     * 如果为null,就是不限数量
     */
    private Long count;
    /**
     * 优惠券版期状态，
     */
    private CouponTypeStatus status;


    public String getCouponTypeName() {
        return couponTypeName;
    }

    public void setCouponTypeName(String couponTypeName) {
        this.couponTypeName = couponTypeName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
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

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public CouponTypeStatus getStatus() {
        return status;
    }

    public void setStatus(CouponTypeStatus status) {
        this.status = status;
    }
}
