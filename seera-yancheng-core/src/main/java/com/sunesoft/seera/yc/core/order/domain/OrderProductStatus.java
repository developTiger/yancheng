package com.sunesoft.seera.yc.core.order.domain;

/**
 * 订单状态枚举
 * Created by zhaowy on 2016/7/11.
 */
public enum OrderProductStatus {


    /**
     *正常
     */
    normal,

    /**
     *改签审核
     */
    mealCheck,

    /**
     * 拒绝改签
     */
    mealReject,
    /**
     *已改签审核通过
     */
    mealed,

    /**
     *已退单
     */
    returned,

    /**
     * 过期未消费
     */
    expire,

//    /**
//     * 等待完成待评价
//     */
//    waitComment,

    /**
     *已完成
     */
    end,


    WaitMealReturn

}
