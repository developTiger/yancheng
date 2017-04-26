package com.sunesoft.seera.yc.core.product.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;
import com.sunesoft.seera.yc.core.tourist.domain.Tourist;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * 订单评价信息
 * Created by zhaowy on 2016/7/11.
 */
@Entity
public class FeedBack extends BaseEntity {


    //region Construct

    public FeedBack() {
        this.setIsActive(true);
        this.setCreateDateTime(new Date());
        this.setLastUpdateTime(new Date());
    }

    //endregion

    //region Field

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

    /**
     * 内容
     */
    private String content;

    /**
     * 评分
     */
    private Integer score;


//endregion

    //region Property Get&Set

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
