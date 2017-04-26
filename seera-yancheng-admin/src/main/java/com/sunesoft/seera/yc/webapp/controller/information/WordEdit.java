package com.sunesoft.seera.yc.webapp.controller.information;

import java.util.Date;

/**
 * Created by admin on 2016/7/21.
 */
public class WordEdit {


    private String num;//编号
    private String problemType;//问题类别
    private String title;//文章标题
    private Date updateTime;//更新时间
    private String person;//发布人
    private String authority;//权限
    private String count;//浏览次数

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
