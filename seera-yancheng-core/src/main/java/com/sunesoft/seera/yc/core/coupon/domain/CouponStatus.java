package com.sunesoft.seera.yc.core.coupon.domain;

/**
 * 优惠券状态枚举
 * Created by zhaowy on 2016/7/12.
 */
public enum CouponStatus {

    /**
     * 已过期
     */
    Expired("已过期",-1),

    /**
     * 已使用
     */
    Used("已使用",0),

    /**
     * 有效
     */
    Valid("有效",1),

    /**
     * 绑定
     */
    bind("绑定",2);

    /**
     * 状态
     */
    private String status;

    /**
     * 值
     */
    private Integer value;

    /**
     * 内部构造函数
     * @param status
     * @param value
     */
    private CouponStatus(String status,Integer value)
    {
        this.status= status;
        this.value= value;
    }

    @Override
    public String toString()
    {
       return String.valueOf(this.status);
    }

    public Integer toValue()
    {
        return this.value;
    }
}
