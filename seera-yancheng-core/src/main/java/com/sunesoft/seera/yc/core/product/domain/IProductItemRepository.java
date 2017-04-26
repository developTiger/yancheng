package com.sunesoft.seera.yc.core.product.domain;

import com.sunesoft.seera.fr.ddd.infrastructure.IRepository;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.product.domain.criteria.ProductItemCriteria;

/**
 * 商品项数据仓储接口
 * Created by zhaowy on 2016/7/11.
 */
public interface IProductItemRepository extends IRepository<ProductItem,Long> {

    /**
     * @param num 商品项编码
     * @return 匹配的商品项信息，否则范围NULL
     */
    public ProductItem GetByNum(String num);


    /**
     * 查询商品
     * @param criteria
     * @return 商品项信息集合
     */
    public PagedResult<ProductItem> findProductsItems(ProductItemCriteria criteria);

}
