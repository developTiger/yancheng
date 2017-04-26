package com.sunesoft.seera.yc.core.shoppingCar.application.factory;

import com.sunesoft.seera.fr.utils.Factory;
import com.sunesoft.seera.yc.core.product.application.factory.ProductFactory;
import com.sunesoft.seera.yc.core.shoppingCar.application.dto.ShoppingItemDto;
import com.sunesoft.seera.yc.core.shoppingCar.domain.ShoppingItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowy on 2016/7/18.
 */
public class ShoppingCarFactory extends Factory {

    public static List<ShoppingItemDto> convert(List<ShoppingItem> itemList) {
        List<ShoppingItemDto> dtos = new ArrayList<>();
        itemList.stream().forEach(i ->
                dtos.add(convert(i)));
        return dtos;
    }

    public static ShoppingItemDto convert(ShoppingItem item) {
        ShoppingItemDto dto = ProductFactory.convert(item, ShoppingItemDto.class);
        dto.setTouristId(item.getTourist().getId());
        dto.setPrice(item.getProduct().getDiscountPrice());
        dto.setSpecDescription(item.getProduct().getSpecDescription());
        dto.setStock(item.getProduct().getStock());
        dto.setMainPicturePath(item.getProduct().getMainPicturePath());
        dto.setTouristName(item.getTourist().getUserName());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setType(item.getProduct().getType());
        dto.setRejectAreas(item.getProduct().getRejectAreas());
        return dto;
    }
}
