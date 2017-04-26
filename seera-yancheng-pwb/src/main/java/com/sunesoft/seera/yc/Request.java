package com.sunesoft.seera.yc;

import com.sunesoft.seera.yc.jaxb.JaxbBase;

import java.lang.reflect.ParameterizedType;

/**
 * Created by bing on 16/7/27.
 */
public abstract class Request<T extends Response> extends JaxbBase {

    protected Class<T> responseClass;

    public Request(Class<?> objectClass) {
        super(objectClass);
        responseClass = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    public Class<T> getResponseClass() {
        return this.responseClass;
    }
}
