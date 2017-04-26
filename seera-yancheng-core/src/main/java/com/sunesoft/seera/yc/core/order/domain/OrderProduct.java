package com.sunesoft.seera.yc.core.order.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;
import com.sunesoft.seera.fr.utils.DateHelper;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单商品信息实体
 * <remark>订单中包含的商品信息包括商品本身、商品数量</remark>
 * Created by  on 2016/7/20.
 */
@Entity
public class OrderProduct extends BaseEntity {

    //region Field

    /**
     * 对应版本商品
     */
    @ManyToOne
    private ProductHistory product;

    /**
     * 订单商品项集合
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<OrderProductItem> items;

//    @ManyToOne()
//    private OrderInfo orderInfo;
    /**
     * 商品数量
     */
    private int count;

    /**
     * 本订单商品总价
     * <p></p>
     */
    private BigDecimal totalPrice;

    /**
     * 订单商品状态
     */
    @Column(name = "op_status")
    private OrderProductStatus status = OrderProductStatus.normal;

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


    public OrderProduct() {
        this.orderOperatorStatus = OrderOperatorStatus.Wait;
        this.setIsActive(true);
        this.setCreateDateTime(new Date());
        this.setLastUpdateTime(new Date());
    }
    //endregion

    public void synacProductError() {
        List<OrderOperatorStatus> orderOperatorStatuses = new ArrayList<>();
        Boolean isAllChecked=true;
        for (OrderProductItem item : items) {

            if(item.getItemStatus()!=ItemStatus.checked)
                isAllChecked=false;
            if (item.getItemStatus() == ItemStatus.cancelFailed) {
                orderOperatorStatuses.add(OrderOperatorStatus.zybCancelError);
                continue;
            }
            if (item.getItemStatus() == ItemStatus.createError) {
                orderOperatorStatuses.add(OrderOperatorStatus.ZybError);
                continue;
            }
            if (item.getItemStatus() == ItemStatus.reCreateError) {
                orderOperatorStatuses.add(OrderOperatorStatus.ZybError);
                continue;
            }
            if (item.getItemStatus() == ItemStatus.imgError) {
                orderOperatorStatuses.add(OrderOperatorStatus.ZybImgError);
                continue;
            }


        }
        if (orderOperatorStatuses.size() == 0) {
            this.orderOperatorStatus = OrderOperatorStatus.Success;
            this.operatorRemark="";
        } else {
            if (orderOperatorStatuses.contains(OrderOperatorStatus.ZybError)) {
                this.orderOperatorStatus = OrderOperatorStatus.ZybError;

            } else if (orderOperatorStatuses.contains(OrderOperatorStatus.ZybImgError)) {
                this.orderOperatorStatus = OrderOperatorStatus.ZybImgError;

            } else if (orderOperatorStatuses.contains(OrderOperatorStatus.zybCancelError)) {
                this.orderOperatorStatus = OrderOperatorStatus.zybCancelError;

            }

        }
        if(isAllChecked){
            this.setStatus(OrderProductStatus.end);
        }
    }

    //region Property Get&Set

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public ProductHistory getProduct() {
        return product;
    }

    public void setProduct(ProductHistory product) {
        this.product = product;

        this.totalPrice = this.getProduct().getDiscountPrice().multiply(BigDecimal.valueOf(this.count));
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        this.totalPrice = null != this.getProduct() ?
                this.getProduct().getDiscountPrice().multiply(BigDecimal.valueOf(this.count))
                : BigDecimal.ZERO;

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

    public OrderProductStatus getStatus() {
        return status;
    }

    public void setStatus(OrderProductStatus status) {
        if (status.ordinal() > this.status.ordinal())
            this.status = status;
    }

    public Date getTourScheduleDate() {
        return tourScheduleDate;
    }

    public void setTourScheduleDate(Date tourScheduleDate) {
        this.tourScheduleDate = tourScheduleDate;
    }

    public Date getHotelScheduleDate() {
        return hotelScheduleDate;
    }

    public void setHotelScheduleDate(Date hotelScheduleDate) {
        this.hotelScheduleDate = hotelScheduleDate;
    }

    public Date getTourScheduleMealDate() {
        return tourScheduleMealDate;
    }

    protected void setTourScheduleMealDate(Date tourScheduleMealDate) {
        this.tourScheduleMealDate = tourScheduleMealDate;
    }

    public Date getHotelScheduleMealDate() {
        return hotelScheduleMealDate;
    }

    protected void setHotelScheduleMealDate(Date hotelScheduleMealDate) {
        this.hotelScheduleMealDate = hotelScheduleMealDate;
    }


    //endregion

    //region Domain Action

    /**
     * 改签
     *
     * @param tourMealDate          改签入园日期
     * @param hotelScheduleMealDate 改签入住日期，非酒店订单可忽略
     * @return
     */
    public boolean meal(Date tourMealDate, Date hotelScheduleMealDate) {
        // 只允许已付款订单改签，且改签日期(两个同时)不能为当日，或预定同日
        if (null == tourMealDate && null == hotelScheduleMealDate) return false;
        //正常情况之外不允许改签
        if (this.status != OrderProductStatus.normal)
            return false;
        //两个数据都没有改变的情况下，不进行改签操作
        if (null != tourMealDate && null != hotelScheduleMealDate
                && (DateHelper.isSameDay(hotelScheduleMealDate, this.tourScheduleDate) && DateHelper.isSameDay(tourMealDate, this.tourScheduleDate)))
            return false;
        //允许改签为当前时间之后并排除已预定日期
        if (null != tourMealDate && tourMealDate.after(new Date()))
        //&& !DateHelper.isSameDay(tourMealDate,this.tourScheduleDate))
        {
            this.setTourScheduleMealDate(tourMealDate);
        }

        //允许改签为当前时间之后并排除已预定日期
        if (null != hotelScheduleMealDate && hotelScheduleMealDate.after(new Date()))
        // && !DateHelper.isSameDay(hotelScheduleMealDate,this.tourScheduleDate))
        {
            this.setTourScheduleMealDate(hotelScheduleMealDate);
        }
        this.setStatus(OrderProductStatus.mealCheck);
        return true;
    }

    /**
     * 改签审核
     * <p>改签成功后计算订单入园或入住时间已改签后的时间为准</p>
     *
     * @param approve
     * @return
     */
    public boolean mealCheck(Boolean approve) {
        if (approve && this.status == OrderProductStatus.mealCheck) {
            this.status = OrderProductStatus.mealed;
            return true;
        }
        return false;
    }

    /**
     * 退单处理
     *
     * @param approve 同意退单
     * @return 改签成功与否
     */
    public boolean returnApprove(Boolean approve) {
        //管理员不同意退票、 订单已消费使用、已退单得情况下不允许退单
        if (approve && this.status != OrderProductStatus.end
                && this.status != OrderProductStatus.returned) {
            this.status = OrderProductStatus.returned;
            return true;
        }
        return false;
    }

    public List<OrderProductItem> getItems() {
        return items;
    }

    public void setItems(List<OrderProductItem> items) {
        this.items = items;
    }

    public OrderOperatorStatus getOrderOperatorStatus() {
        return orderOperatorStatus;
    }

    public void setOrderOperatorStatus(OrderOperatorStatus orderOperatorStatus) {
        this.setLastUpdateTime(new Date());
        this.orderOperatorStatus = orderOperatorStatus;
    }

    public String getOperatorRemark() {
        return operatorRemark;
    }

    public void setOperatorRemark(String operatorRemark) {
        this.operatorRemark = operatorRemark;
    }

//    public OrderInfo getOrderInfo() {
//        return orderInfo;
//    }


    //endregion
}
