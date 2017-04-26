package com.sunesoft.seera.yc.core.product.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.product.domain.criteria.ProductItemCriteria;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductItemDto;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductItemSimpleDto;

import java.util.List;

/**
 * 商品项服务接口
 * Created by zhaowy on 2016/7/11.
 */
public interface IProductItemService {

    /**
     * @param id 商品项标识
     * @return 商品项详细信息
     */
    public ProductItemDto get(Long id);

    /**
     * @param num 商品项编码
     * @return 商品项详细信息
     */
    public ProductItemDto get(String num);

    /**
     * @param id 商品项标识
     * @return 商品项简要信息
     */
    public ProductItemSimpleDto getSimple(Long id);

    /**
     * @param num 商品项编码
     * @return 商品项简要信息
     */
    public ProductItemSimpleDto getSimple(String num);

    /**
     * 新增商品项
     * @param productItem 商品项信息
     */
    public CommonResult create(ProductItemDto productItem);

    /**
     * 编辑商品项
     * @param productItem 确保ProductItemDto中id正确
     */
    public CommonResult edit(ProductItemSimpleDto productItem);

    /**
     *  编辑商品项
     * @param productItem
     * @return
     */
    public CommonResult edit(ProductItemDto productItem);
    /**
     * 根据标识移除商品项
     * @param id
     */
    public CommonResult remove(Long id);

    /**
     * 根据商品项标识批量移除商品项
     * @param ids
     */
    public CommonResult remove(List<Long> ids);

    /**
     * 查询商品项
     *  @param criteria
     * @return 商品项简要信息集合
     */
    public PagedResult<ProductItemSimpleDto> findProductItemsSimple(ProductItemCriteria criteria);

    /**
     * 查询商品项
     *  @param criteria
     * @return 商品项详细信息集合
     */
    public PagedResult<ProductItemDto> findProductItems(ProductItemCriteria criteria);

    /**
     * 获取所有商品项
     * @return
     */
    public List<ProductItemSimpleDto> getProductItemSimpleDtos();

    /**
     * 获取所有商品项
     * @return
     */
    public List<ProductItemDto> getProductItemDtos();
}
