package com.sunesoft.seera.yc.webapp.controller.activity;

import java.util.Date;

/**
 * Created by admin on 2016/7/21.
 */
public class TitleEvent {

    private String orderNo;//订单编号
    private String type;//类目
    private String eventName;//活动名称
    private Long price;//促销价
    private String person;//策划人
    private Date eventTime;//活动时间
    private Boolean isActive;//活动状态

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
