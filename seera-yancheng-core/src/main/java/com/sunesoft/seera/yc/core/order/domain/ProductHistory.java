package com.sunesoft.seera.yc.core.order.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;
import com.sunesoft.seera.yc.core.product.domain.ProductType;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * 商品历史版本
 * <p>商品版本控制通过比较lastUpdateTime时间
 * HistorProducty.getLastUpdateTime() > Product.getLastUpdateTime()视为版本一致，
 * 否则需要新增ProductHistory版本</p>
 *
 * Created by zhaowy on 2016/7/20.
 */
@Entity
public class ProductHistory extends BaseEntity {

    //region Constructor


    public  ProductHistory()
    {

    }

    //endregion

    //region Field
    private String rejectAreas;

    /**
     * 原始 商品 标识
     * <p>用于关联原始商品的最新情况</p>
     *
     */
    private Long originalId;

    /**
     * 商品编号
     * <p>与原始商品信息一致，为保障数据迁移导致的数据隐射关系</p>
     */
    private String num;


    private BigDecimal price;

    /**
     * 商品总价
     * <p>商品总价由原始商品定义的商品销售价格计算得出</p>
     */
    private BigDecimal discountPrice;

    /**
     * 商品主图片
     */
    private String mainPicturePath;

    /**
     * 商品名称
     */
//     @Column(name = "product_name")
    private String name;

    /**
     * 产品类型
     */
    private ProductType type;

    //endregion

    //region Property Get&Set

    public Long getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Long originalId) {
        this.originalId = originalId;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

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

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
    //endregion

}
