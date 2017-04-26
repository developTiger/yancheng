package com.sunesoft.seera.yc.core.uAuth.application.dtos;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by jiangkefan on 2016/5/26.
 */
public class PermissionGroupDto {

    private Long id;

    private Date createDatetime; //创建时间

    private boolean isActive ; // 是否有下级

    private Date lastUpdateTimes; //最后更新时间

    private String name ; //姓名

    private Integer sort ;  //排序

    private List<Long> menuIds; //下拉菜单


    private List<PermissionGroupDto> child;

    public List<PermissionGroupDto> getChild() {
        return child;
    }

    public void setChild(List<PermissionGroupDto> child) {
        this.child = child;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


    public Date getLastUpdateTimes() {
        return lastUpdateTimes;
    }

    public void setLastUpdateTimes(Date lastUpdateTimes) {
        this.lastUpdateTimes = lastUpdateTimes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Long> menuIds) {
        this.menuIds = menuIds;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
}
