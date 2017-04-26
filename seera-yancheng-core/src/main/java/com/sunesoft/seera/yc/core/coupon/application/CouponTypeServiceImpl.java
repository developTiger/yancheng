package com.sunesoft.seera.yc.core.coupon.application;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateFinder;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.coupon.application.dto.CouponTypeDto;
import com.sunesoft.seera.yc.core.coupon.application.dto.UploadCoupon;
import com.sunesoft.seera.yc.core.coupon.application.factory.CouponTypeFactory;
import com.sunesoft.seera.yc.core.coupon.domain.*;
import com.sunesoft.seera.yc.core.coupon.domain.criteria.CouponTypeCriteria;
import com.sunesoft.seera.yc.core.tourist.domain.ITouristRepository;
import com.sunesoft.seera.yc.core.tourist.domain.Tourist;
import com.sunesoft.seera.yc.core.tourist.domain.TouristStatus;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xiazl on 2016/8/31.
 */
@Service("iCouponTypeService")
public class CouponTypeServiceImpl extends GenericHibernateFinder implements ICouponTypeService {

    @Autowired
    ICouponTypeRepository repository;
    @Autowired
    ITouristRepository touristRepository;
    @Autowired
    ICouponRepository couponRepository;

    /**
     * 增加优惠券领取项
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult create(CouponTypeDto dto) throws Exception {
       CommonResult result= check(dto);
        if(!result.getIsSuccess()){
            return result;
        }
        try {
            return ResultFactory.commonSuccess(repository.save(CouponTypeFactory.convert(dto)));
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }
    }

    /**
     * 修改优惠券领取项
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult edit(CouponTypeDto dto) throws Exception {
        check(dto);
        CouponType couponDate = repository.get(dto.getId());
        if (couponDate == null || !couponDate.getIsActive()) return ResultFactory.commonError("该优惠券领取项已经不存在");
        try {
            return ResultFactory.commonSuccess(repository.save(CouponTypeFactory.convert(dto, couponDate)));
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }
    }


    public  List<Coupon> getAllCoupon(){
        Criteria criteria = getSession().createCriteria(Coupon.class);
        criteria.add(Restrictions.eq("isActive", true));
        return criteria.list();
    }

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    public CouponTypeDto getById(Long id) {
        CouponType date = repository.get(id);
        if (date != null && date.getIsActive()) return CouponTypeFactory.convert(date);
        return null;
    }

    /**
     * 设置优惠券领取的状态
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    public CommonResult setStatus(Long id, CouponTypeStatus status) {
        return repository.setStatus(id, status);
    }

    /**
     * 设置优惠券领取的状态
     *
     * @param ids
     * @param status
     * @return
     */
    @Override
    public CommonResult setStatus(List<Long> ids, CouponTypeStatus status) {
        if (ids == null || ids.size() == 0) return ResultFactory.commonError("请选择优惠券活动项");
        return repository.setStatus(ids, status);
    }

    /**
     * 单个删除优惠券领取活动项
     *
     * @param id
     * @return
     */
    @Override
    public CommonResult delete(Long id) {
        CouponType couponDate = repository.get(id);
        if (couponDate == null || !couponDate.getIsActive()) return ResultFactory.commonError("该优惠券领取项已经不存在");
        repository.delete(id);
        return ResultFactory.commonSuccess(id);
    }

    /**
     * 删除优惠券领取活动的状态
     *
     * @param ids
     * @return
     */
    @Override
    public CommonResult delete(List<Long> ids) {
        Criteria criteria = getSession().createCriteria(CouponType.class);
        criteria.add(Restrictions.eq("isActive", true));
        criteria.add(Restrictions.in("id", ids));
        List<CouponType> list = criteria.list();
        if (list != null && list.size() > 0) {
            list.stream().forEach(i -> repository.delete(i.getId()));
            return ResultFactory.commonSuccess();
        }
        return ResultFactory.commonError("请选择有效的优惠券活动项");
    }

    /**
     * 检查给游客是否领取
     *
     * @param id
     * @return
     */
    @Override
    public CommonResult check(Long id, Long couponTypeId) {
        Tourist tourist = touristRepository.get(id);
        if (tourist != null && tourist.getIsActive()) {
            if (tourist.getBindCoupons() != null && tourist.getBindCoupons().size() > 0 && tourist.getBindCoupons().stream().anyMatch(i -> i.getCouponTypeId() != null && i.getCouponTypeId().equals(couponTypeId))) {
                return ResultFactory.commonError("您已领取该优惠券");
            }
        }
        return ResultFactory.commonSuccess();
    }

