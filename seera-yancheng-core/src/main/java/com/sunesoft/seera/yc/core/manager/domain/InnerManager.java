package com.sunesoft.seera.yc.core.manager.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;
import com.sunesoft.seera.fr.utils.MD5;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.uAuth.domain.SysRole;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 内部员工实体聚合根
 * Created by zhaowy on 2016/7/11.
 */
@Entity
@Table(name = "inner_manager")
public class InnerManager extends BaseEntity {

    /**
     * 员工状态，true 启用，false 禁用
     */
    @Column(name = "manager_status")
    private boolean status;

    /**
     * 用户名
     */
    @Column(name = "user_name", nullable = false)
    private String userName;

    /**
     * 用户password
     */
    @Column(name = "user_password")
    private String password;

    /**
     * 真实姓名
     */
    @Column(name = "real_name")
    private String realName;
    /**
     * 管理员联系号码
     */
    private String phone;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login_time")
    private Date lastLoginTime; // 账号最后登录时间
    /**
     * 账号登录次数
     */
    @Column(name = "login_count", columnDefinition = "int default 0")
    private int loginCount; // 账号登录次数
    /**
     *  角色项
     */

    /**
     *  角色项
     */
    @ManyToMany
    @JoinTable(name = "sys_manager_to_role", inverseJoinColumns = @JoinColumn(name = "role_id"), joinColumns = @JoinColumn(name = "manager_id"))
    private List<SysRole> userRoleList; // 角色项


    public InnerManager() {
        this.setIsActive(true);
        this.setStatus(true);
        this.setCreateDateTime(new Date());
        this.setLastUpdateTime(new Date());
        this.userRoleList=new ArrayList<>();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

//    private String getPassword() {
//        return password;
//    }

    public void setPassword(String password) {
        if(!StringUtils.isNullOrWhiteSpace(password))
        this.password = MD5.GetMD5Code(password);
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

    public List<SysRole> getUserRoleList() {
        return userRoleList;
    }

    public void setUserRoleList(List<SysRole> userRoleList) {
        this.userRoleList = userRoleList;
    }
}
