package com.sunesoft.seera.yc.core.parameter.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zhouz on 2016/6/1.
 */
@Entity
@Table(name = "parameter")
public class Parameter extends BaseEntity {

    @Column(name="param_name")
    private String paramName;//参数名称

    @Column(name ="param_value")
    private String paramValue;//值

    @Column(name = "param_desc")
    private String paramDesc;//描述

    @ManyToOne
    @JoinColumn(name = "param_type_id")
    private ParameterType parameterType;

    private String remark;//备注

    private String attrbute1;//扩展1

    private String attrbute2;//扩展2

    private String attrbute3;//扩展3

    private String attrbute4;//扩展4

    private String attrbute5;//扩展5

    public Parameter() {
        this.setIsActive(true);
        this.setLastUpdateTime(new Date());
        this.setCreateDateTime(new Date());
    }


    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue(String paramValue) {
        return this.paramValue;
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

    public String getAttrbute1() {return attrbute1;}

    public void setAttrbute1(String attrbute1) {this.attrbute1 = attrbute1;}

    public String getAttrbute2() {return attrbute2;}

    public void setAttrbute2(String attrbute2) {this.attrbute2 = attrbute2;}

    public String getAttrbute3() {
        return attrbute3;
    }

    public void setAttrbute3(String attrbute3) {
        this.attrbute3 = attrbute3;
    }

    public String getAttrbute4() {
        return attrbute4;
    }

    public void setAttrbute4(String attrbute4) {
        this.attrbute4 = attrbute4;
    }

    public String getAttrbute5() {
        return attrbute5;
    }

    public void setAttrbute5(String attrbute5) {this.attrbute5 = attrbute5;}

    public String getParamValue() {return paramValue;}

    public ParameterType getParameterType() {
        return parameterType;
    }

    public void setParameterType(ParameterType parameterType) {
        this.parameterType = parameterType;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "paramName='" + paramName + '\'' +
                ", paramValue='" + paramValue + '\'' +
                ", paramDesc='" + paramDesc + '\'' +
                ", parameterType=" + parameterType +
                ", remark='" + remark + '\'' +
                ", attrbute1='" + attrbute1 + '\'' +
                ", attrbute2='" + attrbute2 + '\'' +
                ", attrbute3='" + attrbute3 + '\'' +
                ", attrbute4='" + attrbute4 + '\'' +
                ", attrbute5='" + attrbute5 + '\'' +
                '}';
    }
}
