package com.sunesoft.seera.yc.core.coupon.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.coupon.application.dto.CouponTypeDto;
import com.sunesoft.seera.yc.core.coupon.application.dto.UploadCoupon;
import com.sunesoft.seera.yc.core.coupon.domain.CouponType;
import com.sunesoft.seera.yc.core.coupon.domain.CouponTypeStatus;
import com.sunesoft.seera.yc.core.coupon.domain.criteria.CouponTypeCriteria;

import java.util.List;

/**
 * 优惠券活动项服务接口
 * Created by xiazl on 2016/8/31.
 */
public interface ICouponTypeService {

    /**
     * 增加优惠券领取项
     * @param dto
     * @return
     */
    public CommonResult create(CouponTypeDto dto) throws Exception;

    /**
     * 修改优惠券领取项
     * @param dto
     * @return
     */
    public CommonResult edit(CouponTypeDto dto) throws Exception;

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    public CouponTypeDto getById(Long id);

    /**
     * 设置优惠券活动的状态
     * @param id
     * @param status
     * @return
     */
    public CommonResult setStatus(Long id,CouponTypeStatus status);
    /**
     * 设置优惠券活动的状态
     * @param ids
     * @param status
     * @return
     */
    public CommonResult setStatus(List<Long> ids,CouponTypeStatus status);
    /**
     * 单个删除优惠券活动项
     * @param id
     * @return
     */
    public CommonResult delete(Long id);
    /**
     * 删除优惠券活动的状态
     * @param ids
     * @return
     */
    public CommonResult delete(List<Long> ids);

    /**
     * 检查给游客是否领取
     * @param id
     * @return
     */
    public CommonResult check(Long id,Long couponTypeId);

    /**
     * 领取优惠券
     * @param touristId
     * @param couponTypeId
     * @return
     */
    public CommonResult draw(Long touristId,Long couponTypeId);

    /**
     * 优惠券的活动分页查询
     * @param couponTypeCriteria
     * @return
     */
    public PagedResult<CouponTypeDto> findPage(CouponTypeCriteria couponTypeCriteria);

    public List<CouponType> getAll();



    public CommonResult bindCouponsByIds(Long touristId,Long couponTypeId,Integer count);


    public CommonResult uploadManagerCoupons(List<UploadCoupon> uploadCoupons);

}
