package com.sunesoft.seera.yc.core.uAuth.application;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.uAuth.application.criteria.RoleCriteria;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.RoleDto;
import com.sunesoft.seera.yc.core.uAuth.application.criteria.RoleCriteria;

import java.util.List;

/**
 * Created by xiazl on 2016/5/26.
 */
public interface SysRoleService {

    /**
     * 增加角色
     * @param role
     * @return
     */
    public Long addRole(RoleDto role);


    /**
     * 根据id删除角色
     * @param ids
     * @return
     */
    public Boolean deleteRole(Long[] ids);


    /**
     * 修改角色
     * @param dto
     * @return
     */
    public Long updateRole(RoleDto dto);
    /**
     * 根据id获取角色
     * @param id
     * @return
     */
    public RoleDto getRoleById(Long id);
    /**
     * 保存
     * @param role
     * @return
     */
    public Long save(RoleDto role);
    /**
     * 数据传输
     * @param dto
     * @return
     */
    public Long addOrUpdate(RoleDto dto);
    /**
     * 获取所用角色
     * @return
     */
    public List<RoleDto> getAllRole();
    /**
     * 分页
     * @param criteria
     * @return
     */
    public PagedResult<RoleDto> getRolePage(RoleCriteria criteria);

    /**
     * 获取所用角色
     *  @param roleName
     * @return
     */
    public List<RoleDto> getAllRole(String roleName);
}
