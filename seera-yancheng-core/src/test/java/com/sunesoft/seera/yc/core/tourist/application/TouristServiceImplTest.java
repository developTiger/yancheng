package com.sunesoft.seera.yc.core.tourist.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.NumGenerator;
import com.sunesoft.seera.yc.core.coupon.application.ICouponService;
import com.sunesoft.seera.yc.core.coupon.application.dto.CouponDto;
import com.sunesoft.seera.yc.core.coupon.domain.CouponStatus;
import com.sunesoft.seera.yc.core.product.domain.criteria.ProductCriteria;
import com.sunesoft.seera.yc.core.tourist.application.dtos.FetcherDto;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristDto;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristSimpleDto;
import com.sunesoft.seera.yc.core.tourist.application.factory.TouristFactory;
import com.sunesoft.seera.yc.core.tourist.domain.Tourist;
import com.sunesoft.seera.yc.core.tourist.domain.TouristGender;
import com.sunesoft.seera.yc.core.tourist.domain.TouristStatus;
import com.sunesoft.seera.yc.core.tourist.domain.criteria.TouristCriteria;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TouristServiceImplTest extends TestCase {
    @Autowired
    private ITouristService service;
    @Autowired
    private ICouponService couponService;

    @Test
    public void testRegister() throws Exception {
        CommonResult c = service.register("t", "t");
        if (c.getIsSuccess()) {
            System.out.println(c.getIsSuccess());
        }

    }


    @Test
    public void timeSpanTest() throws Exception {
        Date date = DateHelper.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd hhmmss");
        Date orginalDate = DateHelper.parse("2016-01-01 00:00:00");
        long time = date.getTime() - orginalDate.getTime();
        for (int i = 0; i < 10; i++) {
            System.out.println("time&num:" + NumGenerator.generate(new Date(), 2));
            //System.out.println("start&time:" + NumGenerator.generate("cn"));
            //System.out.println("num:" + NumGenerator.generate(3));
        }
    }

    @Test
    public void testCreate() throws Exception {
//        for (int i = 0; i < 1; i++) {
//            Tourist tourist = new Tourist("name-"+UUID.randomUUID(),"000000");
//            tourist.setEmail(UUID.randomUUID()+"@sunesoft.com");
//            tourist.setWxName("wxName-" + UUID.randomUUID());
//            tourist.setGender(TouristGender.Unknown);
//
//            TouristDto dto = TouristFactory.convert(tourist, TouristDto.class);
//            dto.setPassword("000000");
//            Assert.isTrue(service.create(dto).getIsSuccess());
//        }

        TouristDto dto = new TouristDto();
        dto.setStatus(TouristStatus.Normal);
        dto.setUserName("n2");
        dto.setRealName("xx");
        dto.setMobilePhone("9234519456");
        CouponDto c = couponService.getById(3l);
        List<CouponDto> dtos = new ArrayList<>();
        dtos.add(c);
        dto.setBindCouponDtos(dtos);
        FetcherDto f = new FetcherDto();
        f.setRealName("f");
        f.setMobilePhone("11111121");
        f.setIdCardNo("20160808112231");
        List<FetcherDto> fs = new ArrayList<>();
        fs.add(f);
        dto.setFetcherDtos(fs);
        CommonResult cr = service.create(dto);
        if (cr != null) {
            System.out.println(cr.getIsSuccess() + "\t" + cr.getMsg());
        } else {
            System.out.println("no");
        }
    }

    @Test
    public void testCheck() throws Exception {
//pwd.处有疑问
        CommonResult c = service.check("3@qq.com");
        if (c != null) {
            System.out.println("c.getId():" + c.getId() + "\t" + "c.getMsg():" + c.getMsg() + "\t" + "c.getIsSuccess():" + c.getIsSuccess());
        } else {
            System.out.println("no");
        }

    }

    @Test
    public void testCheckOpenId() throws Exception {
//pwd.处有疑问
        UniqueResult<TouristSimpleDto> c = service.checkOpenId("489148198");
        if (c != null) {
            System.out.println("c.getId():" + c.getT().getId() + "\t" + "c.getMsg():" + c.getMsg() + "\t" + "c.getIsSuccess():" + c.getIsSuccess());
        } else {
            System.out.println("no");
        }

    }


    @Test
    public void testlogin() throws Exception {
        UniqueResult c = service.login("t3", "123456");
        if (c != null) {
            System.out.println(c.getIsSuccess());
        } else {
            System.out.println("no");
        }

    }

    @Test
    public void testUpdate() throws Exception {
        TouristDto dto = new TouristDto();
        dto.setStatus(TouristStatus.Normal);
        dto.setId(5l);
        dto.setUserName("x5");
        dto.setRealName("xx");
        dto.setMobilePhone("1234516956");
        CommonResult c = service.update(dto);
        if (c != null) {
            System.out.println(c.getIsSuccess());
        } else {
            System.out.println("no");
        }
    }


    @Test
    public void testGetTourist() throws Exception {
        UniqueResult<TouristDto> ur = service.getTourist(11L);//caoyn@sunesoft.com  15261115854
        if (ur != null && ur.getT() != null) {
            System.out.println(ur.getT().getUserName() + "\t" + ur.getT().getStatus() + "\t");
        } else {
            System.out.println("no");
        }
    }

    @Test
    public void testGetTourist1() throws Exception {
        UniqueResult<TouristDto> ur1 = service.getTourist("1");
        UniqueResult<TouristDto> ur2 = service.getTourist("1@qq.com");
        UniqueResult<TouristDto> ur3 = service.getTourist("15261115854");
        if (ur1 != null) {
            System.out.println("用户名获取\t" + ur1.getIsSuccess());
        } else {
            System.out.println("no");
        }if (ur2 != null) {
            System.out.println("邮箱获取\t" + ur2.getIsSuccess());
        } else {
            System.out.println("no");
        }if (ur3 != null) {
            System.out.println("手机号获取\t" + ur3.getIsSuccess());
        } else {
            System.out.println("no");
        }
    }

    @Test
    public void testSetStatus() throws Exception {
        CommonResult c = service.setStatus(1L, TouristStatus.Forbidden);
        if (c != null) {
            System.out.println("c.getId():" + c.getId() + "\t" + "c.getMsg():" + c.getMsg() + "\t" + "c.getIsSuccess():" + c.getIsSuccess());
        } else {
            System.out.println("no");
        }

    }


    @Test
    public void testFindSimpleTourists() throws Exception {
        TouristCriteria criteria = new TouristCriteria();
        criteria.setOrderByProperty("mobilePhone");
        criteria.setAscOrDesc(false);
        PagedResult<TouristSimpleDto> pagedResult = service.findSimpleTourists(criteria);
        if (pagedResult.getTotalItemsCount() > 0)
            pagedResult.getItems().stream().forEach(x -> System.out.print(x.getMobilePhone() + "|" + x.getUserName() + "|" + x.getEmail() + "\n"));
    }

    @Test
    public void testFindTourists() throws Exception {
        TouristCriteria criteria = new TouristCriteria();
        criteria.setOrderByProperty("mobilePhone");
        criteria.setAscOrDesc(false);
        PagedResult<TouristDto> pagedResult = service.findTourists(criteria);
        if (pagedResult.getTotalItemsCount() > 0)
            pagedResult.getItems().stream().forEach(x -> System.out.print(x.getMobilePhone() + "|" + x.getUserName() + "|" + x.getEmail() + "\n"));
    }

    @Test
    public void testcreate3() {
        FetcherDto dto = new FetcherDto();
        dto.setRealName("x1");
        dto.setMobilePhone("123456");
        CommonResult c = service.create(7l, dto);
        if (c != null) {
            System.out.println(c.getIsSuccess());
        } else {
            System.out.println("no");
        }
    }

    @Test
    public void testupdate3() {
        FetcherDto dto = new FetcherDto();
        dto.setId(3l);
        dto.setRealName("33");
        dto.setMobilePhone("123456");
        CommonResult c = service.update(7l, dto);
        if (c != null) {
            System.out.println(c.getIsSuccess());
        } else {
            System.out.println("no");
        }
    }

    @Test
    public void testremove() {
        List<Long> list = new ArrayList<>();
        list.add(6L);
        CommonResult c = service.remove(2L, list);
        if (c != null) {
            System.out.println(c.getIsSuccess());
        } else {
            System.out.println("no");
        }
    }

    @Test
    public void testgetByFetcherId() {

        FetcherDto dto = service.getByFetcherId(7l, 3l);
        if (dto != null) {
            System.out.println(dto.getId() + "\t" + dto.getRealName());
        }

    }

    @Test
    public void testgetAllFetchers() {
        List<FetcherDto> dtos = service.getAllFetchers(2l);
        if (dtos != null) {
            System.out.println(dtos.size());
        }
    }

    @Test
    public void testbindCoupon() {
        CommonResult c = service.bindCoupon(7l, "f5d40b0e-1154-4d09-b4fd-d7d09e3e3ff6");
        if (c != null) {
            System.out.println(c.getIsSuccess());
        } else {
            System.out.println("no");
        }
    }

    @Test
    public void testremoveCoupon() {
        List<String> list = new ArrayList<>();
        list.add("b27b5c9f-b485-4298-a029-608b3c055a9f");
        list.add("f5d40b0e-1154-4d09-b4fd-d7d09e3e3ff6");
        CommonResult c = service.removeCoupon(2l, list);
        if (c != null) {
            System.out.println(c.getIsSuccess());
        } else {
            System.out.println("no");
        }
    }

    @Test
    public void testgetByNum() {
        CouponDto dto = service.getByNum(2l, "f5d40b0e-1154-4d09-b4fd-d7d09e3e3ff6");
        if (dto != null) {
            System.out.println(dto.getId());
        } else {
            System.out.println("no");
        }
    }

    @Test
    public void testgetAllCoupons() {
        List<CouponDto> list = service.getAllCoupons(2l, CouponStatus.bind);
        if (list != null) {
            System.out.println(list.size());
        }

    }

    @Test
    public void updateNoSames() {
        FetcherDto dto = new FetcherDto();
        dto.setId(32l);
        dto.setRealName("x");
        dto.setMobilePhone("12345");
        dto.setIdCardNo("xx");
        CommonResult c = service.updateNoSame(9l, dto);
        if (c != null) {
            if (c.getIsSuccess())
                System.out.println(c.getIsSuccess());
            else
                System.out.println(c.getMsg());
        }
    }

    @Test
    public void updateNSames() {
        CommonResult c = service.setDefault(9L, 32L);
        if (c != null) {
            if (c.getIsSuccess())
                System.out.println(c.getIsSuccess());
            else
                System.out.println(c.getMsg());
        }
    }
}