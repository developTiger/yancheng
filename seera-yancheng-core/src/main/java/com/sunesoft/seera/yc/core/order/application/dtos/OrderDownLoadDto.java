package com.sunesoft.seera.yc.core.order.application.dtos;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zwork on 2016/11/4.
 */
public class OrderDownLoadDto {

    /**
     * 订单编号
     * <p>需定义订单编号规则</p>
     */
    private String num;

    /**
     * 下单人
     */
    private String userName;

    /**
     * 订单商品
     */
    private String productNames;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 总价
     */
    private BigDecimal orderPrice;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 订单支付方式
     */
    private  String payTypes;

    /**
     * 使用优惠券
     */
    private String couponInfo;

    /**
     * 取票人
     */
    private String  fetcher;

    /**
     * 取票人
     */
    private String  phone;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductNames() {
        return productNames;
    }

    public void setProductNames(String productNames) {
        this.productNames = productNames;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getCouponInfo() {
        return couponInfo;
    }

    public void setCouponInfo(String couponInfo) {
        this.couponInfo = couponInfo;
    }

    public String getFetcher() {
        return fetcher;
    }

    public void setFetcher(String fetcher) {
        this.fetcher = fetcher;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getPayTypes() {
        return payTypes;
    }

    public void setPayTypes(String payTypes) {
        this.payTypes = payTypes;
    }
}
