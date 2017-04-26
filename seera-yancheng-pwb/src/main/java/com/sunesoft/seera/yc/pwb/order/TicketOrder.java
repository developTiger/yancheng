package com.sunesoft.seera.yc.pwb.order;

import java.io.Serializable;

/**
 * 子订单对象.
 * <p>
 * Created by bing on 16/7/27.
 */
public final class TicketOrder implements Serializable {

    /**
     * 子订单编码.
     */
    private String orderCode;

    /**
     * 票价，必填.
     */
    private String price;

    /**
     * 票数量.
     */
    private int quantity;

    /**
     * 子订单总价.
     */
    private String totalPrice;

    /**
     * 日期.
     */
    private String occDate;

    /**
     * 商品编码，同票型编码.
     */
    private String goodsCode;

    /**
     * 商品名称.
     */
    private String goodsName;

    /**
     * 备注.
     */
    private String remark;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOccDate() {
        return occDate;
    }

    public void setOccDate(String occDate) {
        this.occDate = occDate;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
