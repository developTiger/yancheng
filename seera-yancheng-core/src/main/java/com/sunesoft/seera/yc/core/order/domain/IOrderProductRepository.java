package com.sunesoft.seera.yc.core.order.domain;

import com.sunesoft.seera.fr.ddd.infrastructure.IRepository;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.product.domain.Product;

import java.util.List;

/**
 * 订单商品仓储接口
 * Created by xzl on 2016/7/25.
 */
public interface IOrderProductRepository extends IRepository<OrderProduct,Long> {

    /**
     * 根据原始商品id获取订单商品
     * @param originalId
     * @return
     */
    List<OrderProduct> getByProductHistory(Long originalId);

    /**
     * 订单商品s查询
     * @param orderProductStatus
     * @return
     */
    public List<OrderProduct> findOrderProduct(OrderProductStatus orderProductStatus);


}
