package com.sunesoft.seera.yc.core.order.application.factory;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.Factory;
import com.sunesoft.seera.yc.core.coupon.application.factory.CouponFactory;
import com.sunesoft.seera.yc.core.order.application.dtos.OrderDto;
import com.sunesoft.seera.yc.core.order.application.dtos.OrderProductDto;
import com.sunesoft.seera.yc.core.order.application.dtos.OrderProductItemDto;
import com.sunesoft.seera.yc.core.order.domain.OrderInfo;
import com.sunesoft.seera.yc.core.tourist.application.dtos.FetcherDto;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristSimpleDto;
import com.sunesoft.seera.yc.core.tourist.domain.Tourist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowy on 2016/7/19.
 */
public class OrderFactory extends Factory {

    /**
     * OrderInfo convert OrderDto
     *
     * @param info
     * @return
     */
    public static OrderDto convert(OrderInfo info) {
        OrderDto dto = Factory.convert(info, OrderDto.class);
        //coupon->
        if (info.getCoupon() != null)
            dto.setCouponDto(CouponFactory.convert(info.getCoupon()));
        //fetcher->
        dto.setFetcherDto(Factory.convert(info.getFetcher(), FetcherDto.class));
        //tourist->
        dto.setTouristSimpleDto(getTsimple(info.getTourist()));
        //List<OrderProductItem>->



        List<OrderProductDto> orderProductDtos = new ArrayList<>();
        info.getProducts().stream().forEach(i -> {
            OrderProductDto pd = new OrderProductDto();
            pd = OrderProductFactory.convert(i);
            List<OrderProductItemDto> productItemDtos = new ArrayList<>();
            i.getItems().stream().forEach(e -> {
                OrderProductItemDto dto1 = OrderItemFactory.convert(e);
//            dto1.setOrderId(info.getId());
                productItemDtos.add(dto1);
            });
            pd.setProductItemDtos(productItemDtos);

            orderProductDtos.add(pd);
        });
        dto.setProductDtos(orderProductDtos);

        return dto;
    }

    public static List<OrderDto> convert(List<OrderInfo> orders) {
        List<OrderDto> dtos = new ArrayList<>();
        if (null != orders && !orders.isEmpty()) {
            orders.stream().forEach(i -> dtos.add(convert(i)));
            return dtos;
        }
        return null;
    }

    public static PagedResult<OrderDto> convert(PagedResult<OrderInfo> pagedResult) {
        //OrderInfo -> List<OrderProduct>
        // OrderProduct -> ProductHistory
        //OrderInfo -> List<OrderProductItem>
        List<OrderDto> dtos;
        if (null != pagedResult && pagedResult.getTotalItemsCount() > 0) {
            dtos = convert(pagedResult.getItems());
            return new PagedResult<>(
                    dtos, pagedResult.getPageNumber(), pagedResult.getPageSize(), pagedResult.getTotalItemsCount());

        }
        return null;

    }


    private static TouristSimpleDto getTsimple(Tourist tourist){
        TouristSimpleDto dto=new TouristSimpleDto();
        dto.setIsActive(tourist.getIsActive());
        dto.setStatus(tourist.getStatus());
        dto.setMobilePhone(tourist.getMobilePhone());
        dto.setWxName(tourist.getWxName());
        dto.setUserName(tourist.getUserName());
        dto.setRealName(tourist.getRealName());
        dto.setId(tourist.getId());
        dto.setCreateDateTime(tourist.getCreateDateTime());
        dto.setEmail(tourist.getEmail());
        dto.setIntegrals(tourist.getIntegrals());
         return dto;
    }
}
