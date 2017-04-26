package com.sunesoft.seera.yc.core.activity;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.yc.core.activity.application.IActivityService;
import com.sunesoft.seera.yc.core.activity.application.dtos.ActivityDto;
import com.sunesoft.seera.yc.core.activity.application.dtos.ActivitySimpleDto;
import com.sunesoft.seera.yc.core.activity.domain.ActivityStatus;
import com.sunesoft.seera.yc.core.activity.domain.ActivityType;
import com.sunesoft.seera.yc.core.activity.domain.creteria.ActivityCriteria;
import com.sunesoft.seera.yc.core.product.application.IProductService;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductDto;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ActivityServiceImplTest extends TestCase {
    @Autowired
    IActivityService service;
    @Autowired
    IProductService productService;

    @Test
    public void testCreate() throws Exception {
        for(int i=0;i<4;i++) {
            ActivityDto dto = new ActivityDto();
            ProductDto p = productService.get(91l);
            System.out.println(p.getId());

            dto.setName("王祖蓝Stop");
            dto.setProductDto(p);
            dto.setStartTime(new Date());
            dto.setEndTime(DateHelper.addDay(new Date(), 37));
            dto.setNotice("请付款");
            dto.setContent("王祖蓝也得付款");
            dto.setActivityStatus(ActivityStatus.Stop);
            dto.setType(ActivityType.HolidayEvent);
            CommonResult c = service.create(dto);
            if (c != null) {
                System.out.println(c.getIsSuccess());
            } else System.out.println("no");
        }

    }
    @Test
    public void testEdit() throws Exception {
        ActivityDto dto = new ActivityDto();

        ProductDto p = productService.get(7l);
        System.out.println(p.getId());
        dto.setId(1L);
        dto.setName("xx");
        dto.setProductDto(p);
        dto.setStartTime(new Date());
        dto.setEndTime(DateHelper.addDay(new Date(), 7));
        dto.setType(ActivityType.ChildEvent);
        CommonResult c = service.edit(dto);
        if (c != null) {
            System.out.println(c.getIsSuccess());
        } else System.out.println("no");

    }
    @Test
    public void testRemove() throws Exception {
        List<Long> ids=new ArrayList<>();
        ids.add(15l);
        ids.add(14l);
        ids.add(13l);
        ids.add(16l);
        CommonResult c= service.remove(ids);
        if (c != null) {
            System.out.println(c.getIsSuccess());
        } else System.out.println("no");

    }
    @Test
    public void testGetById() throws Exception {
        ActivityDto dto=service.getById(1l);
        if(dto!=null)System.out.println(dto.getName());

    }
    @Test
    public void testGetSimpleById() throws Exception {
        ActivitySimpleDto dto=service.getSimpleById(3l);
        if(dto!=null)System.out.println(dto.getName());
    }
    @Test
    public void testGetByIds() throws Exception {
        List<Long> ids=new ArrayList<>();
        ids.add(11l);
        ids.add(12l);
        ids.add(13l);
        ids.add(16l);
        List<ActivityDto>  dtos=service.getByIds(ids);
        System.out.println(dtos.size());

    }
    @Test
    public void testGetSimpleByIds() throws Exception {
        List<Long> ids=new ArrayList<>();
        ids.add(11l);
        ids.add(12l);
        ids.add(13l);
        ids.add(16l);
        List<ActivitySimpleDto>  dtos=service.getSimpleByIds(ids);
        System.out.println(dtos.size());

    }
    @Test
    public void testFindPage() throws Exception {
        ActivityCriteria criteria=new ActivityCriteria();
//        criteria.setName("t");
        criteria.setProductId(8l);
        criteria.setPageSize(5);
        PagedResult<ActivityDto> pg = service.findPage(criteria);
        System.out.println("获取项："+pg.getItems().size()+"\t 第几页："
                +pg.getPageNumber()+"\t 每页数量："+pg.getPageSize()+"\t 页数："+pg.getPagesCount()
                +"\t 项总数："+pg.getTotalItemsCount());
    }
    @Test
    public void testFindSimplePage() throws Exception {
        ActivityCriteria criteria=new ActivityCriteria();
        criteria.setName("t");
        PagedResult<ActivitySimpleDto> pg = service.findSimplePage(criteria);
        System.out.println(pg.getItems().size()+"\t"+pg.getPageNumber()+"\t"+pg.getPageSize()+"\t"+pg.getPagesCount());
    }

}