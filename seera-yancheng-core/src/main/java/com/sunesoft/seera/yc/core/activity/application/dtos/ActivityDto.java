package com.sunesoft.seera.yc.core.activity.application.dtos;

import java.util.Date;


/**
 * Created by xiazl on 2016/8/5.
 */
public class ActivityDto extends ActivitySimpleDto {



    /**
     * 活动须知
     */
    private String notice;
    /**
     * 活动内容
     */
    private String content;


    /**
     * 创建时间
     */
    private Date createDateTime; // 创建时间

    /**
     * 更新时间
     */
    private Date lastUpdateTime; // 最后修改时间

    /**
     * 活动页内容
     * @return
     */
    private String pageProfile;

    private String pageProfileMobile;


    public String getPageProfileMobile() {
        return pageProfileMobile;
    }

    public void setPageProfileMobile(String pageProfileMobile) {
        this.pageProfileMobile = pageProfileMobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getPageProfile() {
        return pageProfile;
    }

    public void setPageProfile(String pageProfile) {
        this.pageProfile = pageProfile;
    }

}