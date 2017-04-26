package com.sunesoft.seera.yc.core.shoppingCar.domain;

import com.sunesoft.seera.fr.ddd.infrastructure.IRepository;

import java.util.List;

/**
 * Created by zhaowy on 2016/7/18.
 */
public interface IShoppingCarRepository extends IRepository<ShoppingItem, Long> {

    /**
     * 获取游客购物车商品项
     * @param touristId 游客标识
     * @return
     */
    public List<ShoppingItem> getTouristShoppingItem(Long touristId);

    /**
     *  获取包含指定商品的所有购物车商品项
     * @param productId 商品标识
     * @return
     */
    public List<ShoppingItem> getProductShoppingItem(Long productId);

}
