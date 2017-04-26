package com.sunesoft.seera.yc.core.activity.application;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateFinder;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.activity.application.Activityfactory.Activityfactory;
import com.sunesoft.seera.yc.core.activity.application.dtos.ActivityDto;
import com.sunesoft.seera.yc.core.activity.application.dtos.ActivitySimpleDto;
import com.sunesoft.seera.yc.core.activity.domain.Activity;
import com.sunesoft.seera.yc.core.activity.domain.ActivityStatus;
import com.sunesoft.seera.yc.core.activity.domain.IActivityRepository;
import com.sunesoft.seera.yc.core.activity.domain.creteria.ActivityCriteria;

import com.sunesoft.seera.yc.core.parameter.application.factory.DtoFactory;
import com.sunesoft.seera.yc.core.product.domain.IProductRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by xiazl on 2016/8/6.
 */
@Service("iActivityService")
public class ActivityServiceImpl extends GenericHibernateFinder implements IActivityService {

    @Autowired
    IActivityRepository repository;

    @Autowired
    IProductRepository productRepository;

    /**
     * 增加活动
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult create(ActivityDto dto) {
        if (dto.getStartTime() == null || (dto.getEndTime() != null && !dto.getStartTime().before(dto.getEndTime())))
            return ResultFactory.commonError("please set right startTime");
        if(repository.check(dto.getName()))
            return ResultFactory.commonError("该活动已经存在，正在进行或即将进行");
        Activity activity = Activityfactory.convert(dto);
        activity.setProduct(productRepository.get(dto.getProductDto().getId()));

        return ResultFactory.commonSuccess(repository.save(activity));
    }

    /**
     * 修改活动
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult edit(ActivityDto dto) {
        if (dto.getStartTime() != null && dto.getEndTime() != null && !dto.getStartTime().before(dto.getEndTime()))
            return ResultFactory.commonError("起止时间设置不合理额");
        Activity ac=repository.get(dto.getId());
        if(!dto.getName().equals(ac.getName())&&repository.check(dto.getName())){
            return ResultFactory.commonError("修改后的活动已经存在，正在进行或即将进行");
        }
        if (ActivityStatus.Run == dto.getActivityStatus()) {
            if (!dto.getEndTime().after(new Date())) {
                return new CommonResult(false, "活动已过期，请修改活动时间后运行！");

            }
            if (dto.getStartTime().after(new Date())) {
                return new CommonResult(false, "活动尚未到开始时间，请修改活动时间后运行！");
            }
        }
        Activity activity = repository.get(dto.getId());
        if (activity == null || !activity.getIsActive()) return ResultFactory.commonError("该活动不存在");
        try {
            activity = DtoFactory.convert(dto, activity);
            activity.setProduct(productRepository.get(dto.getProductDto().getId()));
            return ResultFactory.commonSuccess(repository.save(activity));
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }
    }

    /**
     * 移除活动
     *
     * @param ids
     * @return
     */
    @Override
    public CommonResult remove(List<Long> ids) {
        Criteria criterion = getSession().createCriteria(Activity.class);
        criterion.add(Restrictions.eq("isActive", true));
        criterion.add(Restrictions.in("id", ids));
        List<Activity> list = criterion.list();
        if (list != null && list.size() > 0) {
//            list.stream().forEach(i->repository.delete(i.getId()));
            list.stream().forEach(i -> {
                i.setIsActive(false);
                repository.save(i);
            });
            return ResultFactory.commonSuccess();
        }
        return ResultFactory.commonError("请选择活动项");
    }

    /**
     * 查询某个活动
     *
     * @param id
     * @return
     */
    @Override
    public ActivityDto getById(Long id) {
        Activity activity = repository.get(id);
        if (activity != null || activity.getIsActive())
            return Activityfactory.convert(activity);
        return null;
    }

    /**
     * 查询个活动
     *
     * @param id
     * @return
     */
    @Override
    public ActivitySimpleDto getSimpleById(Long id) {
        Activity activity = repository.get(id);
        if (activity != null || activity.getIsActive())
            return Activityfactory.convertSimple(activity);
        return null;
    }

