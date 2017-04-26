package com.sunesoft.seera.yc.core.uAuth.application.criteria;

import com.sunesoft.seera.fr.results.PagedCriteria;

/**
 * Created by xiazl on 2016/5/26.
 */
public class RoleCriteria extends PagedCriteria {

    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
