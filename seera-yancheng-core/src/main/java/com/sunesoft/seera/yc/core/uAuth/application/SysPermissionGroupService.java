package com.sunesoft.seera.yc.core.uAuth.application;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.uAuth.application.criteria.PermissionGroupCriteria;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.PermissionGroupDto;
import com.sunesoft.seera.yc.core.uAuth.domain.SysPermissionGroup;

import java.util.List;

/**
 * Created by jiangkefan on 2016/5/26.
 */

public interface SysPermissionGroupService {

    /**
     * 新增权限
     * @param sysPermissionGroup
     * @return
     */
    public Long add(SysPermissionGroup sysPermissionGroup);



    /**
     * 查询权限
     * @param id
     * @return
     */
    public SysPermissionGroup getById(Long id);
    /**
     * 查询权限
     * @param id
     * @return
     */
    public PermissionGroupDto getDtoById(Long id);
    /**
     * 删除权限
     * @param ids
     * @return
     */
    public boolean delete(Long[] ids);

    /**
     * 更新权限
     * @param dto
     * @return
     */
    public Long addOrUpdate(PermissionGroupDto dto);

    /**
     * @param permissionGroupDto
     * @return
     */
    public boolean update(PermissionGroupDto permissionGroupDto);

//    /**
//     * 展示
//     * @param criteria
//     * @return
//     */
//    public PagedResult<SysPermissionGroup> getPermissionGroupPaged(PermissionGroupCriteria criteria);



    /**
     * 得到 SysPermissionGroup。。。。
     * @return
     */
    public List<PermissionGroupDto> getAllPermissionGroupNames();


    /**
     * 展示
     * @param groupName
     * @return
     */
    public List<PermissionGroupDto> getPermissionGroup(String groupName);

    /**
     * 展示
     * @param criteria
     * @return
     */
    public PagedResult<PermissionGroupDto> getPermissionGroupPaged(PermissionGroupCriteria criteria);



}
