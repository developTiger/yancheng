package com.sunesoft.seera.yc.core.picture.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;

import javax.persistence.Entity;

/**
 * Created by xiazl on 2016/9/2.
 */
@Entity
public class Picture extends BaseEntity{

    /**
     * 图片使用的页面
     */
    private String location;

    /**
     * 图片使用的页面位置
     * 从上而下，从左到右，从0开始数
     */
    private Integer position;
    /**
     * 图片名
     */
    private String name;
    /**
     * 图片的超链接
     */
    private String link;
    /**
     * 图片路径
     */
    private String path;

    /**
     * 图片用途类型
     */
    private PictureType type;

    /**
     * 图片描述
     */
    private String description;

    /**
     * 图片状态
     */
    private Boolean pictureStatus;

    public Picture() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public PictureType getType() {
        return type;
    }

    public void setType(PictureType type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getPictureStatus() {
        return pictureStatus;
    }

    public void setPictureStatus(Boolean pictureStatus) {
        this.pictureStatus = pictureStatus;
    }
}
