package com.sunesoft.seera.yc.core.order.application.criteria;

import com.sunesoft.seera.fr.results.PagedCriteria;
import com.sunesoft.seera.yc.core.order.domain.OrderProductStatus;
import com.sunesoft.seera.yc.core.order.domain.OrderStatus;

import java.util.Date;

/**
 *
 * Created by zhaowy on 2016/7/21.
 */
public class OrderProductItemCriteria extends PagedCriteria {

    //region Field

    /**
     * 商品项编号
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
     * 商品项编号
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
     * 商品项名称
     * @return
     */
    private String productItemName;

    private String orderNum;

    private String product_status;


    private OrderProductStatus productStatus;

    public String getProduct_status() {
        return product_status;
    }

    public void setProduct_status(String product_status) {
        this.product_status = product_status;
    }

    public OrderProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(OrderProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public String getProductItemName() {
        return productItemName;
    }

    public void setProductItemName(String productItemName) {
        this.productItemName = productItemName;
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

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }


}
