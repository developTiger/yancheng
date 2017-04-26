package com.sunesoft.seera.yc.core.product.domain;

import com.sunesoft.seera.fr.ddd.infrastructure.IRepository;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.product.domain.criteria.ProductCriteria;

import java.util.List;

/**
 * 商品数据仓储接口
 * Created by zhaowy on 2016/7/11.
 */
public interface IProductRepository extends IRepository<Product,Long> {

    /**
     * 检查商品是否同名
     * @param name
     * @return
     */
    public Boolean check(String name);
    /**
     * @param num 商品编码
     * @return 匹配的商品信息，否则范围NULL
     */
    public Product getByNum(String num);

    /**
     * 查询商品
     *
     * @param criteria
     * @return 商品信息集合
     */
    public PagedResult<Product> findProducts(ProductCriteria criteria);


    /**
     * 查询包含指定商品项标识的所有商品
     * @param productItemId
     * @return
     */
    public List<Product> getProductWithItemId(Long productItemId);
}
