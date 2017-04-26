package com.sunesoft.seera.yc.core.activity.domain.infrastructure.hibernate;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateRepository;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.activity.domain.Activity;
import com.sunesoft.seera.yc.core.activity.domain.ActivityStatus;
import com.sunesoft.seera.yc.core.activity.domain.IActivityRepository;
import com.sunesoft.seera.yc.core.activity.domain.creteria.ActivityCriteria;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiazl on 2016/8/6.
 */
@Service("iActivityRepository")
public class ActivityRepositoryImpl extends GenericHibernateRepository<Activity, Long> implements IActivityRepository {


    @Override
    public boolean check(String activeName) {
        Criteria criteria = getSession().createCriteria(Activity.class);
        criteria.add(Restrictions.eq("isActive", true));
        criteria.add(Restrictions.eq("name", activeName));
        List<ActivityStatus> list = new ArrayList<>();
        list.add(ActivityStatus.Run);
        list.add(ActivityStatus.Wait);
        criteria.add(Restrictions.in("activityStatus", list.toArray()));
        if (criteria.list() != null && criteria.list().size() > 0)
            return true;
        return false;
    }

    /**
     * 分页查询
     *
     * @param criteria
     * @return
     */
    @Override
    public PagedResult<Activity> findActivity(ActivityCriteria criteria) {

        Criteria criterion = getSession().createCriteria(Activity.class);
        criterion.add(Restrictions.eq("isActive", true));

        if (!StringUtils.isNullOrWhiteSpace(criteria.getName())) {
            criterion.add(Restrictions.like("name", "%" + criteria.getName() + "%"));
        }
        if (!StringUtils.isNullOrWhiteSpace(criteria.getProductName())) {
            criterion.createAlias("product", "product");
            criterion.add(Restrictions.like("product.name", "%" + criteria.getProductName() + "%"));
        }

        if (criteria.getBeginTime() != null)
            criterion.add(Restrictions.ge("startTime", criteria.getBeginTime()));
        if (criteria.getOverTime() != null)
            criterion.add(Restrictions.le("endTime", criteria.getOverTime()));
        if (criteria.getType() != null)
            criterion.add(Restrictions.eq("type", criteria.getType()));

        if (criteria.getActivityStatus() != null) {
            criterion.add(Restrictions.eq("activityStatus", criteria.getActivityStatus()));
        }
        int totalCount = ((Long) criterion.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criterion.setProjection(null);
        criterion.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criterion.addOrder(
                criteria.isAscOrDesc() ?
                        Order.asc(criteria.getOrderByProperty() == null ? "id" : criteria.getOrderByProperty()) :
                        Order.desc(criteria.getOrderByProperty() == null ? "id" : criteria.getOrderByProperty())
        );
        criterion.setFirstResult((criteria.getPageNumber() - 1) * criteria.getPageSize()).setMaxResults(criteria.getPageSize());
        return new PagedResult<Activity>(criterion.list(), criteria.getPageNumber(), criteria.getPageSize(), totalCount);
    }

    /**
     * 获取所有活动
     * @return
     */
    public List<Activity> getAll(){
        Criteria criteria=getSession().createCriteria(Activity.class);
        criteria.add(Restrictions.eq("isActive",true));
        List<Activity> list=criteria.list();
        if(list==null||list.isEmpty())return null;
        return list;
    }
}
