package com.sunesoft.seera.yc.core.order.application.factory;

import com.sunesoft.seera.fr.utils.Factory;
import com.sunesoft.seera.yc.core.order.application.dtos.ProductHistoryDto;
import com.sunesoft.seera.yc.core.order.domain.ProductHistory;
import com.sunesoft.seera.yc.core.parameter.application.factory.DtoFactory;
import com.sunesoft.seera.yc.core.product.domain.Product;

import java.util.Date;

/**
 * Created by zhaowy on 2016/7/21.
 */
public class ProductHistoryFactory{


    public static ProductHistory convert(Product product) {
        ProductHistory history = Factory.convert(product,ProductHistory.class);
        history.setId(null);
        history.setOriginalId(product.getId());
        history.setCreateDateTime(new Date());
        return history;
    }
}
