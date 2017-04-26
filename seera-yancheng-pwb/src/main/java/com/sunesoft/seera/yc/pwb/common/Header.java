package com.sunesoft.seera.yc.pwb.common;

import java.io.Serializable;

/**
 * Created by bing on 16/7/27.
 */
public class Header implements Serializable {
    
    private String application;

    private String requestTime;

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }
}