package com.sunesoft.seera.yc.core.order.domain;

import com.sunesoft.seera.fr.ddd.infrastructure.IRepository;

/**
 * 订单商品项仓储接口
 * Created by zhaowy on 2016/7/11.
 */
public interface IOrderItemRepository extends IRepository<OrderProductItem,Long> {


    OrderProductItem getByOriginalId(Long originalId);


    /**
     * 根据商品商品项提取码获取订单商品项
     * @param takeNum
     * @return
     */
    OrderProductItem get(String takeNum);
}
