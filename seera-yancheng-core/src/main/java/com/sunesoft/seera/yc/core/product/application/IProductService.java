package com.sunesoft.seera.yc.core.product.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.product.application.dtos.FeedBackDto;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductDto;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductSimpleDto;
import com.sunesoft.seera.yc.core.product.domain.ProductKind;
import com.sunesoft.seera.yc.core.product.domain.ProductStatus;
import com.sunesoft.seera.yc.core.product.domain.criteria.FeedBackCriteria;
import com.sunesoft.seera.yc.core.product.domain.criteria.ProductCriteria;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品服务接口
 * Created by zhaowy on 2016/7/11.
 */
public interface IProductService {

    /**
     * 新增商品
     * @param dto 商品dto
     */
    public CommonResult create(ProductDto dto);

    /**
     * 新增商品项
     * @param productId    商品标识
     * @param itemId 新增商品项标识
     * @param count 新增商品项数量
     * @return
     */
    public CommonResult addProductItem(Long productId,Long itemId,int count);

    /**
     * 重置商品项集合
     * @param productId    商品标识
     * @param map 重置商品项标识集合
     * @return
     */
    public CommonResult resetProductItem(Long productId,Map<Long,Integer> map,BigDecimal dicprice);


    /**
     * 设置商品销售类型
     * @param id 商品标识
     * @param kind 商品状态
     */
    public CommonResult setProductKind(Long id, ProductKind kind);

    /**
     * 批量设置商品销售类型
     * @param ids 商品标识
     * @param kind 商品状态
     */
    public CommonResult setProductKind(List<Long> ids, ProductKind kind);
    /**
     * 设置商品状态
     * @param id 商品标识
     * @param status 商品状态
     */
    public CommonResult setProductStatus(Long id, ProductStatus status);

    /**
     * 批量设置商品状态
     * @param ids 商品标识
     * @param status 商品状态
     */
    public CommonResult setProductStatus(List<Long> ids, ProductStatus status);
    /**
     * 移除商品
     * @param id
     */
    public CommonResult remove(Long id);

    /**
     * 批量移除商品
     * @param ids
     */
    public CommonResult remove(List<Long> ids);

    /**
     * 更新商品
     *<p>仅针对商品信息本身的更新，不支持商品项信息内容修改</p>
     @param dto
     */
    public CommonResult edit(ProductDto dto);

    /**
     * 获取商品信息
     * @param productId 商品标识
     * @return
     */
    public ProductDto get(long productId);

    /**
     * 获取商品信息
     * @param num 商品编码
     * @return
     */
    public ProductDto get(String num);

    /**
     * 获取商品信息
     * @param productId 商品标识
     * @return
     */
    public ProductSimpleDto getSimple(Long productId);

    /**
     * 获取商品信息
     * @param num 商品编码
     * @return
     */
    public ProductSimpleDto getSimple(String num);

    /**
     * 增加商品库存量
     *  @param productId
     * @param count
     * @return
     */
    public CommonResult increaseStock(Long productId,int count);

    /**
     * 减少商品库存量
     *  @param productId
     * @param count
     * @return
     */
    public CommonResult reduceStock(Long productId,int count);

    /**
     * 查询商品
     *  @param criteria
     * @return 商品简要信息集合
     */
    public PagedResult<ProductSimpleDto> findProductsSimple(ProductCriteria criteria);

    /**
     * 查询商品
     *  @param criteria
     * @return 商品详细信息集合
     */
    public PagedResult<ProductDto> findProducts(ProductCriteria criteria);

    /**
     * 查询包含指定商品项标识的所有商品
     * @param productItemId
     * @return
     */
    public List<ProductSimpleDto> getProductWithItemId(Long productItemId);

    /**新增商品评论
     * @param dto 评论信息
     * @return
     */
    public CommonResult addProductFeedBack(FeedBackDto dto);

    /**批量新增商品评论
     * @param dtos 评论信息
     * @return
     */
    public CommonResult addProductFeedBack(List<FeedBackDto> dtos,String orderNum);

    /**删除商品评论
     * @param feedBackId 评论标识
     * @return
     */
    public CommonResult removeProductFeedBack(Long feedBackId);

    /** 获取商品评论信息
     * @param criteria 评论查询条件
     * @return
     */
    public PagedResult<FeedBackDto> getProductFeeBacks(FeedBackCriteria criteria);

    /**
     * 获取所有省份
     * @return
     */
    public Map<Integer,String> getAllProvices();

    /**
     * 用的地方多所以变成公有的
     * @param keys
     * @return
     */
    public String editRejectAreasNames(String keys);

}
