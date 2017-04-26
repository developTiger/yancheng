package com.sunesoft.seera.fr.results;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by zhouz on 2016/5/11.
 */
public class PagedCriteria implements Serializable {

    private int pageNumber = 1; // 去取第几页的数据

    private int pageSize = 10; //  每页多少条数据

    private Map<String,String> whereConditions;

    private String orderByProperty;

    private boolean ascOrDesc;//true, asc, false desc

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Map<String, String> getWhereConditions() {
        return whereConditions;
    }

    public void setWhereConditions(Map<String, String> whereConditions) {
        this.whereConditions = whereConditions;
    }

    public String getOrderByProperty() {
        return orderByProperty;
    }

    public void setOrderByProperty(String orderByProperty) {
        this.orderByProperty = orderByProperty;
    }

    public boolean isAscOrDesc() {
        return ascOrDesc;
    }

    public void setAscOrDesc(boolean ascOrDesc) {
        this.ascOrDesc = ascOrDesc;
    }

}
