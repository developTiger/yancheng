package com.sunesoft.seera.yc.core.shoppingCar.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.yc.core.shoppingCar.application.dto.ShoppingItemDto;

import java.util.Date;
import java.util.List;

/**
 * Created by zhaowy on 2016/7/18.
 */

public interface IShoppingCarService {

    /**
     * 添加购物车商品
     *
     * @param item 游客选购商品
     */
    public CommonResult addItem(ShoppingItemDto item);

    /**
     * 设置购物车商品项无效
     *
     * @param productId 商品标识集合
     * @return
     */

    public void setShoppingItemInvalid(Long productId);

    /**
     * 增加购物车商品项数量
     *
     * @param shoppingItemId 购物车项标识
     * @param count          增加数量
     */
    public CommonResult increaseItemCount(Long shoppingItemId, Integer count);


    /**
     * 设置购物车商品数量
     *
     * @param shoppingItemId 购物车项标识
     * @param count          增加数量
     */
    public CommonResult setItemCount(Long shoppingItemId, Integer count);

    /**
     * 设置购物车数量 和 计划日期
     * @param shoppingItemId
     * @param count
     * @param hotelDate
     * @param tureDate
     * @return
     */
    CommonResult SetProductCountAndScheduleDate(Long shoppingItemId, Integer count,Date hotelDate,Date tureDate);
    /**
     * 减少购物车商品数量
     *
     * @param shoppingItemId 购物车项标识
     * @param count          减少数量
     */
    public CommonResult reduceItemCount(Long shoppingItemId, Integer count);

    /**
     * 移除购物车商品项
     *
     * @param itemId 待移除项标识
     */
    public CommonResult removeItem(Long itemId);

    /**
     * 批量移除商品项
     *
     * @param itemIds
     * @return
     */
    public CommonResult removeItems(List<Long> itemIds);

    /** 获取购物车商品项
     * @param itemId 购物车商品项标识
     * @return
     */
    public ShoppingItemDto get(Long itemId);

    /** 获取购物车商品项
     * @param itemIds 购物车商品项标识
     * @return
     */
    public List<ShoppingItemDto> get(List<Long> itemIds);

    /**
     * 获取游客购物车清单
     *
     * @param touristId 游客标识
     * @return 购物车清单
     */
    public List<ShoppingItemDto> getTouristShoppingItems(Long touristId);

    /**
     * 获取游客购物车商品数量
     *
     * @param touristId 游客标识
     * @return 购物车清单
     */
    public int getItemsCount(Long touristId);

    /**
     * 清空购物车
     * @param touristId
     * @return
     */
    public CommonResult clearShoppingCar(Long touristId);
}
