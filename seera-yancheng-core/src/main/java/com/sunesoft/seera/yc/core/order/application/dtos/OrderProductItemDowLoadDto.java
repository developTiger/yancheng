package com.sunesoft.seera.yc.core.order.application.dtos;

import com.sunesoft.seera.yc.core.order.domain.ItemStatus;
import com.sunesoft.seera.yc.core.product.domain.ProductItemType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhaowy on 2016/7/20.
 */
public class OrderProductItemDowLoadDto {



    /**
     * 订单编号
     * @return
     */
    private String orderNum;

    /**
     * 预约时间
     */
    private Date scheduleDate;

    /**
     * 商品项名称
     */
    private String name;

    /**
     * 商品项编号
     */
    private String num;

    /**
     * 商品项类型
     */
    private String type;

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

    private String ItemStatus;

    /**
     * 商品项领取时间
     */
    private Date takeDate;


    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getTakeDate() {
        return takeDate;
    }

    public void setTakeDate(Date takeDate) {
        this.takeDate = takeDate;
    }



    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItemStatus() {
        return ItemStatus;
    }

    public void setItemStatus(String itemStatus) {
        ItemStatus = itemStatus;
    }

}
