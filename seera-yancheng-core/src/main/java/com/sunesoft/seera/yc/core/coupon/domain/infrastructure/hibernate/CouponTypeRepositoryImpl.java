package com.sunesoft.seera.yc.core.coupon.domain.infrastructure.hibernate;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateRepository;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.coupon.domain.CouponType;
import com.sunesoft.seera.yc.core.coupon.domain.CouponTypeStatus;
import com.sunesoft.seera.yc.core.coupon.domain.ICouponTypeRepository;
import com.sunesoft.seera.yc.core.coupon.domain.criteria.CouponTypeCriteria;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xiazl on 2016/8/31.
 */
@Service("iCouponTypeRepository")
public class CouponTypeRepositoryImpl extends GenericHibernateRepository<CouponType, Long> implements ICouponTypeRepository {


    @Override
    public CommonResult setStatus(Long id, CouponTypeStatus status) {
        Criteria criteria = getSession().createCriteria(CouponType.class);
        criteria.add(Restrictions.eq("isActive", true));
        criteria.add(Restrictions.eq("id", id));
        List<CouponType> list = criteria.list();
        if (list != null && list.size() > 0) {
            list.get(0).setStatus(status);
            return ResultFactory.commonSuccess(save(list.get(0)));
        }
        return ResultFactory.commonError("该优惠券活动不存在");
    }

    @Override
    public CommonResult setStatus(List<Long> ids, CouponTypeStatus status) {
        Criteria criteria = getSession().createCriteria(CouponType.class);
        criteria.add(Restrictions.eq("isActive", true));
        criteria.add(Restrictions.in("id", ids));
        List<CouponType> list = criteria.list();
        if (list != null && list.size() > 0) {
            list.stream().forEach(i -> {
                i.setStatus(status);
                save(i);
            });
            return ResultFactory.commonSuccess();
        }
        return ResultFactory.commonError("该优惠券活动不存在");
    }

    @Override
    public PagedResult<CouponType> findPage(CouponTypeCriteria criteria) {
        Criteria criterion = getSession().createCriteria(CouponType.class);
        criterion.add(Restrictions.eq("isActive", true));
        if(!StringUtils.isNullOrWhiteSpace(criteria.getCouponTypeName())){
            criterion.add(Restrictions.like("couponTypeName", "%"+criteria.getCouponTypeName()+"%"));
        }
        if (criteria.getCouponTypeId() != null)
            criterion.add(Restrictions.eq("id", criteria.getCouponTypeId()));
        if (criteria.getStatus() != null)
            criterion.add(Restrictions.eq("status", criteria.getStatus()));
        if (criteria.getStartQuota() != null)
            criterion.add(Restrictions.ge("quota", criteria.getStartQuota()));
        if (criteria.getEndQuota() != null)
            criterion.add(Restrictions.le("quota", criteria.getEndQuota()));
        if (criteria.getStartTime() != null)
            criterion.add(Restrictions.ge("startTime", criteria.getStartTime()));
        if (criteria.getEndTime() != null)
            criterion.add(Restrictions.le("overTime", criteria.getEndTime()));
        int totalCount = ((Long) criterion.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criterion.setProjection(null);
        criterion.addOrder(
                criteria.isAscOrDesc() ?
                        Order.asc(criteria.getOrderByProperty() == null ? "id" : criteria.getOrderByProperty()) :
                        Order.desc(criteria.getOrderByProperty() == null ? "id" : criteria.getOrderByProperty())

        );
        criterion.setFirstResult((criteria.getPageNumber()-1)*criteria.getPageSize()).setMaxResults(criteria.getPageSize());

        return new PagedResult<CouponType>(criterion.list(),criteria.getPageNumber(),criteria.getPageSize(),totalCount);
    }



}
