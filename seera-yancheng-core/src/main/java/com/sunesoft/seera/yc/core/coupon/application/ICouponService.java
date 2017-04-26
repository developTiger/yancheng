package com.sunesoft.seera.yc.core.coupon.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.coupon.application.dto.CouponDto;
import com.sunesoft.seera.yc.core.coupon.domain.criteria.CouponCriteria;

import java.util.List;


/**
 * 优惠券服务接口
 * Created by zhaowy on 2016/7/11.
 */
public interface ICouponService {

    /**
     * 增加或者修改优惠券
     * @param dto
     * @return
     */
    public CommonResult addCoupon(CouponDto dto);

    /**
     * 增加或者修改优惠券
     * @param dto
     * @return
     */
    public CommonResult updateCoupon(CouponDto dto);

    /**
     * 根据id获取优惠券
     * @param id
     * @return
     */
    public CouponDto getById(Long id);

    /**
     * 根据id获取优惠券
     * @param ids
     * @return
     */
    public List<CouponDto> getByIds(List<Long> ids);


    /**
     * 根据num获取优惠券
     * @param num
     * @return
     */
    public CouponDto getByNum(String num);

    /**
     * 根据nums删除优惠券
     * @param nums
     * @return
     */
    public CommonResult deleteByNums(List<String> nums);

    /**
     * 根据ids删除优惠券
     * @param ids
     * @return
     */
    public CommonResult deleteByIds(List<Long> ids);
    /**
     * 根据id删除优惠券(这里指定为未绑定游客的优惠券可以删除)
     * @param id
     * @return
     */
    public CommonResult deleteById(Long id);

    /**
     * 设置使用过
     * @param num
     * @return
     */
    public CommonResult setUsed(String num);

    /**
     * 绑定员工
     * @param saffId
     * @param ids
     * @return
     */
    public CommonResult bindStaff(Long saffId,List<Long> ids);

    /**
     * 获取所有优惠券
     * @return
     */
    public List<CouponDto> getAll();

    /**
     * 分页查询
     * @param criteria
     * @return
     */
    public PagedResult<CouponDto> findCoupons(CouponCriteria criteria);

    /**
     * 分页查询(仅限管理员的票)
     * @param criteria
     * @return
     */
    public PagedResult<CouponDto> findManagerCoupons(CouponCriteria criteria);

}
