package com.sunesoft.seera.yc.core.manager.application.criteria;

import com.sunesoft.seera.fr.results.PagedCriteria;

/**
 * Created by zhouz on 2016/5/19.
 */
public class ManagerCriteria extends PagedCriteria {

    private String userName;


    private String phone;

    private Boolean status;


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
