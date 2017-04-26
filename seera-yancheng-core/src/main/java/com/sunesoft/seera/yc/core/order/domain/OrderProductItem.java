package com.sunesoft.seera.yc.core.order.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;
import com.sunesoft.seera.yc.core.product.domain.ProductItemType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单中商品项实体
 * Created by zhaowy on 2016/7/20.
 */
@Entity
public class OrderProductItem extends BaseEntity {

    public OrderProductItem(){
        this.setIsActive(true);
        this.setItemStatus(ItemStatus.notCreate);
        this.setCreateDateTime(new Date());
        this.setLastUpdateTime(new Date());
    }
    /**
     * 所属商品
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private OrderProduct product;

    /**
     * 商品项编号
     */
    private String num;

    /**
     * 原始 商品项 标识
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
     * 预约时间
     */
    private Date scheduleDate;

    /**
     * 商品项二维码
     *<p>商品项标识二维码由订单号 + 商品项编号 + 商品项数量唯一构成的URL，扫描可触发收货事件</p>
     * <p> 存储格式如下：
     * {
     "item":
     {
     "name":"xxx",//商品项名称
     "count":1,//商品项数量
     "code":"01011001" //商品项二维码
     }
     }</p>
     */
    @Column(columnDefinition = "blob")
    private String qrCode;

    /**
     * 订单商品项提取码
     *   orderNum_orderProductItemId
     */
    private String takeNum;


    /**
     * 是否已收货
     * <P>如果已收货，则设置为true，否则设置为false</P>
     */
    private Boolean haveTaked= false;

    /**
     * 商品项领取时间
     */
    @Column(name = "take_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takeDate;


    /**
     * 扫描员标识
     * 用于商品扫码校验
     */
    private Long innerManageId;


    private ItemStatus ItemStatus;


    private String ItemStatusRemark;

    public Boolean ticketCanCancel(){
        List<ItemStatus> itemStatuses = new ArrayList<>();
        itemStatuses.add(ItemStatus.created);
        itemStatuses.add(ItemStatus.reCreated);
        itemStatuses.add(ItemStatus.cancelFailed);
        itemStatuses.add(ItemStatus.imgError);
        itemStatuses.add(ItemStatus.allSuccess);
        return itemStatuses.contains(this.ItemStatus);
    }
    public Boolean ticketCanCreate(){
        List<ItemStatus> itemStatuses = new ArrayList<>();
        itemStatuses.add(ItemStatus.createError);
        itemStatuses.add(ItemStatus.reCreateError);
        itemStatuses.add(ItemStatus.notCreate);
        return itemStatuses.contains(this.ItemStatus);
    }

    public Boolean isComplete(){


        List<ItemStatus> itemStatuses = new ArrayList<>();
        itemStatuses.add(ItemStatus.checked);
        itemStatuses.add(ItemStatus.canceled);

        return itemStatuses.contains(this.ItemStatus);
    }

    public void updateItemStatus(ItemStatus status,String remark){
        this.setItemStatus(status);
        this.setItemStatusRemark(remark);
        this.setLastUpdateTime(new Date());
    }
    //endregion

    //region Property Get&Set

    public BigDecimal getItemTotalPrice(){
        return price.multiply(BigDecimal.valueOf(count));
    }


    public Date getTakeDate() {
        return takeDate;
    }

    public void setTakeDate(Date takeDate) {
        this.takeDate = takeDate;
    }

    public OrderProduct getProduct() {
        return product;
    }

    public void setProduct(OrderProduct product) {
        this.product = product;
    }

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

    public void setTakeNum(String takeNum) {
        this.takeNum = takeNum;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
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

    //endregion
}
