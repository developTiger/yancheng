package com.sunesoft.seera.yc.obj;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by bing on 16/8/4.
 */
public class Order {

    private String linkName;

    @XmlElement
    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }
}
