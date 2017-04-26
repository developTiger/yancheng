package com.sunesoft.seera.yc.core.order.application.factory;

import com.sunesoft.seera.fr.utils.Factory;
import com.sunesoft.seera.yc.core.order.application.dtos.OrderProductDto;
import com.sunesoft.seera.yc.core.order.application.dtos.ProductHistoryDto;
import com.sunesoft.seera.yc.core.order.domain.OrderProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowy on 2016/7/22.
 */
public class OrderProductFactory extends Factory {

    public static OrderProductDto convert(OrderProduct product) {
        OrderProductDto dto = convert(product, OrderProductDto.class);
        dto.setProductDto(convert(product.getProduct(), ProductHistoryDto.class));
        return dto;
    }

    public static List<OrderProductDto> convert(List<OrderProduct> products){
        List<OrderProductDto> dtos = new ArrayList<>();
        products.stream().forEach(i-> dtos.add(convert(i)));
        return dtos;
    }
}
