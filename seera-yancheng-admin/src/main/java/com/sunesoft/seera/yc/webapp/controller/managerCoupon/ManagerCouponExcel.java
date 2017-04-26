package com.sunesoft.seera.yc.webapp.controller.managerCoupon;

import com.sunesoft.seera.yc.core.coupon.domain.CouponStatus;

import java.math.BigDecimal;

/**
 * Created by xiazl on 2016/9/10.
 */
public class ManagerCouponExcel {

    private String num;

    /**
     * 优惠额度
     */

    private BigDecimal quota;


    /**
     * 使用条件
     */
    private BigDecimal useCondition;



    /**
     * 优惠券状态
     */

    private CouponStatus couponStatus;

    /**
     * 过期时间
     */

    private String gqDate;



    /**
     * 关联员工name
     */
    private String realName;

    public CouponStatus getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(CouponStatus couponStatus) {
        this.couponStatus = couponStatus;
    }

    public String getGqDate() {
        return gqDate;
    }

    public void setGqDate(String gqDate) {
        this.gqDate = gqDate;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public BigDecimal getQuota() {
        return quota;
    }

    public void setQuota(BigDecimal quota) {
        this.quota = quota;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public BigDecimal getUseCondition() {
        return useCondition;
    }

    public void setUseCondition(BigDecimal useCondition) {
        this.useCondition = useCondition;
    }
}
