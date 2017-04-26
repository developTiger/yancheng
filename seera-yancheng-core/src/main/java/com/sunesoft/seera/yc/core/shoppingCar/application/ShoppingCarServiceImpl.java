package com.sunesoft.seera.yc.core.shoppingCar.application;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateFinder;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.product.application.IProductService;
import com.sunesoft.seera.yc.core.product.domain.IProductRepository;
import com.sunesoft.seera.yc.core.shoppingCar.application.dto.ShoppingItemDto;
import com.sunesoft.seera.yc.core.shoppingCar.application.factory.ShoppingCarFactory;
import com.sunesoft.seera.yc.core.shoppingCar.domain.IShoppingCarRepository;
import com.sunesoft.seera.yc.core.shoppingCar.domain.ShoppingItem;
import com.sunesoft.seera.yc.core.tourist.domain.ITouristRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 购物车商品项服务实现
 * Created by zhaowy on 2016/7/18.
 */
@Service("iShoppingCarService")
public class ShoppingCarServiceImpl
        extends GenericHibernateFinder implements IShoppingCarService {

    @Autowired
    private IShoppingCarRepository repository;
    @Autowired
    private ITouristRepository touristRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IProductService iProductService;


    /**
     * 添加购物车商品
     *
     * @param item 游客选购商品
     */
    @Override
    public CommonResult addItem(ShoppingItemDto item) {
        if (item.getCount() < 1) return ResultFactory.commonError("添加商品数量");
        final Long[] shopItemId = {0L};
        if (item.getTouristId() == null || item.getTouristId() < 1) {
            return ResultFactory.commonError("id of tourist must not be null!");
        }

        try {
            List<ShoppingItem> items = repository.getTouristShoppingItem(item.getTouristId());


            if (null != item && items.stream().anyMatch(i -> i.getProduct().getId().equals(item.getProductId())
                    && i.getTourist().getId().equals(item.getTouristId()) && isSameDate(i, item.getTourScheduleDate(), item.getHotelScheduleDate()))) {

                //购物车中已存在商品项增加数量即可
                items.stream().filter(i -> i.getProduct().getId().equals(item.getProductId())
                        && i.getTourist().getId().equals(item.getTouristId()) && isSameDate(i, item.getTourScheduleDate(), item.getHotelScheduleDate()))
                        .forEach(shopItem -> {
                            shopItem.increaseItemCount(item.getCount());
                            shopItemId[0] = repository.save(shopItem);
                        });
            }
            //新增购物车
            else {
                ShoppingItem shoppingItem = ShoppingCarFactory.convert(item, ShoppingItem.class);
                shoppingItem.setId(null);
                shoppingItem.setProduct(productRepository.get(item.getProductId()));
                shoppingItem.setTourist(touristRepository.get(item.getTouristId()));
                shopItemId[0] = repository.save(shoppingItem);
            }

            /**
             * 添加购物车数量！！！
             */
            int count=this.getItemsCount(item.getTouristId());
            /* return ResultFactory.commonSuccess(shopItemId[0]);*/
            return ResultFactory.commonSuccess(Long.valueOf(count));
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }
    }

    private Boolean isSameDate(ShoppingItem item, Date tureDate, Date hotelDate) {
        Boolean isSameTureDate = false;
        Boolean isSameHotalDate = false;
        //游玩日期是否相同
        if (item.getTourScheduleDate() != null) {
            if (item.getTourScheduleDate().equals( new Timestamp(tureDate.getTime())))
                isSameTureDate = true;
            else
                isSameTureDate = false;
        } else {
            if (tureDate == null) isSameTureDate = true;
            else isSameTureDate = false;
        }

        //入住日期是否相同
        if (item.getHotelScheduleDate() != null) {
            if (item.getHotelScheduleDate().equals(new Timestamp(hotelDate.getTime())))
                isSameHotalDate = true;
            else
                isSameHotalDate = false;
        } else {
            if (hotelDate == null) isSameHotalDate = true;
            else isSameHotalDate = false;
        }

        return isSameTureDate && isSameHotalDate;
    }

    /**
     * 设置购物车商品项无效
     *
     * @param productId 商品标识集合
     * @return
     */
    @Override
    public void setShoppingItemInvalid(Long productId) {
        List<ShoppingItem> items = repository.getProductShoppingItem(productId);
        items.stream().forEach(i -> {
            i.setIsValid(false);
            i.setLastUpdateTime(new Date());
            repository.save(i);
        });
    }

    /**
     * 增加购物车商品项数量
     *
     * @param shoppingItemId 购物车项标识
     * @param count          增加数量
     */
    @Override
    public CommonResult increaseItemCount(Long shoppingItemId, Integer count) {
        ShoppingItem item = repository.get(shoppingItemId);
        if (null != item && item.isValid) {
            item.increaseItemCount(count);
            item.setLastUpdateTime(new Date());
            return ResultFactory.commonSuccess(repository.save(item));
        }
        return ResultFactory.commonError(shoppingItemId + " don't exist or is not valid !");
    }


    /**
     * 设置购物车商品数量
     *
     * @param shoppingItemId 购物车项标识
     * @param count          增加数量
     */
    public CommonResult setItemCount(Long shoppingItemId, Integer count) {

        ShoppingItem item = repository.get(shoppingItemId);
        if (null != item && item.isValid) {
            item.setCount(count);
            item.setLastUpdateTime(new Date());
            return ResultFactory.commonSuccess(repository.save(item));
        }
        return ResultFactory.commonError(shoppingItemId + " don't exist or is not valid !");
    }


    /**
     * 设置商品信息
     * @param shoppingItemId
     * @param count
     * @param hotelDate
     * @param tureDate
     * @return
     */
    public CommonResult SetProductCountAndScheduleDate(Long shoppingItemId, Integer count,Date hotelDate,Date tureDate) {

        ShoppingItem item = repository.get(shoppingItemId);
        if (null != item&&item.isValid) {
            item.setCount(count);
            item.setLastUpdateTime(new Date());
            item.setTourScheduleDate(tureDate);
            item.setHotelScheduleDate(hotelDate);
            return ResultFactory.commonSuccess(repository.save(item));
        }
        return ResultFactory.commonError(shoppingItemId + " don't exist or is not valid !");
    }


    /**
     * 减少购物车商品数量
     *
     * @param shoppingItemId 购物车项标识
     * @param count          减少数量
     */
    @Override
    public CommonResult reduceItemCount(Long shoppingItemId, Integer count) {
        ShoppingItem item = repository.get(shoppingItemId);
        if (null != item && item.isValid) {
            item.reduceItemCount(count);
            item.setLastUpdateTime(new Date());
            return ResultFactory.commonSuccess(repository.save(item));
        }
        return ResultFactory.commonError(shoppingItemId + " don't exist or is not valid !");
    }

    /**
     * 移除购物车商品项
     *
     * @param itemId 待移除项标识
     */
    @Override
    public CommonResult removeItem(Long itemId) {
        try {
            repository.delete(itemId);
            return ResultFactory.commonSuccess();
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }
    }

    /**
     * 批量移除商品项
     *
     * @param itemIds 待移除项集合
     */
    @Override
    public CommonResult removeItems(List<Long> itemIds) {
        try {
            itemIds.stream().forEach(i -> repository.delete(i));
            return ResultFactory.commonSuccess();
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }
    }

    @Override
    public ShoppingItemDto get(Long itemId) {
        ShoppingItem item = repository.get(itemId);
        if (null != item)
            return ShoppingCarFactory.convert(item);
        return null;
    }

    /**
     * 获取购物车商品项
     *
     * @param itemIds 购物车商品项标识
     * @return
     */
    public List<ShoppingItemDto> get(List<Long> itemIds) {
        Criteria criteria = getSession().createCriteria(ShoppingItem.class);
        criteria.add(Restrictions.eq("isActive", true));
        criteria.add(Restrictions.in("id", itemIds));
        if (criteria.list() == null || criteria.list().size() == 0) return null;
        List<ShoppingItemDto> dtos= ShoppingCarFactory.convert(criteria.list());
        if(dtos.size()>0){
            dtos.stream().forEach(i->i.setRejectAreasNames(iProductService.editRejectAreasNames(i.getRejectAreas())));
        }
        return dtos;
    }

    /**
     * 获取游客购物车清单
     *
     * @param touristId 游客标识
     * @return 购物车清单
     */
    @Override
    public List<ShoppingItemDto> getTouristShoppingItems(Long touristId) {
        List<ShoppingItem> items = repository.getTouristShoppingItem(touristId);
        return ShoppingCarFactory.convert(items);
    }

    /**
     * 获取游客购物车商品数量
     *
     * @param touristId 游客标识
     * @return 购物车清单
     */
    @Override
    public int getItemsCount(Long touristId) {
        return getTouristShoppingItems(touristId) == null ? 0 : getTouristShoppingItems(touristId).size();
    }

    /**
     * 清空购物车
     *
     * @param touristId 游客标识
     * @return 清空是否成功
     */
    @Override
    public CommonResult clearShoppingCar(Long touristId) {
        try {
            List<ShoppingItem> items = repository.getTouristShoppingItem(touristId);
            if (null != items && !items.isEmpty())
                items.stream().forEach(i -> repository.delete(i.getId()));
            return ResultFactory.commonSuccess();
        } catch (Exception ex) {
            return ResultFactory.commonError(ex.getMessage());
        }
    }


}
