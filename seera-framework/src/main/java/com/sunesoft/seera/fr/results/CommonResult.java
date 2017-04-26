package com.sunesoft.seera.fr.results;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhouz on 2016/5/18.
 */
public class CommonResult implements Serializable {

    private final Boolean isSuccess;

    private final String msg;

    private Long id;

    public CommonResult(Boolean isSuccess, String msg) {
        this.isSuccess = isSuccess;
        this.msg = msg;
    }


    public CommonResult(Boolean isSuccess, String msg,Long id) {
        this.isSuccess = isSuccess;
        this.msg = msg;
        this.id=id;
    }
    public CommonResult(Boolean isSuccess) {
        this.isSuccess = isSuccess;
        this.msg="";
    }



    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public String getMsg() {
        return msg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
