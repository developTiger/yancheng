package com.sunesoft.seera.yc.pwb;

import com.sunesoft.seera.yc.Response;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by bing on 16/7/27.
 */
public class PwbResponse extends Response {

    /**
     * 事务名称.
     */
    protected String transactionName;

    /**
     * 编码.
     */
    protected Integer code;

    /**
     * 描述.
     */
    protected String description;

    public PwbResponse(Class<?> objectClass) {
        super(objectClass);
    }

    @XmlElement(name = "transactionname")
    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
