package com.sunesoft.seera.yc.core.coupon.domain.criteria;

import com.sunesoft.seera.fr.results.PagedCriteria;
import com.sunesoft.seera.yc.core.coupon.domain.CouponTypeStatus;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xiazl on 2016/8/31.
 */
public class CouponTypeCriteria extends PagedCriteria {

    private String couponTypeName;

    private Long couponTypeId;
    /**
     * 状态查询
     */
    private CouponTypeStatus status;

    /**
     * 最小值
     */
    private BigDecimal startQuota;
    /**
     * 最大值
     */
    private BigDecimal endQuota;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;

    public Long getCouponTypeId() {
        return couponTypeId;
    }

    public void setCouponTypeId(Long couponTypeId) {
        this.couponTypeId = couponTypeId;
    }

    public String getCouponTypeName() {
        return couponTypeName;
    }

    public void setCouponTypeName(String couponTypeName) {
        this.couponTypeName = couponTypeName;
    }

    public BigDecimal getEndQuota() {
        return endQuota;
    }

    public void setEndQuota(BigDecimal endQuota) {
        this.endQuota = endQuota;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getStartQuota() {
        return startQuota;
    }

    public void setStartQuota(BigDecimal startQuota) {
        this.startQuota = startQuota;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public CouponTypeStatus getStatus() {
        return status;
    }

    public void setStatus(CouponTypeStatus status) {
        this.status = status;
    }
}
