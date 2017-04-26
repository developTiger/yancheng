package com.sunesoft.seera.fr.results;

import java.io.Serializable;

/**
 * Created by zhouz on 2016/5/18.
 */
public class UniqueResult<T> implements Serializable {

    private final T t;

    private final Boolean isSuccess;

    private final String msg;

    public UniqueResult(T t) {
        this.t = t;
        this.isSuccess = true;
        this.msg = "";
    }

    public UniqueResult(String errorMsg) {
        t = null;
        this.msg = errorMsg;
        this.isSuccess = false;
    }

    public T getT() {
        return t;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public String getMsg() {
        return msg;
    }
}
