package com.sunesoft.seera.yc.core.parameter.application.criteria;

import com.sunesoft.seera.fr.results.PagedCriteria;

/**
 * Created by zy on 2016/6/2.
 */
public class ParameterCriteria  extends PagedCriteria {

    private String paramName;//参数名称

    private String paramValue;//值

    private String paramDesc;//描述

    private String remark;//备注

   // private String attrbute1;//扩展1

   // private String attrbute2;//扩展2

    //private String attrbute3;//扩展3

   // private String attrbute4;//扩展4


    //以下get,set方法
    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getParamDesc() {
        return paramDesc;
    }

    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


}
