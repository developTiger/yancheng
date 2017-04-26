package com.sunesoft.seera.yc.core.order.domain;

import com.sunesoft.seera.fr.ddd.infrastructure.IRepository;

/**
 * 商品历史版本仓储接口
 * Created by zhaowy on 2016/7/11.
 */
public interface IProductHistoryRepository extends IRepository<ProductHistory,Long> {
    /**
     * 找出与商品对应的历史订单(ProductHistory)
     * @param originalId
     * @return
     */
    ProductHistory getLatestByOriginalId(Long originalId);

}
