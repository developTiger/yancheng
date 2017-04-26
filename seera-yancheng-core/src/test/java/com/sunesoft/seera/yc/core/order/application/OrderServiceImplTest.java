package com.sunesoft.seera.yc.core.order.application;

import com.mchange.v2.sql.filter.SynchronizedFilterDataSource;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.yc.core.coupon.domain.Coupon;
import com.sunesoft.seera.yc.core.eHr.application.uAuth.SysUserTest;
import com.sunesoft.seera.yc.core.order.application.criteria.CreateOrder;
import com.sunesoft.seera.yc.core.order.application.criteria.OrderCriteria;
import com.sunesoft.seera.yc.core.order.application.dtos.OrderDto;
import com.sunesoft.seera.yc.core.order.application.dtos.OrderProductDto;
import com.sunesoft.seera.yc.core.order.domain.OrderProductStatus;
import junit.framework.TestCase;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Criteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.SetOverrideType;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.util.ServletContextPropertyUtils;
import sun.java2d.SurfaceDataProxy;
import sun.security.pkcs11.wrapper.CK_SSL3_KEY_MAT_OUT;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.time.LocalDate;
import java.util.*;
import java.util.zip.CheckedOutputStream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class OrderServiceImplTest extends TestCase {

    @Autowired
    IOrderService service;

    @Test
    public void testCreateOrder() throws Exception {
        CreateOrder order = new CreateOrder();
        //order.setCouponId(6L);
        order.setTouristId(1l);
        order.setFetcherDtoId(10l);
//        List<OrderProductSchedule> schedules = new ArrayList<>();
//        OrderProductSchedule orderProductSchedule = new OrderProductSchedule();
//        orderProductSchedule.setProductId(61l);
//        orderProductSchedule.setTourScheduleDate(DateUtils.addDays(new Date(),2));
//        schedules.add(orderProductSchedule);
//        order.setSchedules(schedules);
//        Map<Long, Integer> map = new HashMap<>();
//        //map.put(48L, 1);
//        map.put(61l, 1);
//        order.setProductIdToCount(map);
        UniqueResult c = service.createOrder(order);
        if (c != null) {
            System.out.println(c.getIsSuccess());
        } else {
            System.out.println("no");
        }


    }

    @Test
    public void ccccccccc() throws Exception {


        service.gjpCreateOrder("5","yc123456",1);


    }


    @Test
    public void testProductItemTake() throws Exception {
        CommonResult c = service.productItemTake(5l);//TODO 订单号
        if (c.getIsSuccess()) {
            System.out.println(c.getId());
        } else {
            System.out.println(c.getMsg());
        }
    }

    @Test
    public void testOrderComment() throws Exception {
        CommonResult c = service.productItemTake(3l);//TODO 订单号
        if (c != null) {
            System.out.println(c.getIsSuccess());
        } else {
            System.out.println("no");
        }
    }

    @Test
    public void testPayOrder() throws Exception {

    }

    @Test
    public void testCancelOrder() throws Exception {
        CommonResult c = service.cancelOrder(3l);
        if (c != null) {
            System.out.println(c.getIsSuccess());
        } else {
            System.out.println("no");
        }
    }

    @Test
    public void testGetOrder() throws Exception {
        OrderDto dto = service.getOrder(4l);
        if (dto != null) {
            System.out.println(dto.getOrderPrice());
        } else {
            System.out.println("no");
        }
    }

    @Test
    public void testGetOrders() throws Exception {
        OrderCriteria criteria = new OrderCriteria();
        criteria.setNum("1");
        PagedResult<OrderDto> pg = service.getOrders(criteria);
        if (pg != null) {
            System.out.println(pg.getItems().size());
        } else {
            System.out.println("no");
        }
    }

    @Test
    public void testReturnApprove() throws Exception {
        CommonResult c = service.returnApprove(3L);
        if (c != null) {
            System.out.println(c.getIsSuccess());
        } else {
            System.out.println("no");
        }

    }

    @Test
    public void testMeal() throws Exception {
        CommonResult c = service.meal(5l, DateHelper.addDay(new Date(), 1), DateHelper.addDay(new Date(), 1));
        if (c != null) {
            System.out.println(c.getIsSuccess());
        } else {
            System.out.println("no");
        }
    }

    @Test
    public void testMealCheck() throws Exception {
        CommonResult c = service.mealCheck("11", 5l, false);
        if (c != null) {
            System.out.println(c.getIsSuccess());
        } else {
            System.out.println("no");
        }
    }

    @Test
    public void testFindOrderProduct() throws Exception {
        List<OrderProductDto> list = service.findOrderProduct(OrderProductStatus.returned);
        if (list != null) {
            System.out.println(list.size());
        } else {
            System.out.println("no");
        }
    }

    @Test
    public void streamTest() throws Exception {
        List<A> a = new ArrayList<>();
        a.add(new A("1", "a1"));
        a.add(new A("2", "a2"));
        a.add(new A("3", "a3"));

        a.stream().forEach(i ->
                System.out.println("id:" + i.getId() + ",name:" + i.getName()));

        a.stream().forEach(i ->
        {
            if(i.getId() == "1")
            a.remove(i);
        });
        a.stream().forEach(i ->
                System.out.println("id:" + i.getId() + ",name:" + i.getName()));
    }

    public class A {
        String id;
        String name;

        public A() {
        }

        public A(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}