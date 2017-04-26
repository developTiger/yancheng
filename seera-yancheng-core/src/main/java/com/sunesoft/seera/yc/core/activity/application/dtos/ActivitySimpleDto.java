package com.sunesoft.seera.yc.core.activity.application.dtos;

import com.sunesoft.seera.yc.core.activity.domain.ActivityStatus;
import com.sunesoft.seera.yc.core.activity.domain.ActivityType;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductDto;

import java.util.Date;

/**
 * Created by xiazl on 2016/8/6.
 */
public class ActivitySimpleDto {

    /**
     * 活动名称
     */
    private String name;
    /**
     * 商品
     */
    private ProductDto productDto;


    /**
     * 活动开始时间
     */
    private Date startTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

    /**
     * 活动图片路径
     */
    private String pticturePath;

    /**
     * 活动类型
     */
    private ActivityType type;


    private Integer groupMinCount;





    /**
     * 是否有PC端子页面
     * @return
     */
    private Boolean hasPageProfile;

    /**
     * 是否有移动端子页面
     * @return
     */
    private Boolean hasPageProfileMobile;

    private ActivityStatus activityStatus;

    private Long id;

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductDto getProductDto() {
        return productDto;
    }

    public void setProductDto(ProductDto productDto) {
        this.productDto = productDto;
    }

    public String getPticturePath() {
        return pticturePath;
    }

    public void setPticturePath(String pticturePath) {
        this.pticturePath = pticturePath;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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

    public Boolean getHasPageProfile() {
        return hasPageProfile;
    }

    public void setHasPageProfile(Boolean hasPageProfile) {
        this.hasPageProfile = hasPageProfile;
    }

    public Boolean getHasPageProfileMobile() {
        return hasPageProfileMobile;
    }

    public void setHasPageProfileMobile(Boolean hasPageProfileMobile) {
        this.hasPageProfileMobile = hasPageProfileMobile;
    }

    public Integer getGroupMinCount() {
        if(groupMinCount==null)
            return 0;
        return    groupMinCount;
    }

    public void setGroupMinCount(Integer groupMinCount) {
        this.groupMinCount = groupMinCount;
    }
}
