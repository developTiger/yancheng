package com.sunesoft.seera.yc.core.activity.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by zwork on 2016/12/7.
 */
@Table(name ="follow_user")
@Entity
public class FollowUser extends BaseEntity {

    private String wxName;

    private String openId;




    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }


}
