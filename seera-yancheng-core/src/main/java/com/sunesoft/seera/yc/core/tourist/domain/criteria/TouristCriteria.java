package com.sunesoft.seera.yc.core.tourist.domain.criteria;

import com.sunesoft.seera.fr.results.PagedCriteria;
import com.sunesoft.seera.yc.core.tourist.domain.TouristStatus;

import java.util.Date;

/**
 * Created by zhaowy on 2016/7/14.
 */
public class TouristCriteria extends PagedCriteria {

    // region Filed
    /**
     * 用户名 支持userName|wxName|mobilePhone|email|realName
     */
    private String token;

    /**
     * 游客状态
     */
    private TouristStatus status;


    /**
     * 注册时间起始
     */
    private Date fromRegisterDate;

    /**
     * 注册时间截止
     */
    private Date endRegisterDate;

    //endregion

    //region Property Get&Set


    public TouristStatus getStatus() {
        return status;
    }

    public void setStatus(TouristStatus status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public Date getEndRegisterDate() {
        return endRegisterDate;
    }

    public void setEndRegisterDate(Date endRegisterDate) {
        this.endRegisterDate = endRegisterDate;
    }

    public Date getFromRegisterDate() {
        return fromRegisterDate;
    }

    public void setFromRegisterDate(Date fromRegisterDate) {
        this.fromRegisterDate = fromRegisterDate;
    }

    //endregion
}
