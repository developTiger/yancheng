package com.sunesoft.seera.yc.core.order.application.dtos;

import com.sunesoft.seera.yc.core.product.domain.ProductType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhaowy on 2016/7/20.
 */
public class ProductHistoryDto {
    /**
     * 限购区域
     */
    private String rejectAreas;
    /**
     * 限购区域名称
     */
    private String rejectAreasNames;

    /**
     * 标识
     */
    private Long id;

    /**
     * 商品原始Id
     */
    private Long originalId;

    /**
     * 商品类型
     */
    private ProductType type;

    /**
     * 商品编码
     */
    private String num;

    /**
     * 商品名称
     */
/*    @Column(name = "product_name")*/
    private String name;

    /**
     * 商品标价
     * 默认为组合商品项价格总和，可编辑
     */
    private BigDecimal price;

    /**
     * 创建时间
     */
    private Date createDateTime;

    /**
     * 折扣价
     */
    private BigDecimal discountPrice;

    /**
     * 商品主图片
     */
    private String mainPicturePath;

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Long originalId) {
        this.originalId = originalId;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
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
}
