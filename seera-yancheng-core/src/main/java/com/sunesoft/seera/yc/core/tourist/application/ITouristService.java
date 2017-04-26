package com.sunesoft.seera.yc.core.tourist.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.yc.core.coupon.application.dto.CouponDto;
import com.sunesoft.seera.yc.core.coupon.domain.CouponStatus;
import com.sunesoft.seera.yc.core.tourist.application.dtos.FetcherDto;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristDto;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristSimpleDto;
import com.sunesoft.seera.yc.core.tourist.domain.TouristStatus;
import com.sunesoft.seera.yc.core.tourist.domain.criteria.TouristCriteria;

import java.util.List;

/**
 * 游客服务接口
 * Created by zhaowy on 2016/7/11.
 */
public interface ITouristService {

    //region Description 对游客的操作

    /**
     * 新建游客
     *
     * @param dto 必要信息
     * @return
     */
    public CommonResult create(TouristDto dto);

    /**
     * 修改游客
     *
     * @param dto 必要信息
     * @return
     */
    public CommonResult update(TouristDto dto);

    /**
     * wxoauth
     *
     * @param token 支持userName|mobilePhone|email
     * @param pwd   明文密码
     * @return
     */
    UniqueResult<TouristSimpleDto> login(String token, String pwd);

    /**
     * 前台用户注册接口.
     *
     * @param userName    用户名.
     * @param password 密码.
     * @return 返回操作接口.
     */
    CommonResult register(String userName, String password);

    /**
     * 查询Token是否已经存在
     *
     * @param token userName|mobilePhone|email
     * @return
     */
    public CommonResult check(String token);

    /**
     * 查询微信OpenId是否已经存在
     *
     * @param openId
     * @return
     */
    public UniqueResult<TouristSimpleDto> checkOpenId(String openId);

    /**
     * 获取游客详情
     *
     * @param id 游客标识
     * @return
     */
    public UniqueResult<TouristDto> getTourist(Long id);

    /**
     * 获取游客详情
     *
     * @param token 支持userName|mobilePhone|email
     * @return
     */
    public UniqueResult<TouristDto> getTourist(String token);

    /**
     * 获取游客详情
     *
     * @param openId 支持微信绑定登录的游客信息读取
     * @return
     */
    public UniqueResult<TouristDto> getTouristByOpenId(String openId);

    /**
     * 获取游客详情
     *
     * @param id 游客标识
     * @return
     */
    public UniqueResult<TouristSimpleDto> getTouristSimple(Long id);

    /**
     * 获取游客详情
     *
     * @param token 支持userName|mobilePhone|email
     * @return
     */
    public UniqueResult<TouristSimpleDto> getTouristSimple(String token);

    /**
     * 重置游客密码
     *
     * @param id  游客标识
     * @param pwd 新密码
     * @return
     */
    public CommonResult restPassword(Long id, String pwd);

    /**
     * 更新游客密码
     * @param touristId
     * @param oldPassword
     * @param newPassword
     * @return
     */
    CommonResult updatePassWord(Long touristId, String oldPassword, String newPassword);

    /**
     * 设置游客状态
     *
     * @param id     游客标识
     * @param status 游客状态
     * @return
     */
    public CommonResult setStatus(Long id, TouristStatus status);

    /**
     * 设置用户积分
     * @param id 用户标识
     * @param integrals 总积分
     * @return
     */
     public CommonResult setIntegrals(Long id,Integer integrals);

    /**
     * 增加用户积分
     * @param id 用户标识
     * @param integrals 增加积分
     * @return
     */
    public CommonResult increaseIntegrals(Long id,Integer integrals);

    /**
     * 查询游客简要信息
     *
     * @param criteria
     * @return
     */
    public PagedResult<TouristSimpleDto> findSimpleTourists(TouristCriteria criteria);

    /**
     * 查询游客详细信息
     *
     * @param criteria
     * @return
     */
    public PagedResult<TouristDto> findTourists(TouristCriteria criteria);


    //endregion


    //region Description  //对取件人的错做

    /**
     * 增加取件人
     *
     * @param dto
     * @param touristId
     * @return
     */
    public CommonResult create(Long touristId, FetcherDto dto);

    /**
     * 修改取件人
     *
     * @param dto
     * @param touristId
     * @return
     */
    public CommonResult update(Long touristId, FetcherDto dto);


    /**
     * 修改取件人
     *
     * @param dto
     * @param touristId
     * @return
     */
    public CommonResult updateNoSame(Long touristId, FetcherDto dto);

    /**
     * 设置默认取票人
     *
     * @param fetcherId
     * @param touristId
     * @return
     */
    public CommonResult setDefault(Long touristId, Long fetcherId);



    /**
     * 批量移除取件人
     *
     * @param fetcherIds
     * @param touristId
     * @return
     */
    public CommonResult remove(Long touristId, List<Long> fetcherIds);

    /**
     * 获取取件人
     *
     * @param touristId
     * @param fetcherId
     * @return
     */
    public FetcherDto getByFetcherId(Long touristId, Long fetcherId);

    /**
     * 获取所有取件人
     *
     * @param touristId
     * @return
     */
    public List<FetcherDto> getAllFetchers(Long touristId);
    //endregion

    //region 优惠券

    /**
     * 绑定优惠券
     *
     * @param touristId
     * @param num
     * @return
     */
    public CommonResult bindCoupon(Long touristId, String num);

    /**
     * 批量删除优惠券
     *
     * @param touristId
     * @param nums
     * @return
     */
    public CommonResult removeCoupon(Long touristId, List<String> nums);

    /**
     * 删除优惠券
     *
     * @param touristId
     * @param deleteId
     * @return
     */
    public CommonResult removeCoupon(Long touristId, Long deleteId);


    /**
     * 查看优惠券详细信息
     *
     * @param touristId
     * @param num
     * @return
     */
    public CouponDto getByNum(Long touristId, String num);

    /**
     *
     * @param touristId
     *
     * @return
     */
    public List<CouponDto> getAllCoupons(Long touristId);

    /**
     * 获取所有优惠券，其中分：全部，有效，过期的
     *
     * @param touristId
     * @param status
     * @return
     */
    public List<CouponDto> getAllCoupons(Long touristId, CouponStatus status);


    public List<CouponDto> getCouponsByStatus(Long touristId, CouponStatus status);

    //endregion

}
