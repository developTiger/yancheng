package com.sunesoft.seera.yc.core.order.application.dtos;

import com.sunesoft.seera.yc.core.coupon.application.dto.CouponDto;
import com.sunesoft.seera.yc.core.order.domain.OrderOperatorStatus;
import com.sunesoft.seera.yc.core.order.domain.OrderStatus;
import com.sunesoft.seera.yc.core.tourist.application.dtos.FetcherDto;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristSimpleDto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by zhaowy on 2016/7/11.
 */
public class OrderDto {

    private  Long id;

    /**
     * 订单编号
     * <p>需定义订单编号规则</p>
     */
    private String num;

    /**
     * 下单人
     */
    private TouristSimpleDto touristSimpleDto;

    /**
     * 订单商品
     */
    private List<OrderProductDto> productDtos;
     /**
//     * 订单商品项
//     */
//    private List<OrderProductItemDto> productItemDtos;

    /**
     * 订单状态
     */
    private OrderStatus status = OrderStatus.waitPay;

    /**
     * 下单时间
     */
    private Date orderTime = new Date();

    /**
     * 使用优惠券
     */
    private CouponDto couponDto;

    /**
     * 取票人
     */
    private FetcherDto fetcherDto;

    /**
     * 总价
     */
    private BigDecimal orderPrice;

    private Date createDateTime;

    private OrderOperatorStatus orderOperatorStatus;

    private String operatorRemark;

    /**
     * 订单支付方式
     */
    private  String payTypes;//支付类型(1:储值卡，2:现金,3:银行卡,4:微信,5:支付宝,6:优惠券，7：打白条;8:多方式付款;9:微信个人，10：支付宝（个人）)

    /**
     * 订单门票备注
     */
    private String remark;

    public String getPayTypes() {
        return payTypes;
    }

    public void setPayTypes(String payTypes) {
        this.payTypes = payTypes;
    }


    //endregion

    //region Property Get&Set

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }



    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }


    public List<OrderProductDto> getProductDtos() {
        return productDtos;
    }

    public void setProductDtos(List<OrderProductDto> productDtos) {
        this.productDtos = productDtos;
    }

//    public List<OrderProductItemDto> getProductItemDtos() {
//        return productItemDtos;
//    }
//
//    public void setProductItemDtos(List<OrderProductItemDto> productItemDtos) {
//        this.productItemDtos = productItemDtos;
//    }

    public CouponDto getCouponDto() {
        return couponDto;
    }

    public void setCouponDto(CouponDto couponDto) {
        this.couponDto = couponDto;
    }

    public FetcherDto getFetcherDto() {
        return fetcherDto;
    }

    public void setFetcherDto(FetcherDto fetcherDto) {
        this.fetcherDto = fetcherDto;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }


    public TouristSimpleDto getTouristSimpleDto() {
        return touristSimpleDto;
    }

    public void setTouristSimpleDto(TouristSimpleDto touristSimpleDto) {
        this.touristSimpleDto = touristSimpleDto;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    //endregion
}
