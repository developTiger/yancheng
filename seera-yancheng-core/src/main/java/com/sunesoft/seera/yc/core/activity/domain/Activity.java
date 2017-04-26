package com.sunesoft.seera.yc.core.activity.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;
import com.sunesoft.seera.yc.core.product.domain.Product;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by xiazl on 2016/8/5.
 */
@Entity(name = "activity")
public class Activity extends BaseEntity {
    /**
     * 活动名称
     */
    @Column(name = "activity_name")
    private String name;

    /**
     * 商品
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


    /**
     * 活动开始时间
     */
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    /**
     * 活动结束时间
     */
    @Column(name = "End_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    /**
     * 活动图片路径
     */
    @Column(name = "picture_path")
    private String pticturePath;
    /**
     * 活动须知
     */
    private String notice;
    /**
     * 活动内容
     */
    private String content;




    /**
     * 活动类型
     */
    private ActivityType type;


    /**
     * 满指定数量成团
     */
    private Integer groupMinCount;
    /**
     * 活动状态
     */
    private ActivityStatus activityStatus;



    private Boolean hasPageProfile;

    /**
     * 活动页内容
     */
    @Column(columnDefinition="text")
    private String pageProfile;

    private Boolean hasPageProfileMobile;

    /**
     * PC端活动页内容
     */
    @Column(columnDefinition="text")
    private String pageProfileMobile;


    public Activity() {
        setIsActive(true);
        setType(ActivityType.HolidayEvent);
        this.hasPageProfile=false;
        this.hasPageProfileMobile=false;
    }


    public ActivityStatus getActivityStatus() {
//        if (this.activityStatus == null) {
//            if (this.endTime.after(new Date()) && this.startTime.before(new Date())) {
//                return ActivityStatus.Run;
//            } else if (this.startTime.after(new Date())) {
//                return ActivityStatus.Wait;
//            } else if (this.endTime.before(new Date())) {
//                return ActivityStatus.Stop;
//            }
//        }
        return activityStatus;

    }

    public void setActivityStatus(ActivityStatus activityStatus) {
//        if(this.endTime!=null&&this.endTime.before(new Date()))
//            this.activityStatus=ActivityStatus.Complete;
//        xzl
//        if( activityStatus==null){
//            if (this.endTime.after(new Date()) && this.startTime.before(new Date())) {
//                this.activityStatus= ActivityStatus.Run;
//            } else if (this.startTime.after(new Date())) {
//                this.activityStatus= ActivityStatus.Wait;
//            } else if (this.endTime.before(new Date())) {
//                this.activityStatus= ActivityStatus.Stop;
//            }
//        }else {
            this.activityStatus = activityStatus;
//        }
    }


    public Integer getGroupMinCount() {
        return groupMinCount;
    }

    public void setGroupMinCount(Integer groupMinCount) {
        this.groupMinCount = groupMinCount;
    }

    public Boolean getHasPageProfile() {
        return hasPageProfile;
    }

    public void setHasPageProfile(Boolean hasPageProfile) {
        this.hasPageProfile = hasPageProfile;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }


    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }


    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getPticturePath() {
        return pticturePath;
    }

    public void setPticturePath(String pticturePath) {
        this.pticturePath = pticturePath;
    }

    public String getPageProfile() {
        return pageProfile;
    }

    public void setPageProfile(String pageProfile) {
        this.pageProfile = pageProfile;
    }

    public Boolean getHasPageProfileMobile() {
        return hasPageProfileMobile;
    }

    public void setHasPageProfileMobile(Boolean hasPageProfileMobile) {
        this.hasPageProfileMobile = hasPageProfileMobile;
    }

    public String getPageProfileMobile() {
        return pageProfileMobile;
    }

    public void setPageProfileMobile(String pageProfileMobile) {
        this.pageProfileMobile = pageProfileMobile;
    }
}
