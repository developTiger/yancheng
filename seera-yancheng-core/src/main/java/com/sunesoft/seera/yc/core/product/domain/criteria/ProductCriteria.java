package com.sunesoft.seera.yc.core.product.domain.criteria;

import com.sunesoft.seera.fr.results.PagedCriteria;
import com.sunesoft.seera.yc.core.product.domain.ProductKind;
import com.sunesoft.seera.yc.core.product.domain.ProductStatus;
import com.sunesoft.seera.yc.core.product.domain.ProductType;

import java.util.Date;

/**
 * Created by zhaowy on 2016/7/12.
 */
public class ProductCriteria extends PagedCriteria {
    /**
     * 限定区域
     */
   private String rejectAreas;

    //region Field
    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品编号
     */
    private String num;

    /**
     * 商品类型
     */
    private ProductType type;

    /**
     * 商品状态
     */
    private ProductStatus status;

    /**
     * 商品销售类型
     */
    private ProductKind kind;

    /**
     * 是可改签
     */
    private Boolean canMeal;

    /**
     * 是否能退
     */
    private Boolean canReturn;

    /**
     * 查询开始时间
     */
    private Date startTime;

    /**
     * 查询结束时间
     */
    private Date endTime;

    //endregion

    //region Property Get&Set


    public String getRejectAreas() {
        return rejectAreas;
    }

    public void setRejectAreas(String rejectAreas) {
        this.rejectAreas = rejectAreas;
    }

    public ProductKind getKind() {
        return kind;
    }

    public void setKind(ProductKind kind) {
        this.kind = kind;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public Boolean getCanMeal() {
        return canMeal;
    }

    public void setCanMeal(Boolean canMeal) {
        this.canMeal = canMeal;
    }

    public Boolean getCanReturn() {
        return canReturn;
    }

    public void setCanReturn(Boolean canReturn) {
        this.canReturn = canReturn;
    }

    //endregion
}