    /**
     * 领取优惠券
     *
     * @param touristId
     * @param couponTypeId
     * @return
     */
    @Override
    public CommonResult draw(Long touristId, Long couponTypeId) {
        Tourist tourist = touristRepository.get(touristId);
        //检查游客
        if (tourist == null || tourist.getStatus().equals(TouristStatus.Forbidden) || !tourist.getIsActive())
            return ResultFactory.commonError("该游客被禁止或者不存在");
        //检查是否领取过 先去掉，要改回来
//        CommonResult c = check(touristId, couponTypeId);
//        if (!c.getIsSuccess()) return ResultFactory.commonError(c.getMsg());
        //领取优惠券
        CouponType couponType = repository.get(couponTypeId);
        if (couponType != null && couponType.getIsActive() && (couponType.getOverTime() == null || couponType.getOverTime().after(new Date())||couponType.getOverTime().getDay()==new Date().getDay()) && (
                couponType.getCount() == 9999 || couponType.getCount() > 0)) {
            //创建优惠券
            Coupon coupon = new Coupon();
            coupon.setExpireDate(couponType.getCouponOver());
            coupon.setCouponTypeId(couponTypeId);
            coupon.setQuota(couponType.getQuota());
            coupon.setUseCondition(couponType.getUseCondition());
            coupon.setCouponStatus(CouponStatus.Valid);
            couponRepository.save(coupon);
            //绑定优惠券
            tourist.bindCoupons(coupon);
            touristRepository.save(tourist);
            if (couponType.getCount() != 9999)
                //数量限制的情况下
                couponType.setCount(couponType.getCount() - 1);
            //返回的是优惠券活动项的id// TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            //这里最好加一个回滚功能
            return ResultFactory.commonSuccess(repository.save(couponType));
        }
        return ResultFactory.commonError("领取优惠券失败");
    }

    /**
     * 优惠券的活动分页查询
     *
     * @param couponDateCriteria
     * @return
     */
    @Override
    public PagedResult<CouponTypeDto> findPage(CouponTypeCriteria couponDateCriteria) {
        PagedResult<CouponType> pg = repository.findPage(couponDateCriteria);
        if (pg == null) return new PagedResult<CouponTypeDto>(1, 10);
        return CouponTypeFactory.convertpg(pg);
    }

    @Override
    public CommonResult uploadManagerCoupons(List<UploadCoupon> uploadCoupons) {
        List<String> phoneNos = new ArrayList<>();
        Long id =uploadCoupons.get(0).getCouponCode();
        uploadCoupons.stream().forEach(x->phoneNos.add(x.getPhoneNo()));
        List<Tourist> tt =touristRepository.findTouristsByPhone(phoneNos);
        CouponType couponType = repository.get(id);
        for(Tourist tourist :tt){
            for(UploadCoupon coupon :uploadCoupons){
                if(coupon.getPhoneNo().equals(tourist.getMobilePhone())){
                    bindCoupons( tourist, coupon.getTuName(),couponType, coupon.getCouponCount(), false,coupon.getEndDate());
                    break;
                }
            }
         }
        return ResultFactory.commonSuccess();
    }


    public CommonResult bindCouponsByIds(Long touristId,Long couponTypeId,Integer count){
        Tourist tourist = touristRepository.get(touristId);

        CouponType couponType = repository.get(couponTypeId);
        CommonResult c = check(touristId, couponTypeId);
        if (!c.getIsSuccess()) return ResultFactory.commonError(c.getMsg());
        if (couponType != null && couponType.getIsActive() && (couponType.getOverTime() == null || couponType.getOverTime().after(new Date())) && (
                couponType.getCount() == 9999 || couponType.getCount() > 0)) {
            String cpRealName = "";
            if(!StringUtils.isNullOrWhiteSpace(tourist.getUserName()))
                cpRealName = tourist.getUserName();
            else
                cpRealName = StringUtils.isNullOrWhiteSpace(tourist.getWxName())?"":tourist.getWxName();

            bindCoupons( tourist, cpRealName,couponType, count, true,couponType.getCouponOver());
        }
        return ResultFactory.commonSuccess();
    }

    public CommonResult bindCoupons(Tourist tourist,String realName,CouponType couponType,Integer count,Boolean reduceTotalCount,Date endDate){
        for(int i=0;i<count;i++) {
            Coupon coupon = new Coupon();
            coupon.setTouristName(realName);
            coupon.setExpireDate(endDate);
            coupon.setCouponTypeId(couponType.getId());
            coupon.setQuota(couponType.getQuota());
            coupon.setUseCondition(couponType.getUseCondition());
            coupon.setCouponStatus(CouponStatus.Valid);
            tourist.bindCoupons(coupon);
        }
        touristRepository.save(tourist);

        if(reduceTotalCount) {
            if (couponType.getCount() != 9999)
                //数量限制的情况下
                couponType.setCount(couponType.getCount() - 1);
            //返回的是优惠券活动项的id// TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            //这里最好加一个回滚功能
        }
        return ResultFactory.commonSuccess(repository.save(couponType));

    }

    @Override
    public List<CouponType> getAll() {
        Criteria criteria = getSession().createCriteria(CouponType.class);
        criteria.add(Restrictions.eq("isActive", true));
        return criteria.list();
    }

    /**
     * 对传传入的dto数据进行检测
     *
     * @param dto
     * @throws Exception
     */
    private CommonResult check(CouponTypeDto dto) throws Exception {
        if (dto == null) throw new Exception("出入数据不能为null");
        if (dto.getQuota().equals(BigDecimal.ZERO) || dto.getQuota().compareTo(BigDecimal.ZERO) < 0)

            return ResultFactory.commonError("出入的优惠券额度为0无意义");
        if (!dto.getStartTime().before(dto.getOverTime()))
            return ResultFactory.commonError("该优惠券的领取活动时间设置不合理");
        if (!dto.getCouponStart().before(dto.getCouponOver()) )
            return ResultFactory.commonError("该优惠券的领取活动优惠券时间设置不合理");
        return ResultFactory.commonSuccess();
    }
}
