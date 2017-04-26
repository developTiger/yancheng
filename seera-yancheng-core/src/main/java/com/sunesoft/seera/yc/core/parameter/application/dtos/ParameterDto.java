package com.sunesoft.seera.yc.core.parameter.application.dtos;


/**
 * Created by zy on 2016/6/2.
 */
public class ParameterDto{
    private Long id;   //id

    private String paramTypeName;  // 父类字段名

    private Long  paramTypeId;  //父类id

    private String paramName;//参数名称

    private String paramValue;//值

    private String paramDesc;//描述

    private String remark;//备注

    private String attrbute1;//扩展1

    private String attrbute2;//扩展2

    //private String attrbute3;//扩展3

   // private String attrbute4;//扩展4


    //以下get,set方法

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id;}

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

    public String getAttrbute1() {  return attrbute1; }

    public void setAttrbute1(String attrbute1) {  this.attrbute1 = attrbute1;}

    public String getAttrbute2() {  return attrbute2; }

    public void setAttrbute2(String attrbute2) {   this.attrbute2 = attrbute2; }

    public String getParamTypeName() {
        return paramTypeName;
    }

    public void setParamTypeName(String paramTypeName) {
        this.paramTypeName = paramTypeName;
    }

    public Long getParamTypeId() {return paramTypeId;}

    public void setParamTypeId(Long paramTypeId) {this.paramTypeId = paramTypeId;}

    @Override
    public String toString() {
        return "ParameterDto{" +
                "id=" + id +
                ", paramTypeName='" + paramTypeName + '\'' +
                ", paramName='" + paramName + '\'' +
                ", paramValue='" + paramValue + '\'' +
                ", paramDesc='" + paramDesc + '\'' +
                ", remark='" + remark + '\'' +
                ", attrbute1='" + attrbute1 + '\'' +
                ", attrbute2='" + attrbute2 + '\'' +
                '}';
    }
}
