package com.sunesoft.seera.yc;

import com.sunesoft.seera.yc.jaxb.JaxbBase;

import java.io.Serializable;

/**
 * Created by bing on 16/7/27.
 */
public abstract class Response extends JaxbBase implements Serializable {
    public Response(Class<?> objectClass) {
        super(objectClass);
    }
}
