package com.sunesoft.seera.yc.core.activity.domain;

import com.sunesoft.seera.fr.ddd.infrastructure.IRepository;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.activity.domain.creteria.ActivityCriteria;

import java.util.List;

/**
 * 活动仓储接口
 * Created by xiazl on 2016/8/6.
 */
public interface IActivityRepository extends IRepository<Activity,Long>{

    /**
     * 该活动是否已经存在（设计是正在运行的活动名不重复）
     * @param activeName
     * @return
     */
    public boolean check(String activeName);
    /**
     * 分页查询
     * @param criteria
     * @return
     */
    public PagedResult<Activity> findActivity(ActivityCriteria criteria);

    /**
     * 获取所有活动
     * @return
     */
    public List<Activity> getAll();
}
