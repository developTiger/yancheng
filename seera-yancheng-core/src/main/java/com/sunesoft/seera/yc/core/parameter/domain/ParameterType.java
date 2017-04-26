package com.sunesoft.seera.yc.core.parameter.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by zhouz on 2016/6/1.
 */
@Entity
@Table(name="parameter_type")
public class ParameterType extends BaseEntity {

    @Column(name = "param_type_name")
    private String paramTypeName; //参数类型

    @Column(name="param_desc")
    private String paramDesc;//参数描述

    @OneToMany
    @JoinColumn(name = "param_type_id")
    private List<Parameter> parameters;//参数列表

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

    @Override
    public String toString() {
        return "ParameterType{" +
                "paramTypeName='" + paramTypeName + '\'' +
                ", paramDesc='" + paramDesc + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
