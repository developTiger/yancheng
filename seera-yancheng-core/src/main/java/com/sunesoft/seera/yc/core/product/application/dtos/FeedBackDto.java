package com.sunesoft.seera.yc.core.product.application.dtos;

import java.util.Date;

/**
 * Created by zhaowy on 2016/7/19.
 */
public class FeedBackDto {

    //region Field

    private Long id;
    /**
     * 关联商品标识
     */
    private Long productId;

    /**
     *  游客标识
     */
    private Long touristId;

    /**
     *  游客姓名
     */
    private String touristRealName;

    private Date createDateTime; // 创建时间

    private String content;

    private Integer score;

//endregion

    //region Property Get&Set

    //商品编号
    private String num;

    private String commentProduct;

    private String serviceProduct;

    public String getCommentProduct() {
        return commentProduct;
    }

    public void setCommentProduct(String commentProduct) {
        this.commentProduct = commentProduct;
    }

    public String getServiceProduct() {
        return serviceProduct;
    }

    public void setServiceProduct(String serviceProduct) {
        this.serviceProduct = serviceProduct;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTouristRealName() {
        return touristRealName;
    }

    public void setTouristRealName(String touristRealName) {
        this.touristRealName = touristRealName;
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

    //endregion
}
