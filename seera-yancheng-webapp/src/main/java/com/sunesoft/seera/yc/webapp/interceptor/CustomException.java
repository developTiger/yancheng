package com.sunesoft.seera.yc.webapp.interceptor;

/**
 * Created by temp on 2016/9/26.
 */
public class CustomException  extends Exception {

    private String message;

    public CustomException(){}

    public CustomException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}