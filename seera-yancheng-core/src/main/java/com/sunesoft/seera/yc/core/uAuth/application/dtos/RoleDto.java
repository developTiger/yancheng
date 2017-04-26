package com.sunesoft.seera.yc.core.uAuth.application.dtos;

import java.util.Date;
import java.util.List;

/**
 * Created by xiazl on 2016/5/26.
 */
public class RoleDto {

    private Long id;

    private int operate;// 0 ：新增 1 删除

    private String name;//系统角色名称

    private String idCode;//标识码

    private String description;//描述

    private Double sort;//排序

    private Date lastUpdateTime; // 最后修改时间

    private Date createDateTime; // 创建时间

    private List<PermissionGroupDto> privilegeGroupListDtos; // 角色下的权限组

    public RoleDto() {
        this.lastUpdateTime=new Date();
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

    public List<PermissionGroupDto> getPrivilegeGroupListDtos() {
        return privilegeGroupListDtos;
    }

    public void setPrivilegeGroupListDtos(List<PermissionGroupDto> privilegeGroupListDtos) {
        this.privilegeGroupListDtos = privilegeGroupListDtos;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getSort() {
        return sort;
    }

    public void setSort(Double sort) {
        this.sort = sort;
    }


}
