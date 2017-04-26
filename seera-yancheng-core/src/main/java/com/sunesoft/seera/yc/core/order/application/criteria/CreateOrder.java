package com.sunesoft.seera.yc.core.order.application.criteria;

import java.util.List;

/**
 * 下单的时候，前台需要提供的数据
 * Created by xiazl on 2016/7/26.
 */
public class CreateOrder {

    public CreateOrder(){
        checkFetcher=true;
    }

    private List<CreateOrderProduct> orderProducts;

    /**
     * 订单的游客
     */
    private Long touristId;

    /**
     * 取件人信息
     */
    private Long fetcherDtoId;

    /**
     * 优惠券
     */
    private Long couponId;


    private Boolean checkFetcher;

    /**
     * 订单门票备注
     */
    private String remark;

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Long getFetcherDtoId() {
        return fetcherDtoId;
    }

    public void setFetcherDtoId(Long fetcherDtoId) {
        this.fetcherDtoId = fetcherDtoId;
    }


    public Long getTouristId() {
        return touristId;
    }

    public void setTouristId(Long touristId) {
        this.touristId = touristId;
    }

    public List<CreateOrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<CreateOrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public Boolean getCheckFetcher() {
        return checkFetcher;
    }

    public void setCheckFetcher(Boolean checkFetcher) {
        this.checkFetcher = checkFetcher;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
