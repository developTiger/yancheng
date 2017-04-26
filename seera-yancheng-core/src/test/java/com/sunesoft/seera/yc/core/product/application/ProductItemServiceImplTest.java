package com.sunesoft.seera.yc.core.product.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductItemDto;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductItemSimpleDto;
import com.sunesoft.seera.yc.core.product.domain.ProductItemType;
import com.sunesoft.seera.yc.core.product.domain.criteria.ProductItemCriteria;
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
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductItemServiceImplTest extends TestCase {
    @Autowired
    private IProductItemService service;

    /**
     * 全体通过
     * @throws Exception
     */

    @Test
    public void testGetById() throws Exception {
//        PagedResult<ProductItemSimpleDto> exists = getExistItems();
//        if (exists.getTotalItemsCount() > 0) {
//            Long id = exists.getItems().get(0).getId();
//            System.out.println(id);
//            Assert.assertNotNull(service.get(id));
//        }
        ProductItemDto  dto=service.get(1l);
        if(dto!=null){
            System.out.println(dto.getId());
        }else {
            System.out.println("no");
        }
    }
    @Test
    public void testGetByNum() throws Exception {
        ProductItemDto dto=service.get("No-15d3742d-2772-40d8-bb64-f1055ac6cac8");
        if(dto!=null)
            System.out.println(dto.getId());
        else
            System.out.println("no");
    }

    @Test
    public void testGetSimple() throws Exception {
        ProductItemSimpleDto dto=service.getSimple(1L);
        if(dto!=null)
            System.out.println(dto.getNum());
        else
            System.out.println("no");

    }
    @Test
    public void testGetSimple1() throws Exception {
        ProductItemSimpleDto dto=service.get("No-15d3742d-2772-40d8-bb64-f1055ac6cac8");
        if(dto!=null)
            System.out.println(dto.getName());
        else
            System.out.println("no");


    }

    @Test
    public void testCreate() throws Exception {
        for (int i = 0; i <10; i++) {
            ProductItemDto dto = new ProductItemDto();
            dto.setNum("No-" + UUID.randomUUID());
            dto.setName("淹城门票成人票" + new Date().getTime());
            dto.setPrice(BigDecimal.valueOf(190.00 + new Date().getTime() % 30));
            dto.setProductItemType(ProductItemType.Ticket);
            dto.setSeller("自营");
//            Assert.assertTrue(service.create(dto).getIsSuccess());
            CommonResult c=service.create(dto);
            if(c!=null)
                System.out.print(c.getIsSuccess());
        }
    }

    @Test
    public void testEdit() throws Exception {
        ProductItemDto dto = service.get(2l);
        dto.setPrice(BigDecimal.valueOf(6.10));
        dto.setProductItemType(ProductItemType.Souvenirs);
        dto.setName("修改后的名称");
        service.edit(dto);
    }
    @Test
    public void testRemove() throws Exception {
        //采用物理删除
        CommonResult c=service.remove(1L);
        if(c!=null)
            System.out.println(c.getId()+"\t"+c.getIsSuccess()+"\t"+c.getMsg());
        else
            System.out.println("no");
    }
    @Test
    public void testRemove1() throws Exception {
        //采用物理删除
        List<Long> list=new ArrayList<>();
        list.add(1l);
        list.add(2l);
        list.add(3l);
        System.out.println(list.size());
        CommonResult c=service.remove(list);
        if(c!=null)
            System.out.println(c.getId()+"\t"+c.getIsSuccess()+"\t"+c.getMsg());
        else
            System.out.println("no");

    }

    @Test
    public void testFindProductItemsSimple() throws Exception {
        ProductItemCriteria criteria = new ProductItemCriteria();
        criteria.setName("8");
        criteria.setOrderByProperty("price");
        criteria.setAscOrDesc(false);
        PagedResult<ProductItemSimpleDto> pagedResult = service.findProductItemsSimple(criteria);
        if (pagedResult.getTotalItemsCount() > 0)
            pagedResult.getItems().stream().forEach(x -> System.out.print(x.getId() + "|" +x.getName() + "|" + x.getNum() + "|" + x.getPrice()+"\n"));
    System.out.println(pagedResult.getItems().size());
    }

    //todo //test here
    @Test
    public void testFindProductItems() throws Exception {
        ProductItemCriteria criteria = new ProductItemCriteria();
        criteria.setName("8");
        criteria.setOrderByProperty("price");
        criteria.setAscOrDesc(false);
        PagedResult<ProductItemDto> pagedResult = service.findProductItems(criteria);
        if (pagedResult.getTotalItemsCount() > 0)
            pagedResult.getItems().stream().forEach(x -> System.out.print(x.getId() + "|" +x.getName() + "|" + x.getNum() + "|" + x.getPrice()+"\n"));
        System.out.println(pagedResult.getItems().size());
    }

    private PagedResult<ProductItemSimpleDto> getExistItems() {
        ProductItemCriteria criteria = new ProductItemCriteria();
        criteria.setOrderByProperty("price");
        criteria.setAscOrDesc(false);
        return service.findProductItemsSimple(criteria);
    }
}