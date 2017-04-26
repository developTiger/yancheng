package com.sunesoft.seera.yc.core.coupon.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;
import com.sunesoft.seera.fr.utils.NumGenerator;
import com.sunesoft.seera.yc.core.manager.domain.InnerManager;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 优惠券实体聚合根
 * Created by zhaowy on 2016/7/11.
 */
@Entity
public class Coupon extends BaseEntity {

    //region Construct

    public Coupon() {
        this.setQuota(BigDecimal.ZERO);
        this.setIsActive(true);
        this.setCreateDateTime(new Date());
        this.setLastUpdateTime(new Date());
        this.num = NumGenerator.generate("yh");
    }

    //endregion

    private String touristName;

    //region Field

    /**
     * 券号 只读属性，自动生成
     */
    @Column(unique = true, nullable = false)
    private String num;

    /**
     * 优惠券满多少元可以使用
     */
    private BigDecimal useCondition;

    /**
     * 优惠额度
     */
    @Column
    private BigDecimal quota;


    /**
     * 优惠券状态
     */
    @Column(name = "coupon_status")
    private CouponStatus couponStatus;

    /**
     * 过期时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expire_date")
    private Date expireDate;

    /**
     * 关联员工
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "staff_id")
    private InnerManager refStaff;

    //endregion

    //region Property Get&Set
    @Column(name = "CouponType_id")
    private Long CouponTypeId;


    public String getTouristName() {
        return touristName;
    }

    public void setTouristName(String touristName) {
        this.touristName = touristName;
    }

    public void setNum(String num) {
        this.num = num;
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

    public BigDecimal getQuota() {
        return quota;
    }

    public void setQuota(BigDecimal quota) {
        if (quota.compareTo(BigDecimal.ZERO) > 0)
            this.quota = quota;
        else this.quota = BigDecimal.ZERO;
    }

    public String getNum() {
        return num;
    }

    public CouponStatus getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(CouponStatus couponStatus) {
        this.couponStatus = couponStatus;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
//        if (expireDate.before(new Date())) {
//            setCouponStatus(CouponStatus.Expired);
//        }
    }

    public InnerManager getRefStaff() {
        return refStaff;
    }

    public void setRefStaff(InnerManager refStaff) {
        this.refStaff = refStaff;
    }

    //endregion

    //region Method

    /**
     * 设置优惠券已使用
     */
    public void SetCouponUsed() {
        this.setCouponStatus(CouponStatus.Used);
    }
    //endregion
}


