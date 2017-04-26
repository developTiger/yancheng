package com.sunesoft.seera.yc.core.order.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;
import com.sunesoft.seera.fr.utils.NumGenerator;
import com.sunesoft.seera.yc.core.coupon.domain.Coupon;
import com.sunesoft.seera.yc.core.tourist.domain.Fetcher;
import com.sunesoft.seera.yc.core.tourist.domain.Tourist;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单实体聚合根
 * Created by zhaowy on 2016/7/11.
 */
@Entity
public class OrderInfo extends BaseEntity {

    //region Construct

    public OrderInfo() {
        this.orderTime = new Date();
       // this.num = NumGenerator.generate("yc");
        this.orderOperatorStatus = OrderOperatorStatus.Wait;
        this.setIsActive(true);
        this.setCreateDateTime(new Date());
        this.setLastUpdateTime(new Date());
    }

    /**
     * 构造订单
     *
     * @param products 订单商品
     * @param tourist  游客
     */
    public OrderInfo(List<OrderProduct> products, Tourist tourist) {
        this.orderTime = new Date();
       // this.num = NumGenerator.generate("yc");
        this.products = products;
        this.tourist = tourist;

        this.orderOperatorStatus = OrderOperatorStatus.Wait;
        this.setIsActive(true);
        this.setCreateDateTime(new Date());
        this.setLastUpdateTime(new Date());
    }

    //endregion

    // region Field

    /**
     * 订单编号
     * <p>需定义订单编号规则</p>
     */
    private String num;

    /**
     * 下单人
     */
    @ManyToOne
    @JoinColumn(name = "tourist_id")
    private Tourist tourist;

    /**
     * 订单商品
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderProduct> products;
//
//    /**
//     * 订单商品项明细
//     */
//    @ManyToMany
//    @JoinTable(name = "order_to_productItem", inverseJoinColumns = @JoinColumn(name = "productItem_id"),
//            joinColumns = @JoinColumn(name = "order_id"))
//    private List<OrderProductItem> productItems;

    /**
     * 使用优惠券
     */
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    /**
     * 使用取票人
     */
    @ManyToOne
    @JoinColumn(name = "fetcher_id")
    private Fetcher fetcher;


    /**
     * 订单商品总价
     * <P>订单商品总价包括所有商品价格总和 - 优惠券价格</P>
     */
    private BigDecimal orderPrice = BigDecimal.ZERO;

    /**
     * 订单状态
     */
    private OrderStatus status = OrderStatus.waitPay;

    /**
     * 订单支付方式
     */
    private String payTypes;//支付类型(1:储值卡，2:现金,3:银行卡,4:微信,5:支付宝,6:优惠券，7：打白条;8:多方式付款;9:微信个人，10：支付宝（个人）)

    /**
     * 订单门票备注
     */
    private String remark;


    /**
     * 下单时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_Time")
    private Date orderTime = new Date();

    private OrderOperatorStatus orderOperatorStatus;

    private String operatorRemark;

    public void setNum(String num) {
        this.num = num;
    }

//endregion

    //region Property Get&Set

    public List<OrderProduct> getProducts() {
        return products;
    }


    public void synacOrderOperatorStatus() {
        List<OrderOperatorStatus> orderOperatorStatuses = new ArrayList<>();
        Boolean allCancel = true;
        if(products==null||products.size()==0){
            allCancel =false;
        }

        for (OrderProduct p : products) {
            p.synacProductError();
            if (p.getOrderOperatorStatus() != OrderOperatorStatus.Success) {
                orderOperatorStatuses.add(p.getOrderOperatorStatus());
            }

            if(p.getStatus()!=OrderProductStatus.returned){
                allCancel= false;
            }
        }

        if(orderOperatorStatuses.size()==0){
            this.orderOperatorStatus = OrderOperatorStatus.Success;
            this.operatorRemark="";
        }else {
            if (orderOperatorStatuses.contains(OrderOperatorStatus.ZybError)) {
                this.orderOperatorStatus = OrderOperatorStatus.ZybError;

            } else  if (orderOperatorStatuses.contains(OrderOperatorStatus.ZybImgError)) {
                this.orderOperatorStatus = OrderOperatorStatus.ZybImgError;

            } else  if (orderOperatorStatuses.contains(OrderOperatorStatus.zybCancelError)) {
                this.orderOperatorStatus = OrderOperatorStatus.zybCancelError;

            }
        }
        if(allCancel){
            this.setStatus(OrderStatus.canceled);
            this.setOrderOperatorStatus(OrderOperatorStatus.Success);
        }
        this.setLastUpdateTime(new Date());

    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPayTypes() {
        return payTypes;
    }

    public void setPayTypes(String payTypes) {
        this.payTypes = payTypes;
    }

    public void setProduct(List<OrderProduct> products) {
        this.products = products;
    }

    public Tourist getTourist() {
        return tourist;
    }

    public void setTourist(Tourist tourist) {
        this.tourist = tourist;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public String getNum() {
        return num;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        if (status.ordinal() > this.status.ordinal())
            this.status = status;
    }

    public Fetcher getFetcher() {
        return fetcher;
    }

    public void setFetcher(Fetcher fetcher) {
        this.fetcher = fetcher;
    }

//    public List<OrderProductItem> getProductItems() {
//        return productItems;
//    }
//
//    public void setProductItems(List<OrderProductItem> productItems) {
//        this.productItems = productItems;
//    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public OrderOperatorStatus getOrderOperatorStatus() {
        return orderOperatorStatus;
    }

    public void setOrderOperatorStatus(OrderOperatorStatus orderOperatorStatus) {
        this.orderOperatorStatus = orderOperatorStatus;
    }

    public String getOperatorRemark() {
        return operatorRemark;
    }

    public void setOperatorRemark(String operatorRemark) {
        this.operatorRemark = operatorRemark;
    }

//endregion

}
