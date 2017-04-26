package com.sunesoft.seera.yc.core.picture.domain.criteria;

import com.sunesoft.seera.fr.results.PagedCriteria;
import com.sunesoft.seera.yc.core.picture.domain.PictureType;

/**
 * Created by xiazl on 2016/9/2.
 */
public class PictureCriteria extends PagedCriteria {


    /**
     * 图片路径
     */
    private String path;

    /**
     * 图片用途类型
     */
    private PictureType type;

    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public PictureType getType() {
        return type;
    }

    public void setType(PictureType type) {
        this.type = type;
    }
}
