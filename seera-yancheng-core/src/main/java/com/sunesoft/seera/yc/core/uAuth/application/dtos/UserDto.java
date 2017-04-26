package com.sunesoft.seera.yc.core.uAuth.application.dtos;


import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.uAuth.domain.entityEnum.SysEnum;

import java.util.Date;
import java.util.List;


/**
 * Created by xiazl on 2016/5/25.
 */
public class UserDto {
    /**
     * 用户id
     */
    private Long id;//用户id
    /**
     *  0 ：新增 1 禁用 2 启用
     */
    private int operate;// 0 ：新增 1 禁用 2 启用


    /**
     * 用户类型
     */
    private SysEnum type;
    /**
     *  0代表未禁用,1代表禁用
     */
    private int status; // 0代表未禁用,1代表禁用
    /**
     * 等级
     */
    private int levels; // 等级
    /**
     * 登录用户帐号
     */
    private String loginName; // 登录用户帐号
/**
 * 真实姓名
 */
    private String name; // 真实姓名
    /**
     * 登录密码
     */
    private String password; // 登录密码
    /**
     * 加密规则
     */
    private String alt; // 加密规则
    /**
     * 手机
     */
    private String mobile; // 手机
    /**
     * 邮箱
     */
    private String email; // 邮箱
    /**
     * qq
     */
    private String qq; // qq
    /**
     * 座机
     */
    private String phone; // 座机
    /**
     * 照片
     */
    private String photo; // 照片
    /**
     *  简介
     */
    private String brief; // 简介
    /**
     *  创建时间
     */
    private Date createDateTime; // 创建时间
    /**
     * 最后修改时间
     */
    private Date lastUpdateTime; // 最后修改时间
    /**
     * 账号最后登录时间
     */
    private Date lastLoginTime; // 账号最后登录时间
    /**
     * 账号登录次数
     */
    private int loginCount; // 账号登录次数
    /**
     * 角色项
     */
    private List<RoleDto> userRoleList; // 角色项

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getLevels() {
        return levels;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public SysEnum getType() {
        return type;
    }

    public void setType(SysEnum type) {
        this.type = type;
    }

    public List<RoleDto> getUserRoleList() {
        return userRoleList;
    }

    public void setUserRoleList(List<RoleDto> userRoleList) {
        this.userRoleList = userRoleList;
    }
}
