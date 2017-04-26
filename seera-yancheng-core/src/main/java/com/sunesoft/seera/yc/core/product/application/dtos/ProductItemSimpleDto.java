package com.sunesoft.seera.yc.core.product.application.dtos;

import com.sunesoft.seera.yc.core.product.domain.ProductItemType;

import java.math.BigDecimal;

/**
 * 商品项简要信息类
 * Created by zhaowy on 2016/7/12.
 */
public class ProductItemSimpleDto {

    /**
     * 标识
     */
    private Long id;

    /**
     * 商品项编号
     */
    private String num;

    /**
     * 商品项名称
     */
    private String name;

    /**
     * 商品项价格
     */
    private BigDecimal price;

    /**
     * 扫描员标识
     * 用于商品扫码校验
     */
    private Long innerManageId;

    /**
     * 商品项类型枚举
     */
    private ProductItemType productItemType;
    public ProductItemType getProductItemType() {
        return productItemType;
    }

    public void setProductItemType(ProductItemType productItemType) {
        this.productItemType = productItemType;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getInnerManageId() {
        return innerManageId;
    }

    public void setInnerManageId(Long innerManageId) {
        this.innerManageId = innerManageId;
    }
}
