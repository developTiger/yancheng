package com.sunesoft.seera.yc.core.product.domain.criteria;

import com.sunesoft.seera.fr.results.PagedCriteria;
import com.sunesoft.seera.yc.core.product.domain.ProductItemType;

/**
 * Created by zhaowy on 2016/7/12.
 */
public class ProductItemCriteria extends PagedCriteria {

    //region Field
    /**
     * 商品项名称
     */
    private String name;

    /**
     * 商品编号
     */
    private String num;

    /**
     * 商品项类型
     */
    private ProductItemType type;

    /**
     * 商品商家
     */
    private String seller;

    //endregion

    //region Property Get&Set

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public ProductItemType getType() {
        return type;
    }

    public void setType(ProductItemType type) {
        this.type = type;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }


    //endregion
}
