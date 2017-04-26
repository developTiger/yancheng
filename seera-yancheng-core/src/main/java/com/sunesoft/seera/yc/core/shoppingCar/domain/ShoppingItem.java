package com.sunesoft.seera.yc.core.shoppingCar.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;
import com.sunesoft.seera.yc.core.product.domain.Product;
import com.sunesoft.seera.yc.core.tourist.domain.Tourist;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 购物车商品项实体
 * Created by zhaowy on 2016/7/18.
 */
@Entity
public class ShoppingItem extends BaseEntity {
    public ShoppingItem() {
        this.isValid= true;
    }

    //region Field

    /**
     * 游客信息
     */
    @ManyToOne
    @JoinColumn(name = "tourist_id")
    public Tourist tourist;

    /**
     * 商品信息
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    public Product product;

    /**
     * 商品数量
     */
    @Column(name = "shopping_count")
    public Integer count;

    /**
     * 是否有效.
     * <p>购物车商品是否有效随商品下架，</p>
     */
    @Column(name = "is_valid")
    public Boolean isValid;

    /**
     * 预约入园时间
     */
    private Date tourScheduleDate;

    /**
     * 预约入住时间
     */
    private Date hotelScheduleDate;

    //endregion

    //region Property Get & Set

    public Tourist getTourist() {
        return tourist;
    }

    public void setTourist(Tourist tourist) {
        this.tourist = tourist;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    /**
     * 增加购物车商品项数量
     *
     * @param count 增加数量
     */
    public void increaseItemCount(Integer count) {
        this.count += count;
        this.setLastUpdateTime(new Date());
    }

    /**
     * 减少购物车商品数量
     *
     * @param count 减少数量
     */
    public void reduceItemCount(Integer count) {
        if (this.count <= count)
            this.count = 0;
        else this.count -= count;
        this.setLastUpdateTime(new Date());
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

    //endregion


}
