package com.sunesoft.seera.yc.core.coupon.domain.criteria;

import com.sunesoft.seera.fr.results.PagedCriteria;
import com.sunesoft.seera.yc.core.coupon.domain.CouponStatus;

import java.math.BigDecimal;


/**
 * Created by xiazl on 2016/7/16.
 */
public class CouponCriteria extends PagedCriteria {

    private String touristName;


    private Long couponTypeId;
    /**
     * 券号 只读属性，自动生成
     */
    private String num;

    /**
     * 关联员工id
     */
    private Long staffId;

    /**
     * 关联员工
     */
    private String staffRealName;


    /**
     * 优惠券状态
     */
    private CouponStatus couponStatus;

    /**
     * 优惠券满下限元使用
     */
    private BigDecimal startUseCondition;


    /**
     * 优惠券满上线元使用
     */
    private BigDecimal endUseCondition;
    /**
     * 未过期的
     */
    private String endExpireDate;

    /**
     * 未过期的
     */
    private String StartExpireDate;


    public String getTouristName() {
        return touristName;
    }

    public void setTouristName(String touristName) {
        this.touristName = touristName;
    }

    public Long getStaffId() {
        return staffId;
    }

    public Long getCouponTypeId() {
        return couponTypeId;
    }

    public void setCouponTypeId(Long couponTypeId) {
        this.couponTypeId = couponTypeId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public CouponStatus getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(CouponStatus couponStatus) {
        this.couponStatus = couponStatus;
    }

    public String getEndExpireDate() {
        return endExpireDate;
    }

    public void setEndExpireDate(String endExpireDate) {
        this.endExpireDate = endExpireDate;
    }

    public BigDecimal getEndUseCondition() {
        return endUseCondition;
    }

    public void setEndUseCondition(BigDecimal endUseCondition) {
        this.endUseCondition = endUseCondition;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getStaffRealName() {
        return staffRealName;
    }

    public void setStaffRealName(String staffRealName) {
        this.staffRealName = staffRealName;
    }

    public String getStartExpireDate() {
        return StartExpireDate;
    }

    public void setStartExpireDate(String startExpireDate) {
        StartExpireDate = startExpireDate;
    }

    public BigDecimal getStartUseCondition() {
        return startUseCondition;
    }

    public void setStartUseCondition(BigDecimal startUseCondition) {
        this.startUseCondition = startUseCondition;
    }
}
