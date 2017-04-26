package com.sunesoft.seera.yc.core.shoppingItem;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.yc.core.shoppingCar.application.IShoppingCarService;
import com.sunesoft.seera.yc.core.shoppingCar.application.dto.ShoppingItemDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiazl on 2016/7/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class IShoppingCarServiceTest {
    @Autowired
    IShoppingCarService service;

    @Test
    public void addItemTest() {
        ShoppingItemDto dto = new ShoppingItemDto();
        dto.setTouristId(7l);
        dto.setCount(2);
        dto.setProductId(6l);
        dto.setIsValid(true);
        CommonResult c = service.addItem(dto);
        if (c != null) {
            System.out.println(c.getIsSuccess() + "\t" + c.getMsg());
        }


    }

    @Test
    public void setShoppingItemInvalidTest() {
        service.setShoppingItemInvalid(1L);
    }

    @Test
    public void increaseItemCountTest() {
        CommonResult c=service.increaseItemCount(1L,66);
        Assert.isTrue(c.getIsSuccess());

    }

    @Test
    public void reduceItemCountTest() {
        CommonResult c=service.reduceItemCount(1L, 30);
        Assert.isTrue(c.getIsSuccess());
    }

    @Test
    public void removeItemTest() {
        CommonResult c=service.removeItem(2L);
        Assert.isTrue(c.getIsSuccess());
    }

    @Test
    public void removeItemsTest() {
        List<Long> list=new ArrayList<>();
        list.add(1L);
        list.add(2L);
        CommonResult c=service.removeItems(list);
        Assert.isTrue(c.getIsSuccess());

    }

    @Test
    public void getTest() {
        ShoppingItemDto dto=service.get(3l);
        if(dto!=null){
            System.out.println(dto.getTouristId()+"\t"+dto.getProductId());
        }

    }

    @Test
    public void getTouristShoppingItemsTest() {
        List<ShoppingItemDto> list=service.getTouristShoppingItems(3L);
        if(list!=null){
            System.out.println(list.size()+"\t"+list.get(0).getId());
        }
    }
}
