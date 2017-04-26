package com.sunesoft.seera.yc.core.shoppingCar.domain.infrastructure.hibernate;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateRepository;
import com.sunesoft.seera.yc.core.shoppingCar.domain.IShoppingCarRepository;
import com.sunesoft.seera.yc.core.shoppingCar.domain.ShoppingItem;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhaowy on 2016/7/18.
 */
@Service("iShoppingCarRepository")
public class ShoppingCarRepositoryImpl
        extends GenericHibernateRepository<ShoppingItem,Long> implements IShoppingCarRepository {

    /**
     * 获取游客购物车商品项
     *
     * @param touristId 游客标识
     * @return
     */
    @Override
    public List<ShoppingItem> getTouristShoppingItem(Long touristId) {
        Criteria criterion = getSession().createCriteria(ShoppingItem.class);
        criterion.add(Restrictions.eq("isActive",true));
        criterion.createAlias("tourist","tourist");
        criterion.add(Restrictions.eq("tourist.id", touristId));
        return criterion.list();
    }

    /**
     * 获取包含指定商品的所有购物车商品项
     *
     * @param productId 商品标识
     * @return
     */
    @Override
    public List<ShoppingItem> getProductShoppingItem(Long productId) {
        Criteria criterion = getSession().createCriteria(ShoppingItem.class);
        criterion.add(Restrictions.eq("isActive",true));
        criterion.createAlias("product","product");
        criterion.add(Restrictions.eq("product.id", productId));
        return criterion.list();
    }
}

