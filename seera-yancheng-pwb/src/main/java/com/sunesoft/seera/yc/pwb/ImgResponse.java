package com.sunesoft.seera.yc.pwb;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by bing on 16/7/27.
 */
@XmlRootElement(name = "PWBResponse")
public class ImgResponse extends PwbResponse {

    private String img;

    public ImgResponse() {
        super(ImgResponse.class);
    }

    @XmlElement(name = "img")
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}