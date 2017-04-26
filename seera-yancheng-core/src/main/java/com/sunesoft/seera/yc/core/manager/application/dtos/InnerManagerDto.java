package com.sunesoft.seera.yc.core.manager.application.dtos;

import com.sunesoft.seera.yc.core.uAuth.application.dtos.RoleDto;

import java.util.Date;
import java.util.List;

/**
 * 管理员的传输entity
 * Created by xiazl on 2016/7/13.
 */
public class InnerManagerDto {

    private Long id;
    /**
     * 登录密码
     */
    private String password;


    /**
     * 用户名
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 管理员联系号码
     */
    private String phone;

    /**
     * 创建时间
     */
    private Date createDateTime; // 创建时间

    /**
     * 最后修改时间
     */
    private Date lastUpdateTime; // 最后修改时间

    /**
     * 上次登录时间
     */
    private Date lastLoginTime; // 账号最后登录时间

    /**
     * 是否禁用
     */
    private Boolean status;
    /**
     * 账号登录次数
     */
    private int loginCount; // 账号登录次数

    /**
     * 角色项
     */
    private List<RoleDto> userRoleListDto; // 角色项


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
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

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<RoleDto> getUserRoleListDto() {
        return userRoleListDto;
    }

    public void setUserRoleListDto(List<RoleDto> userRoleListDto) {
        this.userRoleListDto = userRoleListDto;
    }
}
