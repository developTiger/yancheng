package com.sunesoft.seera.yc.core.uAuth.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.uAuth.application.criteria.UserCriteria;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.UserDto;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.UserSessionDto;

import java.util.List;

/**
 * Created by zhouz on 2016/5/19.
 */
public interface SysUserService {

    /**
     * add or update用户
     *
     * @param user 用户信息
     * @return Boolean
     */
    public CommonResult addorUpdateUser(UserDto user);

    /**
     * wxoauth
     * @param userName
     * @param password
     * @return
     */
    public UserSessionDto login(String userName,String password);


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
     * @param ids
     * @param status
     * @return
     */
    public CommonResult setUserStatus(List<Long> ids, int status);


    /**
     * 查询实例
     *
     * @param criteria 查询条件
     * @return
     */
    public PagedResult<UserDto> FindUser(UserCriteria criteria);



    public CommonResult delete(List<Long> ids);


    /**
     * 获取所用用户
     *
     * @return
     */
    public List<UserDto> getAllUser();

    /**
     * 根据id获取用户
     *
     * @param id
     * @return
     */
    public UserDto getById(Long id);



    /**
     * 获取所用用户
     * @ param userName
     * @return
     */
    public List<UserDto> getAllUser(String userName);

    /**
     *
     * @param uid
     * @return
     */
    UserSessionDto GetUserSessionDtoById(long uid);
}
