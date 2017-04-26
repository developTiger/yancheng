package com.sunesoft.seera.yc.core.product.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 商品项实体聚合根
 * Created by zhaowy on 2016/7/11.
 */
@Entity
@Table(name = "productItem")
public class ProductItem extends BaseEntity {

    //region Construct

    public ProductItem() {
//        this.setIsActive(true);
//        this.setCreateDateTime(new Date());
//        this.setLastUpdateTime(new Date());
        //this.num=NumGenerator.generate();
    }

    /**
     * @param num 商品项编号自行定义
     * @param name 商品项名称
     * @param price 价格
     * @param seller 商家
     * @param type 商品项类型
     */
    public ProductItem(String num,String name, BigDecimal price, String seller, ProductItemType type) {
//        this.setIsActive(true);
//        this.setCreateDateTime(new Date());
//        this.setLastUpdateTime(new Date());
        this.name = name;
        this.price = price;
        this.seller = seller;
        this.productItemType = type;
/*        this.stockTotal= stock;
        this.stockConsumed =0;*/
    }
    //endregion

    //region Field

    /**
     * 商品项编号
     * <p>商品项编号跟内部库存系统的商品项编号一致。</p>
     */
    private String num;

    /**
     * 商品项名称
     */
    @Column(name = "item_name")
    private String name;

    /**
     * 商品项价格
     */
    private BigDecimal price;

    /**
     * 商品家名称
     */
    private String seller;

    /**
     * 扫描员标识
     * 用于商品扫码校验
     */
    private Long innerManageId;

    /**
     * 商品项类型枚举
     */
    @Column(name = "product_item_type")
    private ProductItemType productItemType;

    //endregion

    //region Property Get&Set

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

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public ProductItemType getProductItemType() {
        return productItemType;
    }

    public void setProductItemType(ProductItemType productItemType) {
        this.productItemType = productItemType;
    }

    public Long getInnerManageId() {
        return innerManageId;
    }

    public void setInnerManageId(Long innerManageId) {
        this.innerManageId = innerManageId;
    }

    //endregion
}
