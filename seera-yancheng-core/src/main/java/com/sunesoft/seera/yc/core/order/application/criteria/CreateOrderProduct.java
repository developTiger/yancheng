package com.sunesoft.seera.yc.core.order.application.criteria;

import java.util.Date;

/**
 * 创建订单商品
 * Created by zhaowy on 2016/9/12.
 */
public class CreateOrderProduct {

    //region Filed

    /**
     * 商品标识
     */
    private Long productId;

    /**
     * 商品数量
     */
    private Integer count;

    /**
     * 预定游玩日期
     */
    private Date tourScheduleDate;

    /**
     * 预定入住日期
     */
    private Date hotelScheduleDate;

    //endregion

    //region Property Get&Set

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getTourScheduleDate() {
        return tourScheduleDate;
    }

    public void setTourScheduleDate(Date tourScheduleDate) {
        this.tourScheduleDate = tourScheduleDate;
    }

    public Date getHotelScheduleDate() {
        return hotelScheduleDate;
    }

    public void setHotelScheduleDate(Date hotelScheduleDate) {
        this.hotelScheduleDate = hotelScheduleDate;
    }

    //endregion
}
