package com.sunesoft.seera.yc.core.activity.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;
import com.sunesoft.seera.fr.utils.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zwork on 2016/12/7.
 */
@Table(name ="share_activity")
@Entity
public class ShareActivity  extends BaseEntity{

    public ShareActivity(){
        setIsActive(true);
        setCreateDateTime(new Date());
        setLastUpdateTime(new Date());
        this.followUsers = new ArrayList<>();
    }

    private String openId;

    private String wxName;

    private String activityName;


    private String title;


    private String content;


    private String filePath;



    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "share_id")
    private  List<FollowUser>  followUsers;


    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public List<FollowUser> getFollowUsers() {
        return followUsers;
    }

    public void setFollowUsers(List<FollowUser> followUsers) {
        this.followUsers = followUsers;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
