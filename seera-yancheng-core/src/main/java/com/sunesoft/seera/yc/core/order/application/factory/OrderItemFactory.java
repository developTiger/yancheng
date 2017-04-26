package com.sunesoft.seera.yc.core.order.application.factory;

import com.sunesoft.seera.fr.utils.Factory;
import com.sunesoft.seera.yc.core.order.application.dtos.OrderProductItemDto;
import com.sunesoft.seera.yc.core.order.domain.OrderProductItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowy on 2016/7/22.
 */
public class OrderItemFactory extends Factory {

    public static OrderProductItemDto convert(OrderProductItem item) {
        OrderProductItemDto dto = convert(item, OrderProductItemDto.class);
        dto.setOrderProductId(item.getProduct().getId());

        //这里仍缺少dto.setOrderId()
        return dto;
    }

    public static List<OrderProductItemDto> convert(List<OrderProductItem> items) {
        List<OrderProductItemDto> dtos = new ArrayList<>();
        items.stream().forEach(i ->dtos.add(convert(i)));
        return dtos;
    }

}
