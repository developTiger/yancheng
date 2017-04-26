package com.sunesoft.seera.yc.core.product.application.dtos;

import com.sunesoft.seera.yc.core.product.domain.ProductCT;
import com.sunesoft.seera.yc.core.product.domain.ProductKind;
import com.sunesoft.seera.yc.core.product.domain.ProductStatus;
import com.sunesoft.seera.yc.core.product.domain.ProductType;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品详细信息类
 * Created by zhaowy on 2016/7/12.
 */
public class ProductDto extends ProductSimpleDto {
    /**
     * 限购省份
     */



    public ProductDto(){
        detailPicturesPaths= Collections.EMPTY_LIST;
    }
    //region Filed

    /**
     * 商品项清单
     */
    private List<ProductItemDto> productItemDtoList;

    /**
     * 商品销售类型
     */
    private ProductKind kind;

    /*
   商品项标识+商品项数量组成
   id:count
   <p>1:2;2:1;4:10</p>
   * */
    private Map<Long,Integer> idAndCount;

    /**
     * 上架时间
     */
    private Date onSaleTime;

    /**
     * 商品状态
     */
    private ProductStatus Status;

    /**
     * 商品类型
     */
    private ProductType type;

    /**
     * 是否包含酒店
     */
    private Boolean hasHotel;

    /**
     * 商品介绍
     */
    private String profile;

    /**
     * 购买须知
     */
    private String notice;

    /**
     * 交通指南
     */
    private String trafficGuide;

    /**
     * 商品轮播图ID集合
     */
    private List<String> detailPicturesPaths;

    /**
     * 可改签
     */
    private Boolean canMeal;

    /**
     * 可退
     */
    private Boolean canReturn;

    /**
     * 创建时间
     */
    private Date createDateTime;

    /**
     *商品类型
     */
    private String typeString;
    /**
     * 页面需要展示的数量字段
     */
    private String countString;

    /**
     * 票种 当天票 /区间票/永久票
     *
     */
    private ProductCT productCt;


    /**
     * 区间票的时间区间
     */
    private Date ctBeginDate;

    private String sctBeginDates[];


    /**
     *
     */
    private  Date ctEndDate;


    public Map<Long, Integer> getIdAndCount() {
        return idAndCount;
    }

    public void setIdAndCount(Map<Long, Integer> idAndCount) {
        this.idAndCount = idAndCount;
    }
//endregion

    // region Property Get&Set


    public ProductKind getKind() {
        return kind;
    }

    public void setKind(ProductKind kind) {
        this.kind = kind;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }

    public ProductStatus getStatus() {
        return Status;
    }

    public void setStatus(ProductStatus status) {
        Status = status;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getTrafficGuide() {
        return trafficGuide;
    }

    public void setTrafficGuide(String trafficGuide) {
        this.trafficGuide = trafficGuide;
    }

    public List<String> getDetailPicturesPaths() {
        return detailPicturesPaths;
    }

    public void setDetailPicturesPaths(List<String> detailPicturesPaths) {
        this.detailPicturesPaths = detailPicturesPaths;
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

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }


    public List<ProductItemDto> getProductItemDtoList() {
        return productItemDtoList;
    }

    public void setProductItemDtoList(List<ProductItemDto> productItemDtoList) {
        this.productItemDtoList = productItemDtoList;
    }
    //endregion


    public String getCountString() {
        return countString;
    }

    public void setCountString(String countString) {
        this.countString = countString;
    }

    public Date getOnSaleTime() {
        return onSaleTime;
    }

    public void setOnSaleTime(Date onSaleTime) {
        this.onSaleTime = onSaleTime;
    }

    public Boolean getHasHotel() {
        return hasHotel;
    }

    public void setHasHotel(Boolean hasHotel) {
        this.hasHotel = hasHotel;
    }


    public ProductCT getProductCt() {
        return productCt;
    }

    public void setProductCt(ProductCT productCt) {
        this.productCt = productCt;
    }

    public Date getCtBeginDate() {
        return ctBeginDate;
    }

    public void setCtBeginDate(Date ctBeginDate) {
        this.ctBeginDate = ctBeginDate;
    }

    public Date getCtEndDate() {
        return ctEndDate;
    }

    public void setCtEndDate(Date ctEndDate) {
        this.ctEndDate = ctEndDate;
    }

    public String[] getCtBeginDates() {
        return sctBeginDates;
    }

    public void setCtBeginDates(String[] ctBeginDates) {
        this.sctBeginDates = ctBeginDates;
    }

    public String[] getSctBeginDates() {
        return sctBeginDates;
    }

    public void setSctBeginDates(String[] sctBeginDates) {
        this.sctBeginDates = sctBeginDates;
    }

    }
