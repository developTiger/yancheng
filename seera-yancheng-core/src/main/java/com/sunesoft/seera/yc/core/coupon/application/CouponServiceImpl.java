package com.sunesoft.seera.yc.core.coupon.application;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateFinder;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.Factory;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.coupon.application.dto.CouponDto;
import com.sunesoft.seera.yc.core.coupon.application.dto.UploadCoupon;
import com.sunesoft.seera.yc.core.coupon.application.factory.CouponFactory;
import com.sunesoft.seera.yc.core.coupon.domain.Coupon;
import com.sunesoft.seera.yc.core.coupon.domain.CouponStatus;
import com.sunesoft.seera.yc.core.coupon.domain.ICouponRepository;
import com.sunesoft.seera.yc.core.coupon.domain.criteria.CouponCriteria;
import com.sunesoft.seera.yc.core.manager.domain.IManagerRepository;
import com.sunesoft.seera.yc.core.manager.domain.InnerManager;
import com.sunesoft.seera.yc.core.parameter.application.factory.DtoFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhaowy on 2016/7/11.
 */
@Service("iCouponService")
public class CouponServiceImpl extends GenericHibernateFinder implements ICouponService {

    @Autowired
    ICouponRepository repository;
    @Autowired
    IManagerRepository managerRepository;

    /**
     * 增加或者修改优惠券
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult addCoupon(CouponDto dto) {
        //新增只能是有效的，未绑定的
        dto.setCouponStatus(CouponStatus.Valid);
        if (dto == null || dto.getCouponStatus() == null || !dto.getCouponStatus().equals(CouponStatus.Valid)) {
            return ResultFactory.commonError("您增加的优惠券属于无效的，添加无意义");
        }
        Coupon coupon = CouponFactory.convertFromDto(dto);
//        //设置manager
//        if (dto.getStaffId() != null) {
//            InnerManager manager = managerRepository.get(dto.getStaffId());
//            if (manager != null && manager.getIsActive() && manager.isStatus()) {
//                if(!StringUtils.isNullOrWhiteSpace(manager.getRealName()))
//                    dto.setRealName(manager.getRealName());
//                coupon.setRefStaff(manager);
//            }
//        }
        return ResultFactory.commonSuccess(repository.save(coupon));
    }

    /**
     * 增加或者修改优惠券
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult updateCoupon(CouponDto dto) {
        Coupon coupon = repository.get(dto.getId());
        if (coupon == null || !coupon.getCouponStatus().equals(CouponStatus.bind) || !coupon.getIsActive()) {
            return ResultFactory.commonError("该优惠券已经失效");
        }
        dto.setNum(coupon.getNum());
        coupon = CouponFactory.convertFromDto(dto,coupon);
        //set manager
        if (dto.getStaffId() != null) {
            InnerManager manager = managerRepository.get(dto.getStaffId());
            if (manager != null && manager.isStatus()) {
                coupon.setRefStaff(manager);
            }
        }
        return ResultFactory.commonSuccess(repository.save(coupon));
    }

    /**
     * 根据id获取优惠券
     *
     * @param id
     * @return
     */
    @Override
    public CouponDto getById(Long id) {
        CouponDto dto = new CouponDto();
        Coupon coupon = repository.get(id);
        if (coupon != null && coupon.getIsActive()) {
            dto = CouponFactory.convert(coupon);
        }
        return dto;
    }

    /**
     * 根据id获取优惠券
     *
     * @param ids
     * @return
     */
    @Override
    public List<CouponDto> getByIds(List<Long> ids) {
        List<CouponDto> dtos = new ArrayList<>();
        if (ids == null || ids.size() == 0)
            return dtos;
        Criteria criterion = getSession().createCriteria(Coupon.class);
        criterion.add(Restrictions.eq("isActive", true));
        criterion.add(Restrictions.in("id", ids));
        List<Coupon> list = criterion.list();
        if (list != null && list.size() > 0) {
            list.stream().forEach(i -> {
                CouponDto dto = new CouponDto();
                dto = CouponFactory.convert(i);
                dtos.add(dto);
            });
        }
        return dtos;

    }

    /**
     * 根据num获取优惠券
     *
     * @param num
     * @return
     */
    @Override
    public CouponDto getByNum(String num) {
        CouponDto dto = new CouponDto();
        Coupon c = repository.getByNum(num);
        if (c != null && c.getIsActive())
            dto = CouponFactory.convert(c);

        return dto;
    }



    /**
     * 根据nums删除优惠券
     *
     * @param nums
     * @return
     */
    @Override
    public CommonResult deleteByNums(List<String> nums) {
        if (nums == null || nums.size() <= 0) {
            return ResultFactory.commonError("请选择要删除的优惠券");
        }
        nums.stream().forEach(num -> {
            Coupon coupon = repository.getByNum(num);
            if (coupon != null) {
                coupon.setIsActive(false);
                coupon.setLastUpdateTime(new Date());
                repository.save(coupon);
            }
        });
        return ResultFactory.commonSuccess();
    }

