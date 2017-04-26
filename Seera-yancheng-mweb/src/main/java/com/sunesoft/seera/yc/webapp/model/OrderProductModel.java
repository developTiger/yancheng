package com.sunesoft.seera.yc.webapp.model;

/**
 * Created by zhouz on 2016/9/13.
 */
public class OrderProductModel {
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
    private String tourScheduleDate;

    /**
     * 预定入住日期
     */
    private String hotelScheduleDate;

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

    public String getTourScheduleDate() {
        return tourScheduleDate;
    }

    public void setTourScheduleDate(String tourScheduleDate) {
        this.tourScheduleDate = tourScheduleDate;
    }

    public String getHotelScheduleDate() {
        return hotelScheduleDate;
    }

    public void setHotelScheduleDate(String hotelScheduleDate) {
        this.hotelScheduleDate = hotelScheduleDate;
    }

}
