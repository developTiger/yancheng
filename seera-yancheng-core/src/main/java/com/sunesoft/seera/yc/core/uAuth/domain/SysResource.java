package com.sunesoft.seera.yc.core.uAuth.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by zhouz on 2016/5/19.
 */
@Entity
@Table(name = "sys_resource")
public class SysResource extends BaseEntity {


    public SysResource() {
        setIsActive(true);
    }

    /**
     *  1: menu   2:form  3:filepath
     */
    @Column(name = "resource_type")
    private int resourceType; // 1: menu   2:form  3:filepath
    /**
     * 菜单名称
     */
@Column(name = "res_name")
    private String name; // 菜单名称
/**
 * 菜单url地址
 */
    private String url; // 菜单url地址
    /**
     *  标识码
     */
    @Column(name = "id_code")
    private String idCode; // 标识码

    private Boolean isRoot;
    /**
     * 父菜单
     */
    @ManyToOne
    @JoinColumn(name = "parent_res_id")
    private SysResource parentResource; // 父菜单
    /**
     * 图标名称
     */
    @Column(name = "icon_name")
    private String iconName; //图标名称
    /**
     *  排序
     */
    private Integer sort; // 排序

   // private Boolean hasChild; // 1.有下级，2。没有下级

    /**
     * 子菜单
     */
    @OneToMany
    @JoinColumn(name = "parent_res_id")
    private List<SysResource> childResource; // 子菜单

    public List<SysResource> getChildResource() {
        return childResource;
    }

    public void setChildResource(List<SysResource> childResource) {
        this.childResource = childResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public SysResource getParentResource() {
        return parentResource;
    }

    public void setParentResource(SysResource parentResource) {
        this.parentResource = parentResource;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }


    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }


    public int getResourceType() {
        return resourceType;
    }

    public void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }


    public Boolean getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(Boolean isRoot) {
        this.isRoot = isRoot;
    }
}

