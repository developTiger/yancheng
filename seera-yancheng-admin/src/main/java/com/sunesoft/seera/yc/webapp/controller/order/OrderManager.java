package com.sunesoft.seera.yc.webapp.controller.order;

import java.util.Date;

/**
 * Created by admin on 2016/7/21.
 */
public class OrderManager {

    private String orderNo;//订单编号
    private String userName;//用户名
    private String goodsName;//商品名称
    private Long number;//数量
    private Long money;//订单金额
    private String  isOK;//是否使用优惠券
    private String mobilePhone;//手机号
    private Date beginTime;//预约日期
    private Boolean isActive;//订单状态


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public String getIsOK() {
        return isOK;
    }

    public void setIsOK(String isOK) {
        this.isOK = isOK;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