    /**
     * 根据ids删除优惠券
     *
     * @param ids
     * @return
     */
    @Override
    public CommonResult deleteByIds(List<Long> ids) {
        if (ids == null || ids.size() <= 0) {
            return ResultFactory.commonError("请选择要删除的优惠券");
        }
        ids.stream().forEach(id -> {
            Coupon coupon = repository.get(id);
            if (coupon != null) {
                coupon.setIsActive(false);
                coupon.setLastUpdateTime(new Date());
                repository.save(coupon);
            }
        });
        return ResultFactory.commonSuccess();
    }

    /**
     * 根据id删除优惠券(这里指定为未绑定游客的优惠券可以删除)
     * @param id
     * @return
     */
    public CommonResult deleteById(Long id){
        Coupon coupon=repository.get(id);
        if(coupon==null||!coupon.getIsActive())return ResultFactory.commonError("该优惠券不存在");
        if(coupon.getCouponStatus().equals(CouponStatus.bind))return ResultFactory.commonError("该优惠券已经被游客认领");
        coupon.setIsActive(false);
        coupon.setLastUpdateTime(new Date());
        return  ResultFactory.commonSuccess(repository.save(coupon));
    }

    /**
     * 设置使用过
     *
     * @param num
     * @return
     */
    @Override
    public CommonResult setUsed(String num) {
        Coupon coupon = repository.getByNum(num);
        if (coupon != null && coupon.getIsActive() && coupon.getCouponStatus().equals(CouponStatus.Valid)) {
            coupon.SetCouponUsed();
            return ResultFactory.commonSuccess(repository.save(coupon));
        }
        return ResultFactory.commonError("该编号的优惠券不存在");
    }

    /**
     * 绑定员工
     *
     * @param saffId
     * @param ids
     * @return
     */
    @Override
    public CommonResult bindStaff(Long saffId, List<Long> ids) {
        if (saffId == null) return ResultFactory.commonError("请选择员工");
        if (ids == null || ids.size() <= 0) return ResultFactory.commonError("请选择优惠券");
        List<CouponDto> list = new ArrayList<>();
        InnerManager manager = managerRepository.get(saffId);
        if (manager == null || !manager.getIsActive() || !manager.isStatus()) {
            return ResultFactory.commonError("该员工不存在，或者被禁用");
        }
        ids.stream().forEach(id -> {
            Coupon coupon = repository.get(id);
            if (coupon != null && coupon.getIsActive() && coupon.getRefStaff() == null && coupon.getCouponStatus().equals(CouponStatus.Valid) && coupon.getExpireDate().after(new Date())) {
                coupon.setRefStaff(manager);
                coupon.setLastUpdateTime(new Date());
                repository.save(coupon);
            }
        });
        return ResultFactory.commonSuccess();
    }

    /**
     * 获取所有优惠券
     *
     * @return
     */
    @Override
    public List<CouponDto> getAll() {
        Criteria criterion = getSession().createCriteria(Coupon.class);
        criterion.add(Restrictions.eq("isActive", true));
        List<Coupon> list = criterion.list();
        List<CouponDto> dtos = new ArrayList<>();
        list.stream().forEach(i -> {
            CouponDto dto = new CouponDto();
            dto = CouponFactory.convert(i);
            dtos.add(dto);
        });
        return dtos;
    }

    /**
     * 分页查询
     *
     * @param criteria
     * @return
     */
    @Override
    public PagedResult<CouponDto> findCoupons(CouponCriteria criteria) {
        PagedResult<Coupon> pg = repository.findCoupons(criteria);
        if(pg!=null&&pg.getItems()!=null&&pg.getItems().size()>0)
        return  CouponFactory.convert(pg);
        return new PagedResult<CouponDto>(1,10);

    }

    /**
     * 分页查询(仅限管理员的票)
     * @param criteria
     * @return
     */
    public PagedResult<CouponDto> findManagerCoupons(CouponCriteria criteria){
        PagedResult<Coupon> pg = repository.findCoupons(criteria);
        List<Coupon> list=new ArrayList<>();
        if(pg!=null&&pg.getItems()!=null&&pg.getItems().size()>0) {
            //管理员的票归档
            pg.getItems().stream().forEach(i->{
                if(i!=null&&i.getRefStaff()!=null)list.add(i);
            });
            pg=new PagedResult<Coupon>(list,pg.getPageNumber(),pg.getPageSize(),pg.getTotalItemsCount());
            return CouponFactory.convert(pg);
        }
        return new PagedResult<CouponDto>(1,10);
    }





}
