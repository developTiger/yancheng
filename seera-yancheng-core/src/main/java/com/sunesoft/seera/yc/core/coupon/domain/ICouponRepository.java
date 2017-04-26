package com.sunesoft.seera.yc.core.coupon.domain;

import com.sunesoft.seera.fr.ddd.infrastructure.IRepository;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.coupon.domain.criteria.CouponCriteria;

import java.util.UUID;

/**
 * 优惠券仓储接口
 * Created by zhaowy on 2016/7/11.
 */
public interface ICouponRepository extends IRepository<Coupon,Long>{

    /**
     * @param num 优惠券编码
     * @return 匹配的优惠券信息，否则范围NULL
     */
    public Coupon getByNum(String num);

    /**
     * 检查优惠券是否过期
     * @param id
     * @return
     */
    public boolean checkDate(Long id);

    /**
     * 检查优惠券是否使用过
     * @param id
     * @return
     */
    public boolean isUsed(Long id);

    /**
     * 查询优惠券
     *
     * @param criteria
     * @return 优惠券信息集合
     */
    public PagedResult<Coupon> findCoupons(CouponCriteria criteria);

}
