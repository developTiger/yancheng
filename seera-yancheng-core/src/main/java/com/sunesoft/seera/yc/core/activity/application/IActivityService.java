package com.sunesoft.seera.yc.core.activity.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.activity.application.dtos.ActivityDto;
import com.sunesoft.seera.yc.core.activity.application.dtos.ActivitySimpleDto;
import com.sunesoft.seera.yc.core.activity.domain.ActivityStatus;
import com.sunesoft.seera.yc.core.activity.domain.creteria.ActivityCriteria;

import java.util.List;

/**
 * 活动服务接口
 * Created by xiazl on 2016/8/6.
 */
public interface IActivityService {
    /**
     * 增加活动
     * @param dto
     * @return
     */
    public CommonResult create(ActivityDto dto);

    /**
     * 修改活动
     * @param dto
     * @return
     */
    public CommonResult edit(ActivityDto dto);

    /**
     * 移除活动
     * @param ids
     * @return
     */
    public CommonResult remove(List<Long> ids);

    /**
     * 查询某个活动
     * @param id
     * @return
     */
    public ActivityDto getById(Long id);

    /**
     * 查询某个活动
     * @param id
     * @return
     */
    public ActivitySimpleDto getSimpleById(Long id);

    /**
     * 查询多个活动
     * @param ids
     * @return
     */
    public List<ActivityDto> getByIds(List<Long> ids);

    /**
     * 查询多个活动
     * @param ids
     * @return
     */
    public List<ActivitySimpleDto> getSimpleByIds(List<Long> ids);

    /**
     * 活动的分页处理
     * @param criteria
     * @return
     */
    public PagedResult<ActivityDto> findPage(ActivityCriteria criteria);

    /**
     * 活动的分页处理
     * @param criteria
     * @return
     */
    public PagedResult<ActivitySimpleDto> findSimplePage(ActivityCriteria criteria);

//    /**
//     * 正在进行中的活动
//     * @return
//     */
//    public List<ActivityDto> beingActiving();
//
//    /**
//     * 已经结束的活动
//     * @return
//     */
//    public List<ActivityDto> postActived();

    /**
     * 改变活动状态
     * @param id
     * @param status
     * @return
     */
    CommonResult  changeActivityStatuss(Long id ,ActivityStatus status);

    /**
     * 获取所有活动
     * @return
     */
    public List<ActivityDto> getAll();

    public List<ActivityDto> getActivityByProductId(Long id);

    CommonResult updatePage(ActivityDto activityDto);
}
