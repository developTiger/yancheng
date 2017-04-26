package com.sunesoft.seera.yc.core.order.application.dtos;

import com.sunesoft.seera.yc.core.order.domain.OrderOperatorStatus;
import com.sunesoft.seera.yc.core.order.domain.OrderProductStatus;

import java.util.Date;
import java.util.List;

/**
 * Created by zhaowy on 2016/7/20.
 */
public class OrderProductDto {

    private Long id;

    /**
     * 对应版本商品
     */
    private ProductHistoryDto productDto;

    /**
     * 商品数量
     */
    private int count;

    /**
     * 订单商品状态
     */
    private OrderProductStatus status;

    /**
     * 可改签
     */
    private Boolean canMeal;

    /**
     * 可退
     */
    private Boolean canReturn;

    /**
     * 预约入园时间
     */
    private Date tourScheduleDate;

    /**
     * 预改签入园日期
     */
    private Date tourScheduleMealDate;

    /**
     * 预约入住时间
     */
    private Date hotelScheduleDate;

    /**
     * 预改签入住时间
     */
    private Date hotelScheduleMealDate;

    private OrderOperatorStatus orderOperatorStatus;

    private String operatorRemark;

    private List<OrderProductItemDto> productItemDtos;

    private Date lastUpdateTime;


    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public ProductHistoryDto getProductDto() {
        return productDto;
    }

    public void setProductDto(ProductHistoryDto productDto) {
        this.productDto = productDto;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public OrderProductStatus getStatus() {
        return status;
    }

    public void setStatus(OrderProductStatus status) {
        this.status = status;
    }

    public Boolean getCanMeal() {
        return canMeal;
    }

    public void setCanMeal(Boolean canMeal) {
        this.canMeal = canMeal;
    }

    public Boolean getCanReturn() {
        return canReturn;
    }

    public void setCanReturn(Boolean canReturn) {
        this.canReturn = canReturn;
    }


    public Date getTourScheduleDate() {
        return tourScheduleDate;
    }

    public void setTourScheduleDate(Date tourScheduleDate) {
        this.tourScheduleDate = tourScheduleDate;
    }

    public Date getTourScheduleMealDate() {
        return tourScheduleMealDate;
    }

    public void setTourScheduleMealDate(Date tourScheduleMealDate) {
        this.tourScheduleMealDate = tourScheduleMealDate;
    }

    public Date getHotelScheduleDate() {
        return hotelScheduleDate;
    }

    public void setHotelScheduleDate(Date hotelScheduleDate) {
        this.hotelScheduleDate = hotelScheduleDate;
    }

    public Date getHotelScheduleMealDate() {
        return hotelScheduleMealDate;
    }

    public void setHotelScheduleMealDate(Date hotelScheduleMealDate) {
        this.hotelScheduleMealDate = hotelScheduleMealDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<OrderProductItemDto> getProductItemDtos() {
        return productItemDtos;
    }

    public void setProductItemDtos(List<OrderProductItemDto> productItemDtos) {
        this.productItemDtos = productItemDtos;
    }
}
