package com.sunesoft.seera.yc.core.coupon.domain;

import com.sunesoft.seera.fr.ddd.infrastructure.IRepository;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.coupon.domain.criteria.CouponTypeCriteria;

import java.util.List;

/**
 * 领取优惠券项的仓储
 * Created by xiazl on 2016/8/31.
 */
public interface ICouponTypeRepository extends IRepository<CouponType,Long> {
//    /**
//     * 检查是否领取过
//     * @param id
//     * @return
//     */
//    public Boolean check(Long id);
    /**
     * 单个设置状态
     * @param id
     * @return
     */
    public CommonResult setStatus(Long id,CouponTypeStatus status);

    /**
     * 多个设置状态
     * @param ids
     * @return
     */
    public CommonResult setStatus(List<Long> ids,CouponTypeStatus status);

    /**
     * 优惠券的领取的活动分页 查询
     * @param criteria
     * @return
     */
    public PagedResult<CouponType> findPage(CouponTypeCriteria criteria);



}
