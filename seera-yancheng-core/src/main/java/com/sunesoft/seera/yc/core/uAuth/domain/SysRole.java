package com.sunesoft.seera.yc.core.uAuth.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by zhouz on 2016/5/19.
 */
@Entity
@Table(name = "sys_role")
public class SysRole extends BaseEntity {
    /**
     * 系统角色名称
     */
    @Column(name = "role_name")
    private String name; // 系统角色名称
    /**
     * 标识码
     */
    @Column(name = "id_code")
    private String idCode; // 标识码
    /**
     * 描述
     */
    private String description; // 描述
    /**
     * 排序
     */
    private double sort; // 排序

    @ManyToMany
    @JoinTable(name = "sys_role_permission_group", inverseJoinColumns = @JoinColumn(name = "sys_permitgroup_id"), joinColumns = @JoinColumn(name = "user_role_id"))
    private List<SysPermissionGroup> privilegeGroupList; // 角色下的权限组

    public List<SysPermissionGroup> getPrivilegeGroupList() {
        return privilegeGroupList;
    }

    public void setPrivilegeGroupList(List<SysPermissionGroup> privilegeGroupList) {
        this.privilegeGroupList = privilegeGroupList;
    }

    public SysRole() {
        this.setIsActive(true);
        this.setCreateDateTime(new Date());
        this.setLastUpdateTime(new Date());
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


    public double getSort() {
        return sort;
    }

    public void setSort(double sort) {
        this.sort = sort;
    }

}
