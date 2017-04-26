package com.sunesoft.seera.yc.core.order.application.dtos;

import com.sunesoft.seera.yc.core.order.domain.OrderProductStatus;

import java.util.Date;

/**
 * Created by temp on 2016/10/19.
 */
public class OrderMealProductDto {


    /**
     * 订单编号
     * <p>需定义订单编号规则</p>
     */
    private String num;

    private String name;

    private String username;

    private OrderProductStatus orderStatus;

    private Long orderProductId;

    private Date timeOrder;

    public Date getOrderTime() {
        return timeOrder;
    }

    public void setOrderTime(Date orderTime) {
        this.timeOrder = orderTime;
    }

    public Long getOrderProductId() {
        return orderProductId;
    }

    public void setOrderProductId(Long orderProductId) {
        this.orderProductId = orderProductId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrderProductStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderProductStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
