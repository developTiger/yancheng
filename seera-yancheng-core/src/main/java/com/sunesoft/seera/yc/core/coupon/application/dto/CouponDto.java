package com.sunesoft.seera.yc.core.coupon.application.dto;

import com.sunesoft.seera.yc.core.coupon.domain.CouponStatus;

import java.math.BigDecimal;
import java.util.Date;


/**
 * Created by zhaowy on 2016/7/14.
 */
public class CouponDto {


    private String touristName;

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

    /**
     * 关联员工Id
     */
    private Long staffId;

    /**
     * 优惠券的id
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createDateTime; // 创建时间
    /**
     * 最后修改时间
     */
    private Date lastUpdateTime; // 最后修改时间

    private Long CouponTypeId;

    public String getTouristName() {
        return touristName;
    }

    public void setTouristName(String touristName) {
        this.touristName = touristName;
    }

    public Long getCouponTypeId() {
        return CouponTypeId;
    }

    public void setCouponTypeId(Long couponTypeId) {
        CouponTypeId = couponTypeId;
    }

    public BigDecimal getUseCondition() {
        return useCondition;
    }

    public void setUseCondition(BigDecimal useCondition) {
        this.useCondition = useCondition;
    }

    public CouponStatus getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(CouponStatus couponStatus) {
        this.couponStatus = couponStatus;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
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

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getGqDate() {
        return gqDate;
    }

    public void setGqDate(String gqDate) {
        this.gqDate = gqDate;
    }

}
