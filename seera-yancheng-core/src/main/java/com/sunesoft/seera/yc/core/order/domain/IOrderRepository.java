package com.sunesoft.seera.yc.core.order.domain;

import com.sunesoft.seera.fr.ddd.infrastructure.IRepository;

/**
 * 订单仓储接口
 * Created by zhaowy on 2016/7/11.
 */
public interface IOrderRepository extends IRepository<OrderInfo,Long> {

    public OrderInfo get(String orderNum);
}
