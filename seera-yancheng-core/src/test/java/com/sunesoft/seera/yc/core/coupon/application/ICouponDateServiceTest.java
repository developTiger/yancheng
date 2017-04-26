package com.sunesoft.seera.yc.core.coupon.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.yc.core.coupon.application.dto.CouponTypeDto;
import com.sunesoft.seera.yc.core.coupon.domain.CouponTypeStatus;
import com.sunesoft.seera.yc.core.coupon.domain.criteria.CouponTypeCriteria;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ICouponDateServiceTest extends TestCase {
    @Autowired
    ICouponTypeService service;

    @Test
    public void testCreate() throws Exception {
        for(int i=0;i<10;i++) {
            CouponTypeDto dto = new CouponTypeDto();
            dto.setQuota(BigDecimal.valueOf(10+i));
            dto.setUseCondition(BigDecimal.valueOf(100));
            dto.setCount(10000L);
            dto.setCouponStart(new Date());
            dto.setCouponOver(DateHelper.addDay(new Date(), 30));
            dto.setOverTime(DateHelper.addDay(new Date(), 20));
            CommonResult c = service.create(dto);
            if (c.getIsSuccess()) {
                System.out.println(c.getId());
            } else {
                System.out.println(c.getMsg());
            }
        }
    }

    @Test
    public void testEdit() throws Exception {
        CouponTypeDto dto = new CouponTypeDto();
        dto.setId(2l);
        dto.setQuota(BigDecimal.valueOf(50));
        dto.setUseCondition(BigDecimal.valueOf(100));
        dto.setCount(10000L);
        dto.setCouponStart(new Date());
        dto.setCouponOver(DateHelper.addDay(new Date(), 30));
        dto.setOverTime(DateHelper.addDay(new Date(), 20));
        CommonResult c = service.create(dto);
        if (c.getIsSuccess()) {
            System.out.println(c.getId());
        } else {
            System.out.println(c.getMsg());
        }
    }

    @Test
    public void testSetStatus() throws Exception {
        CommonResult c=service.setStatus(1l, CouponTypeStatus.End);
        if (c.getIsSuccess()) {
            System.out.println(c.getId());
        } else {
            System.out.println(c.getMsg());
        }
    }

    @Test
    public void testSetStatus1() throws Exception {
        List<Long> list=new ArrayList<>();
        list.add(2l);
        list.add(3l);
        list.add(4l);
        CommonResult c=service.setStatus(list, CouponTypeStatus.End);
        if (c.getIsSuccess()) {
            System.out.println(c.getId());
        } else {
            System.out.println(c.getMsg());
        }
    }

    @Test
    public void testDelete() throws Exception {

        CommonResult c=service.delete(1l);
        if (c.getIsSuccess()) {
            System.out.println(c.getId()+"\t"+c.getIsSuccess());
        } else {
            System.out.println(c.getMsg());
        }
    }

    @Test
    public void testDelete1() throws Exception {
        List<Long> list=new ArrayList<>();
        list.add(2l);
        list.add(3l);
        list.add(4l);
        CommonResult c=service.delete(list);
        if (c.getIsSuccess()) {
            System.out.println(c.getId()+"\t"+c.getIsSuccess());
        } else {
            System.out.println(c.getMsg());
        }
    }

    @Test
    public void testCheck() throws Exception {
        CommonResult c=service.check(9l,5l);
        if (c.getIsSuccess()) {
            System.out.println(c.getId()+"\t"+c.getIsSuccess());
        } else {
            System.out.println(c.getMsg());
        }
    }

    @Test
    public void testDraw() throws Exception {
        CommonResult c=service.draw(9l, 5l);
        if (c.getIsSuccess()) {
            System.out.println(c.getId()+"\t"+c.getIsSuccess());
        } else {
            System.out.println(c.getMsg());
        }
    }

    @Test
    public void testFindPage() throws Exception {
        CouponTypeCriteria couponDateCriteria=new CouponTypeCriteria();
        couponDateCriteria.setStartQuota(BigDecimal.valueOf(11));
        PagedResult<CouponTypeDto> pg=service.findPage(couponDateCriteria);
        if(pg!=null){
            System.out.println(pg.getItems().size()+"\t"+pg.getPageNumber()+"\t"+pg.getPageSize()+"\t"+pg.getTotalItemsCount());
        }

    }
}