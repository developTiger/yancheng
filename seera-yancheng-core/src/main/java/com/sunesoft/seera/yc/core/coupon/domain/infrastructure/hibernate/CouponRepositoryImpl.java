package com.sunesoft.seera.yc.core.coupon.domain.infrastructure.hibernate;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateRepository;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.coupon.domain.Coupon;
import com.sunesoft.seera.yc.core.coupon.domain.CouponStatus;
import com.sunesoft.seera.yc.core.coupon.domain.ICouponRepository;
import com.sunesoft.seera.yc.core.coupon.domain.criteria.CouponCriteria;
import com.sunesoft.seera.yc.core.tourist.domain.Tourist;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * Created by zhaowy on 2016/7/11.
 */

@Service("iCouponRepository")
public class CouponRepositoryImpl
        extends GenericHibernateRepository<Coupon, Long> implements ICouponRepository {


    /**
     * @param num 优惠券编码
     * @return 匹配的优惠券信息，否则范围NULL
     */
    @Override
    public Coupon getByNum(String num) {
        Criteria criterion = getSession().createCriteria(Coupon.class);
        criterion.add(Restrictions.eq("num", num));
        criterion.add(Restrictions.eq("isActive",true));
        return (Coupon) criterion.uniqueResult();
    }

    /**
     * 检查优惠券是否过期
     *
     * @param id
     * @return
     */
    @Override
    public boolean checkDate(Long id) {
        Coupon c = get(id);
        if (c != null && c.getIsActive() && c.getExpireDate() != null && c.getExpireDate().before(new Date())) {
            c.setCouponStatus(CouponStatus.Expired);
            save(c);
            return true;
        }
        return false;
    }

    /**
     * 检查优惠券是否有效
     *
     * @param id
     * @return
     */
    @Override
    public boolean isUsed(Long id) {
        Coupon c = get(id);
        if (c != null && c.getIsActive() && c.getCouponStatus().equals(CouponStatus.Valid)&&c.getExpireDate().after(new Date()))
            return true;
        return false;
    }


    /**
     * 查询优惠券
     *
     * @param criteria
     * @return 优惠券信息集合
     */
    @Override
    public PagedResult<Coupon> findCoupons(CouponCriteria criteria) {
        Criteria criterion = getSession().createCriteria(Coupon.class);
        criterion.add(Restrictions.eq("isActive", true));
        if(criteria.getCouponTypeId()!=null){
            criterion.add(Restrictions.eq("CouponTypeId", criteria.getCouponTypeId()));
        }

        if (!StringUtils.isNullOrWhiteSpace(criteria.getNum())) {
            criterion.add(Restrictions.eq("num", criteria.getNum()));
        }

        if (!StringUtils.isNullOrWhiteSpace(criteria.getTouristName())) {
            criterion.add(Restrictions.like("touristName","%"+ criteria.getTouristName()+"%"));
        }
        if (criteria.getCouponStatus() != null && !StringUtils.isNullOrWhiteSpace(criteria.getCouponStatus().toString())) {
            criterion.add(Restrictions.eq("couponStatus", criteria.getCouponStatus()));
        }

        if(criteria.getEndUseCondition()!=null){
            criterion.add(Restrictions.le("useCondition", criteria.getEndUseCondition()));
        }
        if(criteria.getStartUseCondition()!=null){
            criterion.add(Restrictions.ge("useCondition", criteria.getStartUseCondition()));
        }
        if(!StringUtils.isNullOrWhiteSpace(criteria.getStartExpireDate())){
            criterion.add(Restrictions.ge("expireDate", DateHelper.parse(criteria.getStartExpireDate())));
        }
        if(!StringUtils.isNullOrWhiteSpace(criteria.getEndExpireDate())){
            criterion.add(Restrictions.le("expireDate", DateHelper.parse(criteria.getEndExpireDate())));
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
        List<Coupon> beans = criterion.list();
        return new PagedResult<Coupon>(beans, criteria.getPageNumber(), criteria.getPageSize(), totalCount);
    }
}
