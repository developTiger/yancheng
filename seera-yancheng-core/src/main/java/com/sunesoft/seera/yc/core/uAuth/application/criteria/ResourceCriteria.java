package com.sunesoft.seera.yc.core.uAuth.application.criteria;

import com.sunesoft.seera.fr.results.PagedCriteria;

/**
 * Created by zhouz on 2016/5/19.
 */
public class ResourceCriteria extends PagedCriteria{

    public ResourceCriteria(){
        resType=1;
    }

    private String menuName;

    private Integer resType;


    public Integer getResType() {
        return resType;
    }

    public void setResType(Integer resType) {
        this.resType = resType;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
