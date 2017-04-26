package com.sunesoft.seera.yc.core.activity.domain.creteria;

import com.sunesoft.seera.fr.results.PagedCriteria;
import com.sunesoft.seera.yc.core.activity.domain.ActivityStatus;
import com.sunesoft.seera.yc.core.activity.domain.ActivityType;
import java.util.Date;

/**
 * Created by xiazl on 2016/8/6.
 */
public class ActivityCriteria extends PagedCriteria {

    /**
     * 活动名称
     */
    private String name;

    /**
     * 起始时间
     */
    private Date beginTime;
    /**
     * 终止时间
     */
    private Date overTime;

    /**
     * 活动类型
     */
    private ActivityType type;

    /**
     * 商品名称
     */
    private String ProductName;
    /**
     * 商品id
     */
    private Long productId;


    private ActivityStatus activityStatus;



    //region Description  getter an setter
    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getOverTime() {
        return overTime;
    }

    public void setOverTime(Date overTime) {
        this.overTime = overTime;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public ActivityStatus getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(ActivityStatus activityStatus) {
        this.activityStatus = activityStatus;
    }

    //endregion
}
