package com.sunesoft.seera.yc.core.shoppingCar.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.yc.core.shoppingCar.application.dto.ShoppingItemDto;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ShoppingCarServiceImplTest extends TestCase {

    @Autowired
    ShoppingCarServiceImpl service;

    @Test
    public void testAddItem() throws Exception {
        List<ShoppingItemDto> dtos = service.getTouristShoppingItems(1L);
        List<Long> ids = dtos.stream().map(i -> i.getId()).collect(Collectors.toList());
        Assert.isTrue(service.removeItems(ids).getIsSuccess());
        addItem();
        dtos = service.getTouristShoppingItems(1L);
        Assert.isTrue(dtos.get(0).getCount() == 1);
        addItem();
        dtos = service.getTouristShoppingItems(1L);
        Assert.isTrue(dtos.get(0).getCount() == 2);

//        ShoppingItemDto item=new ShoppingItemDto();
//
//        item.setProductId(6l);
//        item.setTouristId(7l);
//        item.setCount(3);
//        CommonResult c=service.addItem(item);
//        if(c!=null){
//            System.out.println(c.getIsSuccess());
//        }else System.out.println("no");

    }

    @Test
    public void testSetShoppingItemInvalid() throws Exception {
        addItem();
        ShoppingItemDto dto = service.getTouristShoppingItems(1L).get(0);
        service.setShoppingItemInvalid(dto.getProductId());
        ShoppingItemDto repodto = service.get(dto.getId());
        Assert.isTrue(!repodto.getIsValid());
//        service.setShoppingItemInvalid(8l);
    }

    @Test
    public void testIncreaseItemCount() throws Exception {

        addItem();
        ShoppingItemDto dto = service.getTouristShoppingItems(1L).get(0);
        service.increaseItemCount(dto.getId(), 1);
        ShoppingItemDto repodto = service.get(dto.getId());

        Assert.isTrue(dto.getCount() + 1 == repodto.getCount());
//        CommonResult c=service.increaseItemCount(3l, 10);
//        if(c!=null){
//            System.out.println(c.getIsSuccess());
//        }else System.out.println("no");
    }

    @Test
    public void testReduceItemCount() throws Exception {
        addItem();
        ShoppingItemDto dto = service.getTouristShoppingItems(1L).get(0);
        service.reduceItemCount(dto.getId(), 1);
        ShoppingItemDto repodto = service.get(dto.getId());
        Assert.isTrue(dto.getCount() - 1 == repodto.getCount());
//        CommonResult c=service.reduceItemCount(3l, 5);
//        if(c!=null){
//            System.out.println(c.getIsSuccess());
//        }else System.out.println("no");
    }

    @Test
    public void testRemoveItem() throws Exception {
        addItem();
        ShoppingItemDto dto = service.getTouristShoppingItems(1L).get(0);
        Assert.isTrue(service.removeItem(dto.getId()).getIsSuccess());
        ShoppingItemDto reoDto = service.get(dto.getId());
        Assert.isNull(reoDto);
//        CommonResult c=service.removeItem(3l);
//        if(c!=null){
//            System.out.println(c.getIsSuccess());
//        }else System.out.println("no");
    }

    @Test
    public void testRemoveItems() throws Exception {
        addItem();
        addItem();
        List<ShoppingItemDto> dtos = service.getTouristShoppingItems(1L);
        List<Long> ids = dtos.stream().map(i -> i.getId()).collect(Collectors.toList());
        Assert.isTrue(service.removeItems(ids).getIsSuccess());
        dtos = service.getTouristShoppingItems(1L);
        Assert.isTrue(dtos.isEmpty());
    }

    @Test
    public void testGetTouristShoppingItems() throws Exception {
        addItem();
        List<ShoppingItemDto> dtos = service.getTouristShoppingItems(1L);
        Assert.notNull(dtos);
        //TODO item filter testing
//        List<ShoppingItemDto> dtos=service.getTouristShoppingItems(7l);
//        if(dtos!=null){
//            System.out.println(dtos.size());
//        }
    }
    @Test
    public void testClearItems() throws Exception {
        CommonResult c=service.clearShoppingCar(7l);
        if(c!=null){
            System.out.println(c.getIsSuccess());
        }else System.out.println("no");
    }

    private void addItem() {
        ShoppingItemDto dto = new ShoppingItemDto();
        dto.setTouristId(1L);
        dto.setProductId(2L);
        dto.setCount(1);
        service.addItem(dto).getIsSuccess();
    }
}