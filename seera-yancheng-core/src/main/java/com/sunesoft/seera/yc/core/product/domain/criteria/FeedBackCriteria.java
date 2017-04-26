package com.sunesoft.seera.yc.core.product.domain.criteria;

import com.sunesoft.seera.fr.results.PagedCriteria;

/**
 * Created by zhaowy on 2016/7/26.
 */
public class FeedBackCriteria extends PagedCriteria {

    private Long productId;


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
