package com.sunesoft.seera.yc.core.product.application.dtos;

import java.math.BigDecimal;

/**
 * 商品简要信息类
 * Created by zhaowy on 2016/7/12.
 */
public class ProductSimpleDto {

    /**
     * 限购区域
     */
    private String rejectAreas;
    /**
     * 用于展示，限购区域的名称
     */
    private String rejectAreasNames;


    /**
     * 标识
     */
    private Long id;
    /**
     * 商品名称
     */
    private String name;

    /**
     * 特殊规格说明
     * 如适用时间，有效期限等特殊说明
     */
    private String specDescription;

    /**
     * 商品编号
     */
    private String num;

    /**
     * 商品标价(原价)
     * 默认为组合商品项价格总和，可编辑
     */
    private BigDecimal price;

    /**
     * 折扣价
     */
    private BigDecimal discountPrice;

    /**
     * 商品主图片Id
     */
    private String mainPicturePath;

    /**
     * 库存
     */
    private int stock;
    /**
     * 已售数量
     */
    private Integer saleCount;

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

    public Integer getSaleCount() {
        if(saleCount==null)
            return 0;
        return saleCount;
    }

    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getMainPicturePath() {
        return mainPicturePath;
    }

    public void setMainPicturePath(String mainPicturePath) {
        this.mainPicturePath = mainPicturePath;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getSpecDescription() {
        return specDescription;
    }

    public void setSpecDescription(String specDescription) {
        this.specDescription = specDescription;
    }
    //endregion
}
