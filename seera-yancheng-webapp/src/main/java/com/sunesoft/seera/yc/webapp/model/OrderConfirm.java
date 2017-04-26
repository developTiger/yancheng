package com.sunesoft.seera.yc.webapp.model;

import com.sunesoft.seera.yc.core.order.application.criteria.CreateOrderProduct;

import java.util.List;
import java.util.Map;

/**
 * Created by bing on 16/8/20.
 */
public class OrderConfirm {

    List<OrderProductModel> orderProducts;

    private Long fetcherId;

    private Long couponId;

    private String couponNum;

    public List<OrderProductModel> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProductModel> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public Long getFetcherId() {
        return fetcherId;
    }

    public void setFetcherId(Long fetcherId) {
        this.fetcherId = fetcherId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public String getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(String coupon) {
        this.couponNum = coupon;
    }
}
