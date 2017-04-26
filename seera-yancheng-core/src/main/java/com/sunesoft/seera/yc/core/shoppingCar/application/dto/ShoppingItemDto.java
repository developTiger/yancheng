package com.sunesoft.seera.yc.core.shoppingCar.application.dto;

import com.sunesoft.seera.yc.core.product.domain.ProductType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 购物车商品项dto
 * Created by zhaowy on 2016/7/18.
 */
public class ShoppingItemDto {
    /**
     * 限购区域
     */
    private String rejectAreas;
    /**
     * 用于展示，限购区域的名称
     */
    private String rejectAreasNames;

    /**
     * 购物车商品项标识
     */
    public Long id;

    /**
     * 游客标识
     */
    public Long touristId;
    /**
     * 游客标识
     */
    public String touristName;

    /**
     * 商品标识
     */
    public Long productId;

    /**
     * 商品名称
     */
    public String productName;

    /**
     * 预约入园时间
     */
    private Date tourScheduleDate;

    /**
     * 预约入住时间
     */
    private Date hotelScheduleDate;


    /**
     * 商品价格
     */
    public BigDecimal price;

    /**
     * 特殊规格说明
     * 如适用时间，有效期限等特殊说明
     */
    private String specDescription = "";

    /**
     * 商品数量
     */
    public Integer count;

    /**
     * 商品库存量，定义时取决于商品项的最小库存量
     */
    private int stock;


    /**
     * 商品图片
     */
    private String mainPicturePath;


    /**
     * 商品类型
     */
    private ProductType type;
    /**
     * 是否有效.
     * <p>购物车商品是否有效随商品下架，</p>
     */
    public Boolean isValid;


    //region Property Get&Set


    public String getRejectAreas() {
        return rejectAreas;
    }

    public void setRejectAreas(String rejectAreas) {
        this.rejectAreas = rejectAreas;
    }

    public String getRejectAreasNames() {
        return rejectAreasNames;
    }

    public void setRejectAreasNames(String rejectAreasNames) {
        this.rejectAreasNames = rejectAreasNames;
    }

    public Long getTouristId() {
        return touristId;
    }

    public void setTouristId(Long touristId) {
        this.touristId = touristId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

//    public LocalDate getTourScheduleDate() {
//        return tourScheduleDate;
//    }
//
//    public void setTourScheduleDate(LocalDate tourScheduleDate) {
//        this.tourScheduleDate = tourScheduleDate;
//    }
//
//    public LocalDate getHotelScheduleDate() {
//        return hotelScheduleDate;
//    }
//
//    public void setHotelScheduleDate(LocalDate hotelScheduleDate) {
//        this.hotelScheduleDate = hotelScheduleDate;
//    }

    public String getTouristName() {
        return touristName;
    }

    public void setTouristName(String touristName) {
        this.touristName = touristName;
    }

    public String getSpecDescription() {
        return specDescription;
    }

    public void setSpecDescription(String specDescription) {
        this.specDescription = specDescription;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
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

    public String getMainPicturePath() {
        return mainPicturePath;
    }

    public void setMainPicturePath(String mainPicturePath) {
        this.mainPicturePath = mainPicturePath;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }


    //endregion
}
