package com.sunesoft.seera.yc.core.uAuth.application.dtos;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zhouz on 2016/5/25.
 */
public class ResourceDto {


    public ResourceDto() {

    }

    private Long id;

    private int operate;// 0 ：新增 1 修改

    private String name; // 菜单名称

    private String url; // 菜单url地址

    private Integer resType;

    private String idCode; // 标识码

    private String target;//打开方式  _self  _blank  view_window

    private Long parentId; // 父菜单Id

    private String iconName; //图标名称


    private Integer sort; // 排序

    private List<ResourceDto> child;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public int getOperate() {
        return operate;
    }

    public Integer getResType() {
        return resType;
    }

    public void setResType(Integer resType) {
        this.resType = resType;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }

    public List<ResourceDto> getChild() {
        if(null!= child && !child.isEmpty())
        Collections.sort(child, new Comparator<ResourceDto>() {
            public int compare(ResourceDto arg0, ResourceDto arg1) {
                return arg0.getSort().compareTo(arg1.getSort());
            }
        });
        return child;
    }

    public void setChild(List<ResourceDto> child) {
        this.child = child;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

}
