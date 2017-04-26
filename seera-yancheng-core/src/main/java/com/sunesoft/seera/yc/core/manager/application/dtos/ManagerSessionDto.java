package com.sunesoft.seera.yc.core.manager.application.dtos;

/**
 * Created by xiazl on 2016/7/13.
 */
public class ManagerSessionDto {
    /**
     * 用户id
     */
    private Long id;//用户id

    /**
     * 用户名
     */
    private String userName;



    /**
     * 用户状态
     */
    private boolean status;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 管理员联系号码
     */
    private String phone;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
