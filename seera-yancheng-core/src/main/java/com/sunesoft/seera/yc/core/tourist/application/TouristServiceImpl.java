package com.sunesoft.seera.yc.core.tourist.application;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateFinder;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.coupon.application.dto.CouponDto;
import com.sunesoft.seera.yc.core.coupon.application.factory.CouponFactory;
import com.sunesoft.seera.yc.core.coupon.domain.Coupon;
import com.sunesoft.seera.yc.core.coupon.domain.CouponStatus;
import com.sunesoft.seera.yc.core.coupon.domain.ICouponRepository;
import com.sunesoft.seera.yc.core.tourist.application.dtos.FetcherDto;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristDto;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristSimpleDto;
import com.sunesoft.seera.yc.core.tourist.application.factory.TouristFactory;
import com.sunesoft.seera.yc.core.tourist.domain.*;
import com.sunesoft.seera.yc.core.tourist.domain.criteria.TouristCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.ConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 游客服务接口实现类
 * Created by zhaowy on 2016/7/11.
 */
@Service("iTouristService")
public class TouristServiceImpl
        extends GenericHibernateFinder implements ITouristService {

    @Autowired
    private ITouristRepository repository;

    @Autowired
    ICouponRepository couponRepository;


    @Autowired
    YearCardInfoRepository yearCardInfoRepository;

    /**
     * 新建游客
     *
     * @param dto 必要信息
     * @return
     */
    @Override
    public CommonResult create(TouristDto dto) {
        if (null == dto)
            return ResultFactory.commonError(dto.toString() + "is bad format");
        if (null != repository.check(dto.getUserName()))
            return ResultFactory.commonError(dto.getUserName() + "is exist!");

        if (null != repository.check(dto.getEmail()))
            return ResultFactory.commonError(dto.getEmail() + "is exist!");

        if (null != repository.check(dto.getMobilePhone()))
            return ResultFactory.commonError(dto.getMobilePhone() + "is exist!");

//        if (null != repository.check(dto.getWxName()))
//            return ResultFactory.commonError(dto.getWxName() + "is exist!");
        Tourist tourist = TouristFactory.conertFromTouristDto(dto);
        if (dto.getBindCouponDtos() != null && dto.getBindCouponDtos().size() > 0) {
            for (CouponDto d : dto.getBindCouponDtos()) {
                Coupon c = couponRepository.get(d.getId());
                if (c.getCouponStatus().equals(CouponStatus.Valid)) {
                    c.setCouponStatus(CouponStatus.bind);
                    tourist.bindCoupons(c);
                }
            }
        }
        if (null == tourist)
            return ResultFactory.commonError(dto.toString() + "is bad format");

        return ResultFactory.commonSuccess(repository.save(tourist));
    }

    /**
     * 修改游客信息
     *
     * @param dto 必要信息
     * @return
     */
    @Override
    public CommonResult update(TouristDto dto) {
        if (null == dto)
            return ResultFactory.commonError(dto.toString() + "is bad format");
        Tourist tourist = repository.get(dto.getId());
        if (tourist == null || !tourist.getIsActive() || !tourist.getStatus().equals(TouristStatus.Normal)) {
            return ResultFactory.commonError("该用户不存在，或者被禁用");
        }
        //除去与本身的对比
        Tourist exist = repository.check(dto.getUserName());
        if (null != exist && !dto.getUserName().equals(tourist.getUserName()) && !exist.equals(dto.getId()))
            return ResultFactory.commonError(dto.getUserName() + "is exist!");

        exist = repository.check(dto.getEmail());
        if ( null != exist && !dto.getEmail().equals(tourist.getEmail()) &&!exist.equals(dto.getId()))
            return ResultFactory.commonError(dto.getEmail() + "is exist!");

        exist = repository.check(dto.getMobilePhone());
        if ( null != exist &&!dto.getMobilePhone().equals(tourist.getMobilePhone()) && !exist.equals(dto.getId()))
            return ResultFactory.commonError(dto.getMobilePhone() + "is exist!");

        //在这里优惠券和取件人无法更改
        dto.setBindCouponDtos(null);
        dto.setFetcherDtos(null);
        tourist = TouristFactory.conertFromTouristDto(dto, tourist);
        if (!StringUtils.isNullOrWhiteSpace(tourist.getMobilePhone())) {
            YearCardInfo yearCardInfo = yearCardInfoRepository.getYearCartByPhone(tourist.getMobilePhone());
            if (yearCardInfo != null) {
                tourist.setYearCardInfo(yearCardInfo.getYearCardType());
                tourist.setYearCardExpireDate(yearCardInfo.getYearCardExpireDate());
            }
        }
//        tourist = TouristFactory.conertFromTouristDto(dto);
        if (dto.getBindCouponDtos() != null && dto.getBindCouponDtos().size() > 0) {
            for (CouponDto d : dto.getBindCouponDtos()) {
                Coupon c = couponRepository.get(d.getId());
                if (c.getCouponStatus().equals(CouponStatus.Valid)) {
                    c.setCouponStatus(CouponStatus.bind);
                    tourist.bindCoupons(c);
                }
            }
        }
        if (null == tourist)
            return ResultFactory.commonError(dto.toString() + "is bad format");

        return ResultFactory.commonSuccess(repository.save(tourist));
    }


    @Override
    public UniqueResult<TouristSimpleDto> login(String token, String pwd) {
        UniqueResult<Tourist> uniqueResult = repository.login(token, pwd);
        if (!uniqueResult.getIsSuccess())
            return new UniqueResult<>("登录名与密码不匹配");

        //region update tourist Integrals
        Tourist tourist = uniqueResult.getT();
      reloadTouristIntegrals(tourist.getOpenid());
        return new UniqueResult<>(TouristFactory.conertToTouristDto(uniqueResult.getT()));
    }

    /**
     * 更新游客积分数据
     *
     * @param openId
     */
    private void reloadTouristIntegrals(String openId) {
        try {

            Tourist tourist = repository.checkOpenId(openId);
            CommonResult result = UserIntegralService.getUserIntegrals(openId);
            if (result.getIsSuccess()) {
                tourist.setIntegrals(Integer.parseInt(result.getMsg()));
                repository.save(tourist);
            }
        } catch (ConfigurationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 前台用户注册接口.
     *
     * @param userName userName|mobilePhone|email 用户名.
     * @param password 密码.
     * @return 返回操作接口.
     */
    public CommonResult register(String userName, String password) {
        if (org.apache.commons.lang.StringUtils.isEmpty(userName))
            return new CommonResult(false, "用户名不能为空");
        if (org.apache.commons.lang.StringUtils.isEmpty(password))
            return new CommonResult(false, "密码不能为空");
        CommonResult c = check(userName);
        if (!c.getIsSuccess()) return new CommonResult(false, "该用户名已经被使用");
        Tourist tourist = new Tourist(userName, password);
        long id = repository.save(tourist);
        return id > 0 ? new CommonResult(true, "注册用户成功") : new CommonResult(false, "注册用户失败");
    }

    /**
     * 查询Token是否已经存在
     *
     * @param token userName|mobilePhone|email
     * @return
     */
    @Override
    public CommonResult check(String token) {
        Tourist tourist = repository.check(token);
        return null != tourist ?
                ResultFactory.commonError(token + " : is  existed!") : ResultFactory.commonSuccess();
    }

    /**
     * 查询微信OpenId是否已经存在
     *
     * @param openId
     * @return
     */
    public UniqueResult<TouristSimpleDto> checkOpenId(String openId) {
        Tourist tourist = repository.checkOpenId(openId);
        if (tourist != null) {
            reloadTouristIntegrals(tourist.getOpenid());
            return new UniqueResult<>(TouristFactory.conertToTouristDto(tourist));
        }
        return new UniqueResult<>("用户不存在");
    }

    /**
     * 获取游客详情
     *
     * @param id 游客标识
     * @return
     */
    @Override
    public UniqueResult<TouristDto> getTourist(Long id) {
        Tourist tourist = repository.get(id);
        return null == tourist ? new UniqueResult<>(id + "obj do not exist")
                : new UniqueResult<>(TouristFactory.conertToTouristDto(tourist));
    }

    /**
     * 获取游客详情
     *
     * @param token 支持userName|wxName|mobilePhone|email
     * @return
     */
    @Override
    public UniqueResult<TouristDto> getTourist(String token) {
        Tourist tourist = repository.check(token);
        return null == tourist ? new UniqueResult<>("obj do not exist")
                : new UniqueResult<>(TouristFactory.conertToTouristDto(tourist));
    }

    @Override
    public UniqueResult<TouristDto> getTouristByOpenId(String openId) {
        Tourist tourist = repository.checkOpenId(openId);
        return null == tourist ? new UniqueResult<>("obj do not exist")
                : new UniqueResult<>(TouristFactory.conertToTouristDto(tourist));
    }

    @Override
    public UniqueResult<TouristSimpleDto> getTouristSimple(Long id) {
        Tourist tourist = repository.get(id);
        return null == tourist ? new UniqueResult<>(id + "obj do not exist")
                : new UniqueResult<>(TouristFactory.convert(tourist, TouristSimpleDto.class));
    }

    @Override
    public UniqueResult<TouristSimpleDto> getTouristSimple(String token) {
        Tourist tourist = repository.check(token);
        return null == tourist ? new UniqueResult<>("obj do not exist")
                : new UniqueResult<>(TouristFactory.convert(tourist, TouristSimpleDto.class));
    }

    /**
     * 重置游客密码
     *
     * @param id  游客标识
     * @param pwd 新密码
     * @return
     */
    @Override
    public CommonResult restPassword(Long id, String pwd) {
        Tourist tourist = repository.get(id);
        if (tourist == null || tourist.getStatus().equals(TouristStatus.Forbidden)) {
            return ResultFactory.commonError("该用户不存在，或者已经被禁用");
        }
        tourist.setPassword(pwd);
        return ResultFactory.commonSuccess(repository.save(tourist));
    }

    /**
     * 设置游客状态
     *
     * @param id     游客标识
     * @param status 游客状态
     * @return
     */
    @Override
    public CommonResult setStatus(Long id, TouristStatus status) {

        Tourist tourist = repository.get(id);
        if (tourist == null || !tourist.getIsActive()) {
            return ResultFactory.commonError("该用户不存在");
        }
        tourist.setStatus(status);
        return ResultFactory.commonSuccess(repository.save(tourist));
    }

    /**
     * 设置用户积分
     *
     * @param id        用户标识
     * @param integrals 总积分
     * @return
     */
    @Override
    public CommonResult setIntegrals(Long id, Integer integrals) {
        Tourist tourist = repository.get(id);
        if (tourist == null || !tourist.getIsActive()) {
            return ResultFactory.commonError("该用户不存在");
        }
        tourist.setIntegrals(integrals);
        return ResultFactory.commonSuccess(repository.save(tourist));
    }

    /**
     * 增加用户积分
     *
     * @param id        用户标识
     * @param integrals 增加积分
     * @return
     */
    @Override
    public CommonResult increaseIntegrals(Long id, Integer integrals) {
        Tourist tourist = repository.get(id);
        if (tourist == null || !tourist.getIsActive() || tourist.getOpenid() == null) {
            return ResultFactory.commonError("该用户或openid不存在或用户已禁用");
        }
        try {
            UserIntegralService.increaseUserIntegrals(tourist.getOpenid(), integrals);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tourist.increaseIntergrals(integrals);
        return ResultFactory.commonSuccess(repository.save(tourist));
    }

    /**
     * 查询游客简要信息
     *
     * @param criteria 查询统计条件
     * @return
     */
    @Override
    public PagedResult<TouristSimpleDto> findSimpleTourists(TouristCriteria criteria) {
        return TouristFactory.convert(repository.findTourists(criteria), TouristSimpleDto.class);
    }

    /**
     * 查询游客详细信息
     *
     * @param criteria 查询统计条件
     * @return
     */
    @Override
    public PagedResult<TouristDto> findTourists(TouristCriteria criteria) {
        return TouristFactory.convertpg(repository.findTourists(criteria));
    }

    /**
     * 增加取件人
     *
     * @param dto
     * @param touristId
     * @return
     */
    @Override
    public CommonResult create(Long touristId, FetcherDto dto) {

        Tourist tourist = repository.get(touristId);
        if (tourist != null && tourist.getStatus().equals(TouristStatus.Normal)) {
            Fetcher fetcher = TouristFactory.convertFromFetcherDto(dto);
           CommonResult addResult= tourist.addFetchers(fetcher);

            tourist.setLastUpdateTime(new Date());
            repository.save(tourist);
            if(addResult.getId()!=null)
                return ResultFactory.commonSuccess(addResult.getId());
            return ResultFactory.commonSuccess(fetcher.getId());
        }
        return ResultFactory.commonError("该游客不存在，或者被禁用");
    }


    /**
     * 修改取件人
     *
     * @param dto
     * @param touristId
     * @return
     */
    @Override
    public CommonResult update(Long touristId, FetcherDto dto) {
        Tourist tourist = repository.get(touristId);
        if (tourist != null && tourist.getStatus().equals(TouristStatus.Normal)) {
            Fetcher fetcher = TouristFactory.convert(dto, Fetcher.class);
            fetcher.setLastUpdateTime(new Date());
            tourist.updateFetchers(fetcher);
            repository.save(tourist);
            return ResultFactory.commonSuccess(fetcher.getId());
        }
        return ResultFactory.commonError("该游客不存在，或者被禁用");
    }


    /**
     * 修改取件人
     *
     * @param dto
     * @param touristId
     * @return
     */
    public CommonResult updateNoSame(Long touristId, FetcherDto dto) {
        Tourist tourist = repository.get(touristId);
        if (tourist != null && tourist.getStatus().equals(TouristStatus.Normal)) {
            Fetcher fetcher = TouristFactory.convert(dto, Fetcher.class);
            CommonResult c = tourist.updateFetcher(fetcher);
            if (c.getIsSuccess()) {
                repository.save(tourist);
                return ResultFactory.commonSuccess(fetcher.getId());
            } else {
                return ResultFactory.commonError(c.getMsg());
            }
        }
        return ResultFactory.commonError("该游客不存在，或者被禁用");
    }

    /**
     * 设置默认取票人
     *
     * @param fetcherId
     * @param touristId
     * @return
     */
    public CommonResult setDefault(Long touristId, Long fetcherId) {
        Tourist tourist = repository.get(touristId);
        if (tourist != null && tourist.getStatus().equals(TouristStatus.Normal)) {
            CommonResult c = tourist.resetDefault(fetcherId);
            if (c.getIsSuccess()) {
                repository.save(tourist);
                return ResultFactory.commonSuccess(fetcherId);
            } else {
                return ResultFactory.commonError(c.getMsg());
            }

        }
        return ResultFactory.commonError("该游客不存在，或者被禁用");
    }

    /**
     * 批量移除取件人
     *
     * @param fetcherIds
     * @param touristId
     * @return
     */
    @Override
    public CommonResult remove(Long touristId, List<Long> fetcherIds) {
        if (fetcherIds == null || fetcherIds.size() == 0) return ResultFactory.commonError("未选择删除对象");
        Tourist tourist = repository.get(touristId);
        if (tourist != null && tourist.getStatus().equals(TouristStatus.Normal)) {
            tourist.getFetchers().removeIf(i -> fetcherIds.contains(i.getId()));
            tourist.setLastUpdateTime(new Date());
            repository.save(tourist);
            return ResultFactory.commonSuccess();
        }
        return ResultFactory.commonError("该游客不存在，或者被禁用");
    }


    /**
     * 获取取件人
     *
     * @param touristId
     * @param fetcherId
     * @return
     */
    @Override
    public FetcherDto getByFetcherId(Long touristId, Long fetcherId) {
        Tourist tourist = repository.get(touristId);
        if (tourist != null) {
            for (Fetcher f : tourist.getFetchers()) {
                if (f.getId().equals(fetcherId))
                    return TouristFactory.convertToFetcherDto(f);
            }
        }
        return null;
    }


    /**
     * 获取所有取件人
     *
     * @param touristId
     * @return
     */
    @Override
    public List<FetcherDto> getAllFetchers(Long touristId) {
        List<FetcherDto> fetcherDtos = new ArrayList<>();
        Tourist tourist = repository.get(touristId);
        if (tourist != null && tourist.getIsActive()) {
            fetcherDtos.addAll(TouristFactory.conertToFetcherDto(tourist.getFetchers()));
        }
        return fetcherDtos;
    }


    /**
     * 绑定优惠券
     *
     * @param touristId
     * @param num
     * @return
     */
    @Override
    public CommonResult bindCoupon(Long touristId, String num) {
        Coupon coupon = couponRepository.getByNum(num);
        Tourist tourist = repository.get(touristId);
        if(!coupon.getIsActive())
            return ResultFactory.commonError("优惠券已失效！");
        if(!coupon.getCouponStatus().equals(CouponStatus.Valid))
            return ResultFactory.commonError("优惠券已被绑定请勿重复绑定！");

        if(!coupon.getExpireDate().after(new Date()) ){
            return ResultFactory.commonError("优惠券已过期！");
        }


        if (tourist != null && tourist.getStatus().equals(TouristStatus.Normal) &&
                coupon != null && coupon.getIsActive() && coupon.getExpireDate().after(new Date()) && coupon.getCouponStatus().equals(CouponStatus.Valid)) {
            tourist.bindCoupons(coupon);
            repository.save(tourist);
            return ResultFactory.commonSuccess(coupon.getId());
        }
        return ResultFactory.commonError("绑定优惠券失败");
    }


    /**
     * 批量删除优惠券
     *
     * @param touristId
     * @param nums
     * @return
     */
    @Override
    public CommonResult removeCoupon(Long touristId, List<String> nums) {
        Tourist tourist = repository.get(touristId);
        if (tourist != null && tourist.getStatus().equals(TouristStatus.Normal)) {
            tourist.getBindCoupons().removeIf(i -> nums.contains(i.getNum()));
            repository.save(tourist);
            return ResultFactory.commonSuccess();
        }
        return ResultFactory.commonError("该游客不存在，或者被禁用");
    }

    /**
     * 删除优惠券
     *
     * @param touristId
     * @param deleteId
     * @return
     */
    public CommonResult removeCoupon(Long touristId, Long deleteId) {
        Tourist tourist = repository.get(touristId);
        if (tourist != null && tourist.getStatus().equals(TouristStatus.Normal)) {
            tourist.getBindCoupons().removeIf(i -> deleteId.equals(i.getId()));
            repository.save(tourist);
            return ResultFactory.commonSuccess(deleteId);
        }
        return ResultFactory.commonError("该游客不存在，或者被禁用");
    }

    /**
     * 查看优惠券详细信息
     *
     * @param touristId
     * @param num
     * @return
     */
    @Override
    public CouponDto getByNum(Long touristId, String num) {
        Tourist tourist = repository.get(touristId);
        if (tourist != null) {
            for (Coupon c : tourist.getBindCoupons()) {
                if (c.getNum().equals(num)) {
                    return CouponFactory.convert(c);
                }
            }
        }
        return null;
    }

    /**
     * @param touristId
     * @return
     */
    public List<CouponDto> getAllCoupons(Long touristId) {
        List<CouponDto> couponDtos = new ArrayList<>();
        Tourist tourist = repository.get(touristId);
        if (tourist != null) {
            List<Coupon> coupons = tourist.getBindCoupons();
            coupons.stream().forEach(i -> couponDtos.add(CouponFactory.convert(i)));
        }
        return couponDtos;
    }

    /**
     * 获取所有优惠券，其中分：全部，有效，过期的
     *
     * @param touristId
     * @param status
     * @return
     */
    @Override
    public List<CouponDto> getAllCoupons(Long touristId, CouponStatus status) {
        List<CouponDto> couponDtos = new ArrayList<>();
        List<CouponDto> delDtos = new ArrayList<>();
        Tourist tourist = repository.get(touristId);
        if (tourist != null) {
            List<Coupon> coupons = tourist.getBindCoupons();
            if (coupons == null || coupons.isEmpty()) return couponDtos;
            if (!StringUtils.isNullOrWhiteSpace(status.toString())) {
                coupons.stream().forEach(i -> {
                    if (!i.getCouponStatus().equals(status))
                        delDtos.add(CouponFactory.convert(i));
                    couponDtos.add(CouponFactory.convert(i));
                });
            }
        }
        couponDtos.removeAll(delDtos);
        return couponDtos;
    }

    public List<CouponDto> getCouponsByStatus(Long touristId, CouponStatus status) {
        List<CouponDto> couponDtos = new ArrayList<>();
        List<CouponDto> delDtos = new ArrayList<>();
        Tourist tourist = repository.get(touristId);
        if (tourist != null) {
            List<Coupon> couponsList = tourist.getBindCoupons();
            if (couponsList == null || couponsList.isEmpty()) return couponDtos;


            if (status == CouponStatus.bind) {
                couponsList = couponsList.stream().filter(x -> x.getCouponStatus() == status && DateHelper.addDay(x.getExpireDate(),1).after(new Date())).collect(Collectors.toList());
            }
            else if (status == CouponStatus.Expired) {
                couponsList = couponsList.stream().filter(x -> x.getCouponStatus() != CouponStatus.Used && DateHelper.addDay(x.getExpireDate(),1).before(new Date())).collect(Collectors.toList());
            }
         else   if (status == CouponStatus.Used) {
                couponsList = couponsList.stream().filter(x -> x.getCouponStatus() == CouponStatus.Used).collect(Collectors.toList());
            }
            else {
                return couponDtos;
            }
            if (couponsList == null || couponsList.isEmpty()) return couponDtos;
            couponsList.stream().forEach(i -> {
                couponDtos.add(CouponFactory.convert(i));
            });
        }
        return couponDtos;
    }

    /**
     * 更新密码
     *
     * @param touristId
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @Override
    public CommonResult updatePassWord(Long touristId, String oldPassword, String newPassword) {
        Tourist tourist = repository.get(touristId);
        if (tourist == null || tourist.getStatus().equals(TouristStatus.Forbidden)) {
            return ResultFactory.commonError("该用户不存在，或者已经被禁用");
        }
        Boolean checkPassWord = tourist.checkPassword(tourist.getUserName(), oldPassword);
        if (!checkPassWord) {
            return new CommonResult(false, "请输入正确的原密码");
        }
        tourist.setPassword(newPassword);
        return ResultFactory.commonSuccess(repository.save(tourist));
    }
}
