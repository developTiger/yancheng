package com.sunesoft.seera.yc.core.coupon;


import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.yc.core.coupon.application.ICouponService;
import com.sunesoft.seera.yc.core.coupon.application.dto.CouponDto;
import com.sunesoft.seera.yc.core.coupon.domain.CouponStatus;

import com.sunesoft.seera.yc.core.coupon.domain.criteria.CouponCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CouponServiceImplTest {
    @Autowired
    ICouponService service;

    /*
    经过测试CouponStatus.Valid的值会比预定义的值+1
     */
    @Test
    public void addCouponTest() {
        for (int i = 0; i < 6; i++) {
            CouponDto dto = new CouponDto();
            dto.setStaffId(null);
            dto.setCouponStatus(CouponStatus.Valid);
            dto.setUseCondition(BigDecimal.valueOf(100));
            dto.setQuota(BigDecimal.ONE);
            dto.setGqDate(DateHelper.formatDate(DateHelper.addDay(new Date(), 13)));

            Assert.isTrue(service.addCoupon(dto).getIsSuccess());
//            CommonResult c=service.addOrUpdateCoupon(dto);
//            if(c!=null){
//                System.out.println(c.getMsg()+"\t"+c.getIsSuccess());
//            }
        }

    }

    @Test
    public void UpdateCouponTest() {

        CouponDto dto = new CouponDto();
        dto.setId(1l);
        dto.setStaffId(5L);
        dto.setCouponStatus(CouponStatus.Valid);
        dto.setUseCondition(BigDecimal.ZERO);
        dto.setQuota(BigDecimal.TEN);
        dto.setGqDate(DateHelper.formatDate(DateHelper.addDay(new Date(), 1)));

        Assert.isTrue(service.updateCoupon(dto).getIsSuccess());
//            CommonResult c=service.addOrUpdateCoupon(dto);
//            if(c!=null){
//                System.out.println(c.getMsg()+"\t"+c.getIsSuccess());
//            }
    }


    @Test
    public void getByIdTest() {

        Assert.notNull(service.getById(1L));
        CouponDto dto = service.getById(1L);
        if (dto != null) {
            System.out.println(dto.getId()+"\t"+dto.getNum());
        }
    }

    @Test
    public void getByIdsTest() {
        List<Long> list = new ArrayList<>();
        list.add(3l);
        list.add(4l);
        Assert.notNull(service.getByIds(list));
        List<CouponDto> list1 = service.getByIds(list);
        if (list1 != null) {
            System.out.println(list1.size());

        }

    }

    @Test
    public void getByNumTest() {
//        Assert.notNull(service.getByNum("ce16f5cb-67c4-4725-98cc-61a6829fb101"));
        CouponDto dto = service.getByNum("0a4cba2f-cb09-4637-a695-75c9bb44775e");
        if (dto != null) {
            System.out.println(dto.getId()+"\t"+dto.getStaffId());
        }
    }

    @Test
    public void deleteByNUmsTest() {

        String s1 = "0a4cba2f-cb09-4637-a695-75c9bb44775e";
        String s2 = "7ea47d9d-d319-49a6-87f8-06c416bdf534";
        String s3 = "7c1f64ca-10d1-4b32-92a7-0c3b377357f2";
        List<String> list = new ArrayList<>();
        list.add(s1);
        list.add(s2);
        list.add(s3);
        Assert.isTrue(service.deleteByNums(list).getIsSuccess());
        CommonResult c = service.deleteByNums(list);
        if (c != null) {
            System.out.println(c.getId() + "\t" + c.getIsSuccess() + "\t" + c.getMsg());
        }


    }

    @Test
    public void deleteByIdsTest() {
        List<Long> list = new ArrayList<>();
        list.add(4l);
        list.add(5l);
        Assert.isTrue(service.deleteByIds(list).getIsSuccess());
        CommonResult c = service.deleteByIds(list);
        if (c != null) {
            System.out.println(c.getIsSuccess() + "\t" + c.getMsg());
        }

    }

    @Test
    public void setUsedTest() {
        CommonResult c = service.setUsed("a72d18bd-5415-47b1-9c7d-9f04727378fb");
        Assert.isTrue(c.getIsSuccess());
    }

    @Test
    public void bindStaffTest() {
        List<Long> list = new ArrayList<>();
        list.add(6l);
        list.add(7l);
        CommonResult c = service.bindStaff(2L, list);
        if (c != null) {
            System.out.println(c.getIsSuccess() + "\t" + c.getMsg());
        }

    }

    @Test
    public void getAllTest() {
        List<CouponDto> list = service.getAll();
        Assert.notNull(list);
        if (list != null) {
            System.out.println(list.size());
        }

    }

    @Test
    public void findCouponsTest() {
        CouponCriteria criteria = new CouponCriteria();
        criteria.setStaffRealName("xzl");
        PagedResult<CouponDto> pg = service.findCoupons(criteria);
        Assert.notNull(pg);
        if (pg != null) {
            System.out.println(pg.getItems().size() + "\t" + pg.getPageNumber() + "\t" + pg.getPageSize() + "\t" + pg.getTotalItemsCount());
        }
    }
}