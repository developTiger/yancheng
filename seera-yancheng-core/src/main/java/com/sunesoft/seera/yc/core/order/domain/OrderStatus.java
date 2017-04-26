package com.sunesoft.seera.yc.core.order.domain;

/**
 * 订单状态枚举
 * Created by zhaowy on 2016/7/11.
 */
public enum OrderStatus {

    /**
     * 待付款
     */
    waitPay,

    /**
     * 付款待确认
     */
    payCheck,

    /**
     * 已付款
     */
    payed,

    /**
     * 已超时
     */
    expired,

    /**
     * 待评价
     */
    waitComment,

    /**
     * 已取消
     */
    canceled,

    /**
     * 已完成已评价
     */
    end


}
