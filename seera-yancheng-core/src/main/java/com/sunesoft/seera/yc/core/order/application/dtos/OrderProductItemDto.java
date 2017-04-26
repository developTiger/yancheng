package com.sunesoft.seera.yc.core.order.application.dtos;

import com.sunesoft.seera.yc.core.order.domain.ItemStatus;
import com.sunesoft.seera.yc.core.product.domain.ProductItemType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhaowy on 2016/7/20.
 */
public class OrderProductItemDto {

    /**
     * 所属订单商品标识
     */
    private Long id;

    /**
     * 订单商品标识
     */
    private Long orderProductId;

//    /**
//     * 订单标识
//     */
//    private Long orderId;

    /**
     * 商品项编号
     */
    private String num;

    /**
     * 原始商品项标识
     */
    private Long originalId;

    /**
     * 商品项名称
     */
    private String name;

    /**
     * 商品项类型
     */
    private ProductItemType type;

    /**
     * 商品项价格。
     */
    private BigDecimal price;

    /**
     * 商品项数量
     * <p>订单商品项数量 N = 订单商品数量 count * 商品包含商品项数量
     * eg:商品A包含2个商品项B，一个订单下了2件，则生成的商品项数量应为2*2 =4
     * </p>
     */
    private Integer count;

    /**
     * 商品项二维码
     * <p>商品项标识二维码由订单号 + 商品项编号 + 商品项数量唯一构成的URL，扫描可触发收货事件</p>
     * <p> 存储格式如下：
     * {
     "item":
     {
     "name":"xxx",//商品项名称
     "count":1,//商品项数量
     "code":"sdfadsfasdfa" //商品项二维码
     }
     }</p>
     */
    private String qrCode;

    /**
     * 订单商品项提取码
     */
    private String takeNum;

    /**
     * 是否已收货
     * <P>如果已收货，则设置为true，否则设置为false</P>
     */
    private Boolean haveTaked;

    /**
     * 商品项领取时间
     */
    private Date takeDate;

    /**
     * 扫描员标识
     * 用于商品扫码校验
     */
    private Long innerManageId;

    /**
     * 预约时间
     */
    private Date scheduleDate;


    private com.sunesoft.seera.yc.core.order.domain.ItemStatus ItemStatus;


    private String ItemStatusRemark;


    private Date lastUpdateTime;

    /**
     * 订单编号
     * @return
     */
    private String orderNum;

    private Long orderId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderProductId() {
        return orderProductId;
    }

    public void setOrderProductId(Long orderProductId) {
        this.orderProductId = orderProductId;
    }
//
//    public Long getOrderId() {
//        return orderId;
//    }
//
//    public void setOrderId(Long orderId) {
//        this.orderId = orderId;
//    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Long getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Long originalId) {
        this.originalId = originalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Boolean getHaveTaked() {
        return haveTaked;
    }

    public void setHaveTaked(Boolean haveTaked) {
        this.haveTaked = haveTaked;
    }

    public Date getTakeDate() {
        return takeDate;
    }

    public void setTakeDate(Date takeDate) {
        this.takeDate = takeDate;
    }

    public ProductItemType getType() {
        return type;
    }

    public void setType(ProductItemType type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getInnerManageId() {
        return innerManageId;
    }

    public void setInnerManageId(Long innerManageId) {
        this.innerManageId = innerManageId;
    }

    public String getTakeNum() {
        return takeNum;
    }


    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public void setTakeNum(String takeNum) {
        this.takeNum = takeNum;
    }

    public ItemStatus getItemStatus() {
        return ItemStatus;
    }

    public void setItemStatus(ItemStatus itemStatus) {
        ItemStatus = itemStatus;
    }

    public String getItemStatusRemark() {
        return ItemStatusRemark;
    }

    public void setItemStatusRemark(String itemStatusRemark) {
        ItemStatusRemark = itemStatusRemark;
    }
}
