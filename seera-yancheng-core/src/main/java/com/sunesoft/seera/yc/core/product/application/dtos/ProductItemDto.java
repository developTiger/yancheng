package com.sunesoft.seera.yc.core.product.application.dtos;

import com.sunesoft.seera.yc.core.product.domain.ProductItemType;

import java.math.BigDecimal;

/**
 * 商品项详细信息类
 * Created by zhaowy on 2016/7/11.
 */
public class ProductItemDto extends ProductItemSimpleDto {

    /**
     * 商品项商家
     */
    private String  seller;



    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }
}
