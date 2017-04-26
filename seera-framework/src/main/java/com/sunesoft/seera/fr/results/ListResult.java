package com.sunesoft.seera.fr.results;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhouz on 2016/5/18.
 */
public class ListResult <T> implements Serializable {

    private final List<T> items;

    private final Boolean isSuccess;

    private final String msg;


    public ListResult(List<T> list){
        this.items = list;
        isSuccess =true;
        msg="";

    }

    public ListResult(String errorMsg){
        items = Collections.emptyList();
        isSuccess=false;
        msg = errorMsg;
    }

    public List<T> getItems() {
        return items;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public String getMsg() {
        return msg;
    }


}
