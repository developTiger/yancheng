package com.sunesoft.seera.yc.core.manager.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.manager.application.criteria.ManagerCriteria;
import com.sunesoft.seera.yc.core.manager.application.dtos.InnerManagerDto;
import com.sunesoft.seera.yc.core.manager.application.dtos.ManagerSessionDto;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.ResourceDto;

import java.util.List;
import java.util.Map;

/**
 * 员工服务接口
 * Created by zhaowy on 2016/7/11.
 */
public interface IManagerService {

    /**
     * add用户
     *
     * @param dto 用户信息
     * @return Boolean
     */
    public CommonResult addUser(InnerManagerDto dto) throws Exception;

    /**
     *  update用户
     *
     * @param dto 用户信息
     * @return Boolean
     */
    public CommonResult updateUser(InnerManagerDto dto);

    /**
     * wxoauth
     * @param userName
     * @param password
     * @return
     */
    public ManagerSessionDto login(String userName,String password);


    /**
     * change password
     * @param id
     * @param Password
     * @return
     */

    public CommonResult changePassword(Long id, String Password);

    /**
     * 设置用户状态
     *禁用或者为禁用
     * @param id
     * @param status
     * @return
     */
    public CommonResult setStatus(Long id, boolean status);


    /**
     * 设置用户状态
     *禁用或者为禁用
     * @param ids
     * @param status
     * @return
     */
    public CommonResult setUserStatus(List<Long> ids, boolean status);


    /**
     * 查询实例
     *
     * @param criteria 查询条件
     * @return
     */
    public PagedResult<InnerManagerDto> findUser(ManagerCriteria criteria);


    /**
     * 多项选中删除
     * @param ids
     * @return
     */
    public CommonResult delete(List<Long> ids);


    /**
     * 获取所用用户
     *
     * @return
     */
    public List<InnerManagerDto> getAllUser();

    /**
     * 根据id获取用户
     *
     * @param id
     * @return
     */
    public InnerManagerDto getById(Long id);



    /**
     * 获取所用用户
     * @ param userName
     * @return
     */
    public List<InnerManagerDto> getAllUser(String userName);

    /**
     * 获取可以作为leader的所用用户
     * @ param staffId
     * @return
     */
    public List<InnerManagerDto> getLeaders(Long staffId);

    /**
     *获取登录信息
     * @param uid
     * @return
     */
    ManagerSessionDto GetUserSessionDtoById(long uid);

    /**
     * 设置管理员角色
     * @param managerId 管理员标识
     * @param roleId 角色标识
     * @return
     */
    CommonResult SetManagerRole(Long managerId,Long roleId);

    Map<Long,List<ResourceDto>> getAllAuthInfoByRole();

    CommonResult checkPwd(Long id, String oldPwd);
}