    /**
     * 查询多个活动
     *
     * @param ids
     * @return
     */
    @Override
    public List<ActivityDto> getByIds(List<Long> ids) {
        Criteria criterion = getSession().createCriteria(Activity.class);
        criterion.add(Restrictions.eq("isActive", true));
        criterion.add(Restrictions.in("id", ids));
        List<Activity> list = criterion.list();
        if (list != null && list.size() > 0)
            return Activityfactory.convertList(list);
        return null;
    }

    /**
     * 查询多个活动
     *
     * @param ids
     * @return
     */
    @Override
    public List<ActivitySimpleDto> getSimpleByIds(List<Long> ids) {
        Criteria criterion = getSession().createCriteria(Activity.class);
        criterion.add(Restrictions.eq("isActive", true));
        criterion.add(Restrictions.in("id", ids));
        List<Activity> list = criterion.list();
        if (list != null && list.size() > 0)
            return Activityfactory.convertSimpleList(list);
        return null;
    }

    /**
     * 活动的分页处理
     *
     * @param criteria
     * @return
     */
    @Override
    public PagedResult<ActivityDto> findPage(ActivityCriteria criteria) {
        PagedResult<Activity> pg = repository.findActivity(criteria);
        if (pg.getTotalItemsCount() == 0)
            return new PagedResult<ActivityDto>(1, 10);
        return Activityfactory.convertpg(pg);

    }

    /**
     * 活动的分页处理
     *
     * @param criteria
     * @return
     */
    @Override
    public PagedResult<ActivitySimpleDto> findSimplePage(ActivityCriteria criteria) {
        PagedResult<Activity> pg = repository.findActivity(criteria);
        if (pg != null) {
            return Activityfactory.convertSimplepg(pg);
        }
        return new PagedResult<ActivitySimpleDto>(1, 10);
    }

//    @Override
//    public List<ActivityDto> beingActiving() {
//        Criteria criteria = getSession().createCriteria(Activity.class);
//        criteria.add(Restrictions.eq("isActive", true));
//        criteria.add(Restrictions.lt("startTime", new Date()));
//        criteria.add(Restrictions.gt("endTime", new Date()));
//        List<Activity> list = criteria.list();
//        return list == null ? null : Activityfactory.convertList(list);
//    }
//
//    @Override
//    public List<ActivityDto> postActived() {
//        Criteria criteria = getSession().createCriteria(Activity.class);
//        criteria.add(Restrictions.eq("isActive", true));
//        criteria.add(Restrictions.lt("endTime", new Date()));
//        List<Activity> list = criteria.list();
//        return list == null ? null : Activityfactory.convertList(list);
//    }

    @Override
    public CommonResult changeActivityStatuss(Long id, ActivityStatus status) {
        Activity activity = repository.get(id);
        if (ActivityStatus.Run == status) {
            if (!activity.getEndTime().after(new Date())) {
                return new CommonResult(false, "活动已过期，请修改活动时间后运行！");

            }
            if (activity.getStartTime().after(new Date())) {
                return new CommonResult(false, "活动尚未到开始时间，请修改活动时间后运行！");
            }
        }
        activity.setActivityStatus(status);
        activity.setLastUpdateTime(new Date());
        return ResultFactory.commonSuccess(repository.save(activity));
    }

    /**
     * 获取所有活动
     * @return
     */
    public List<ActivityDto> getAll(){
        List<Activity> list=repository.getAll();
        if (list != null && list.size() > 0)
            return Activityfactory.convertList(list);
        return null;
    }

    @Override
    public List<ActivityDto> getActivityByProductId(Long id) {
        Criteria criterion = getSession().createCriteria(Activity.class);
        criterion.add(Restrictions.eq("isActive", true));
        criterion.add(Restrictions.eq("id", id));
        List<Activity> list = criterion.list();
        if (list != null && list.size() > 0)
            return Activityfactory.convertList(list);
        return null;
    }

    @Override
    public CommonResult updatePage(ActivityDto activityDto) {

        Activity activity=repository.get(activityDto.getId());
        if(!StringUtils.isNullOrWhiteSpace(activityDto.getPageProfile())) {
            activity.setHasPageProfile(true);
            activity.setPageProfile(activityDto.getPageProfile());
        }
        if(!StringUtils.isNullOrWhiteSpace(activityDto.getPageProfileMobile())) {
            activity.setHasPageProfileMobile(true);
            activity.setPageProfileMobile(activityDto.getPageProfileMobile());
        }
        return ResultFactory.commonSuccess(repository.save(activity));
    }
}
