package com.sunesoft.seera.yc.webapp.controller.order;

import java.util.Date;

/**
 * Created by admin on 2016/7/22.
 */
public class OrderList {

    private String ordreNo;//订单号
    private String status;//状态
    private String userName;//取票人姓名
    private String phone;//联系方式
    private String email;//邮箱
    private Date beginData;//原预约日期
    private Date endData;//改签日期
    private String reason;//改签理由

    private String realName;//真实姓名
    private String  sEmail;//邮箱
    private String city;//城市


    private String orderNumber;//订单编号
    private Date payTime;//付款时间
    private Date dealTime;//成交时间

    private String productContent;//产品内容
    private String type;//类型
    private String price;//单价
    private String num;//数量
    private String discount;//优惠
    private String totalPrice;//总价

    public String getOrdreNo() {
        return ordreNo;
    }

    public void setOrdreNo(String ordreNo) {
        this.ordreNo = ordreNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBeginData() {
        return beginData;
    }

    public void setBeginData(Date beginData) {
        this.beginData = beginData;
    }

    public Date getEndData() {
        return endData;
    }

    public void setEndData(Date endData) {
        this.endData = endData;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getsEmail() {
        return sEmail;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public String getProductContent() {
        return productContent;
    }

    public void setProductContent(String productContent) {
        this.productContent = productContent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
