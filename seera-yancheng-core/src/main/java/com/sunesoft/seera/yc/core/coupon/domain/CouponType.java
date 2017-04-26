package com.sunesoft.seera.yc.core.coupon.domain;
import com.sunesoft.seera.fr.ddd.BaseEntity;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 优惠券的版期
 * Created by xiazl on 2016/8/31.
 */
@Entity(name = "coupon_type")
public class CouponType extends BaseEntity {

    /**
     * 版期编号
     * 查看是否重复的
     */
    private String couponTypeName;
    /**
     * 该优惠券的领取的开始日期
     */
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    /**
     * 该优惠券的领取的截止日期
     */
    @Column(name = "over_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date overTime;



    /**
     * 该优惠券的开始日期
     */
    @Column(name = "coupon_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date couponStart;
    /**
     * 该优惠券的截止日期
     */
    @Column(name = "coupon_over")
    @Temporal(TemporalType.TIMESTAMP)
    private Date couponOver;


    /**
     * 满足条件
     */
    private BigDecimal useCondition;

    /**
     * 优惠券金额
     */
    private BigDecimal quota;
    /**
     * 设定该类优惠券的总数量
     * 如果为null,就是不限数量
     */
    private Long count;

    /**
     * 优惠券版期状态，是否被禁止 false 禁止，true未禁止
     */
    @Column(name = "cd_status")
    private CouponTypeStatus status= CouponTypeStatus.Run;


    public CouponType() {
        this.setLastUpdateTime(new Date());
        this.setCreateDateTime(new Date());
    }


    public Long getCount() {
        return count;
    }


    public String getCouponTypeName() {
        return couponTypeName;
    }

    public void setCouponTypeName(String couponTypeName) {
        this.couponTypeName = couponTypeName;
    }

    public void setCount(Long count) {
        //数量领完，活动结束
        if(count!=null&&count<=0)setStatus(CouponTypeStatus.End);
        this.count = count;
    }

    public Date getOverTime() {
        return overTime;
    }

    public void setOverTime(Date overTime) {
        if(overTime.before(new Date()))this.status= CouponTypeStatus.End;
        this.overTime=overTime;
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

    public BigDecimal getUseCondition() {
        return useCondition;
    }

    public void setUseCondition(BigDecimal useCondition) {
        if(useCondition!=null&&useCondition.compareTo(BigDecimal.ZERO)<0)this.useCondition=BigDecimal.ZERO;
        else
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

    public BigDecimal getQuota() {
        return quota;
    }

    public void setQuota(BigDecimal quota) {
        if(quota!=null&&quota.compareTo(BigDecimal.ZERO)<0)this.quota=BigDecimal.ZERO;
        else
            this.quota = quota;
    }
}
