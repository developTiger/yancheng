package com.sunesoft.seera.yc.core.parameter.application.criteria;

import com.sunesoft.seera.fr.results.PagedCriteria;
import com.sunesoft.seera.yc.core.parameter.domain.Parameter;
import com.sunesoft.seera.yc.core.parameter.domain.Parameter;

import java.util.List;

/**
 * Created by zy on 2016/6/2.
 */

public class ParameterTypeCriteria  extends PagedCriteria {

    private String paramTypeName; //参数类型

    private String paramDesc;//参数描述

    private List<Parameter> parameters;//参数列表

    //以下get,set方法
    public String getParamTypeName() {
        return paramTypeName;
    }

    public void setParamTypeName(String paramTypeName) {
        this.paramTypeName = paramTypeName;
    }

    public String getParamDesc() {
        return paramDesc;
    }

    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
