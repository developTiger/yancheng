package com.sunesoft.seera.yc.core.product.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.product.application.dtos.FeedBackDto;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductDto;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductSimpleDto;
import com.sunesoft.seera.yc.core.product.domain.ProductStatus;
import com.sunesoft.seera.yc.core.product.domain.ProductType;
import com.sunesoft.seera.yc.core.product.domain.criteria.FeedBackCriteria;
import com.sunesoft.seera.yc.core.product.domain.criteria.ProductCriteria;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductServiceTest extends TestCase {

    @Autowired
    private IProductService service;

    @Test
    public void testGetById() throws Exception {
//        PagedResult<ProductSimpleDto> exists = getExistItems();
//        if (exists.getTotalItemsCount() > 0) {
//            Long id = exists.getItems().get(0).getId();
//            System.out.println(id);
//            Assert.assertNotNull(service.get(id));
//        }
        ProductDto dto=service.get("5605132a-992e-483c-9863-edc0f7901277");
        if(dto!=null){
            System.out.println(dto.getId());
        }
    }
    @Test
    public void testGetSimple() throws Exception {
        ProductSimpleDto dto=service.getSimple(3L);
        if(dto!=null)
            System.out.println(dto.getName()+"\t"+dto.getId());
        else
            System.out.println("no");

    }

    @Test
    public void testRemove() throws Exception {

        CommonResult c=service.remove(7L);
        if(c!=null)
            System.out.println(c.getIsSuccess()+"\t"+c.getId()+"\t"+c.getMsg());
        else
            System.out.println("no");

    }
    @Test
    public void testRemove1() throws Exception {
        List<Long> list=new ArrayList<>();
        list.add(7l);
        list.add(6l);
        CommonResult c=service.remove(list);
        if(c!=null)
            System.out.println(c.getIsSuccess()+"\t"+c.getId()+"\t"+c.getMsg());
        else
            System.out.println("no");

    }
    @Test
    public void testFindProductsSimple() throws Exception {
        //空指针异常
        ProductCriteria criteria=new ProductCriteria();
        criteria.setName("p");
        PagedResult<ProductSimpleDto> pg=service.findProductsSimple(criteria);
        if(pg!=null)
            System.out.println(pg.getItems().size()+"\t"+pg.getPageSize()+"\t"+pg.getPageNumber()+"\t"+pg.getPagesCount());
        else
            System.out.println("no");
    }

    @Test
    public void testgetProductWithItemId() throws Exception {
        //空指针异常
        List<ProductSimpleDto> list=service.getProductWithItemId(132L);
        if(list!=null && !list.isEmpty())
            System.out.println(list.toArray().length);
        else
            System.out.println("no");
    }

    @Test
    public void testFindProducts() throws Exception {

        //空指针异常
//        ProductSimpleDto dto=service.getSimple(1L);
//        ProductCriteria criteria=new ProductCriteria();
//        criteria.setName(dto.getName());
//        criteria.setNum(dto.getNum());
        ProductCriteria criteria=new ProductCriteria();
        criteria.setName("p");

        PagedResult<ProductDto> pg=service.findProducts(criteria);
        if(pg!=null)
            System.out.println(pg.getItems().size()+"\t"+pg.getPageSize()+"\t"+pg.getPageNumber()+"\t"+pg.getPagesCount());
        else
            System.out.println("no");
    }

    @Test
    public void testCreate() throws Exception {
       for(int i=0;i<=3;i++) {
           ProductDto dto = new ProductDto();
           dto.setName("标准门票"+i);
           dto.setPrice(BigDecimal.valueOf(190.00));
           dto.setDiscountPrice(BigDecimal.valueOf(90.00));
           dto.setCanMeal(true);
           dto.setCanReturn(true);
           ArrayList<String> paths = new ArrayList<String>();
           paths.add("~/test/1.png");
           paths.add("~/test/2.png");
           dto.setDetailPicturesPaths(paths);
           dto.setNotice("购票须知，请认真阅读");
           Map<Long, Integer> map = new HashMap<>();
           map.put(132l-i, 1);

           dto.setIdAndCount(map);
           dto.setStatus(ProductStatus.OnSale);

           dto.setType(ProductType.Ticket);
           dto.setProfile("商品介绍"+i);
           dto.setTrafficGuide("交通指南"+i);
           dto.setMainPicturePath("~/test/main.jpg");
           CommonResult c=service.create(dto);
                   if(c!=null)
                       System.out.println(c.getIsSuccess());
//          Assert.assertTrue(service.create(dto).getIsSuccess());
       }
    }
    @Test
    public void testSetProductStatus() throws Exception {

        CommonResult c=service.setProductStatus(1l,ProductStatus.Stoped);
        if(c!=null)
            System.out.println(c.getIsSuccess()+"\t"+c.getId()+"\t"+c.getMsg());
        else
            System.out.println("no");

    }
    @Test
    public void testSetProductStatus1() throws Exception {
        List<Long> list=new ArrayList<>();
        list.add(6L);
        list.add(7L);
        CommonResult c=service.setProductStatus(list,ProductStatus.Stoped);
        if(c!=null)
            System.out.println(c.getIsSuccess()+"\t"+c.getId()+"\t"+c.getMsg());
        else
            System.out.println("no");
    }

    @Test
    public void testEdit() throws Exception {
        //疑惑
        ProductDto dto=new ProductDto();
        dto.setId(6l);
        dto.setName("ttt");
        CommonResult c=service.edit(dto);
        if(c!=null)
            System.out.println(c.getIsSuccess()+"\t"+c.getId()+"\t"+c.getMsg());
        else
            System.out.println("no");
    }

    /**
     *
     */
@Test
    public void getExistItems() {
        ProductCriteria criteria = new ProductCriteria();
        criteria.setOrderByProperty("onSaleTime");
        criteria.setStatus(ProductStatus.Stoped);
       criteria.setType(ProductType.GroupProduct);
        criteria.setAscOrDesc(false);
        PagedResult<ProductSimpleDto> t = service.findProductsSimple(criteria);
        System.out.println(t.getTotalItemsCount());
    }


    @Test
    public void testAddProductItem ()throws Exception {
//        service.addProductItem((long) 2, (long) 41);
//        service.addProductItem(2l,4l,1);
        CommonResult c=service.addProductItem(8l, 6l, 2);
        if(c!=null){
            System.out.println(c.getIsSuccess());
        }

    }

    @Test
    public void testResetProductItem ()throws Exception {
        Map<Long,Integer> map=new HashMap<>();
        map.put(4l,2);
        map.put(5l,2);
        CommonResult c=service.resetProductItem(3l, map, BigDecimal.valueOf(0.5));
        if(c!=null){
            System.out.println(c.getIsSuccess());
        }
    }

// test BigDecimal add
    @Test
    public void test(){
        BigDecimal b1=BigDecimal.ZERO;
        BigDecimal b2=BigDecimal.valueOf(0.5);
        System.out.println(b1.add(b2));
    }



// test feedBack
    @Test
    public void testAddProductFeedBack() throws Exception {
        for(int i=0;i<1;i++) {
            FeedBackDto dto = new FeedBackDto();
            dto.setTouristId(5l);
            dto.setProductId(8l);
            dto.setContent("good");
            dto.setScore(5);
            CommonResult s = service.addProductFeedBack(dto);
            if (s != null) {
                System.out.println(s.getIsSuccess());
            }

        }
    }

    @Test
    public void testRemoveProductFeedBack() throws Exception {
        CommonResult c=service.removeProductFeedBack(1L);
        if (c != null) {
            System.out.println(c.getIsSuccess());
        }

    }

    @Test
    public void testGetProductFeeBacks() throws Exception {
        FeedBackCriteria criteria=new FeedBackCriteria();
        criteria.setProductId(8l);
        PagedResult<FeedBackDto> pr=service.getProductFeeBacks(criteria);
        if(pr!=null){
            System.out.println(pr.getItems().size());
        }
    }
}