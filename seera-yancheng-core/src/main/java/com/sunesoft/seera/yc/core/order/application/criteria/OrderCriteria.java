package com.sunesoft.seera.yc.core.order.application.criteria;

import com.sunesoft.seera.fr.results.PagedCriteria;
import com.sunesoft.seera.yc.core.order.domain.OrderProductStatus;
import com.sunesoft.seera.yc.core.order.domain.OrderStatus;

import java.util.Date;

/**
 *
 * Created by zhaowy on 2016/7/21.
 */
public class OrderCriteria extends PagedCriteria {
    /**
     * 限定区域
     */
    private String rejectArea;

    //region Field

    /**
     * 订单编号
     * <p>需定义订单编号规则</p>
     */
    private String num;

    /**
     * 下单人标识
     */
    private String touristId;

    /**
     * 下单人微信号
     */
    private String touristWxName;

    /**
     * 下单人用户名
     */
    private String touristUserName;

    /**
     * 下单人真实姓名
     */
    private String touristRealName;

    /**
     * 订单商品
     */
    private Long productId;

    /**
     * 订单状态
     */
    private OrderStatus status;

    /**
     * 起始下单时间
     */
    private Date fromOrderTime;

    /**
     * 截止下单时间
     */
    private Date endOrderTime;

    /**
     * 订单名称
     * @return
     */
    private String productName;



    private OrderProductStatus productStatus;

    public String getRejectArea() {
        return rejectArea;
    }

    public void setRejectArea(String rejectArea) {
        this.rejectArea = rejectArea;
    }

    public OrderProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(OrderProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    //endregion

    //region Property Get&Set

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTouristUserName() {
        return touristUserName;
    }

    public void setTouristUserName(String userName) {
        this.touristUserName = userName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getFromOrderTime() {
        return fromOrderTime;
    }

    public void setFromOrderTime(Date fromOrderTime) {
        this.fromOrderTime = fromOrderTime;
    }

    public Date getEndOrderTime() {
        return endOrderTime;
    }

    public void setEndOrderTime(Date endOrderTime) {
        this.endOrderTime = endOrderTime;
    }

    public String getTouristId() {
        return touristId;
    }

    public void setTouristId(String touristId) {
        this.touristId = touristId;
    }

    public String getTouristWxName() {
        return touristWxName;
    }

    public void setTouristWxName(String touristWxName) {
        this.touristWxName = touristWxName;
    }

    public String getTouristRealName() {
        return touristRealName;
    }

    public void setTouristRealName(String touristRealName) {
        this.touristRealName = touristRealName;
    }
}
