package com.sunesoft.seera.yc.core.order.domain.infrastructure.hibernate;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateRepository;
import com.sunesoft.seera.yc.core.order.domain.IProductHistoryRepository;
import com.sunesoft.seera.yc.core.order.domain.ProductHistory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xiazl on 2016/7/25.
 */
@Service("iProductHistoryRepository")
public class ProductHistoryRepositoryImpl extends GenericHibernateRepository<ProductHistory,Long> implements IProductHistoryRepository {

    /**
     * 找出与商品对应的历史订单(ProductHistory)
     * @param originalId
     * @return ProductHistory
     */
    @Override
    public ProductHistory getLatestByOriginalId(Long originalId) {
        Criteria criterion=getSession().createCriteria(ProductHistory.class);
        criterion.add(Restrictions.eq("originalId",originalId));
        criterion.addOrder(Order.desc("lastUpdateTime"));
        //TODO　获取最新版本的商品历史版本
        List<ProductHistory> list=criterion.list();
        if(list==null||list.size()<1){
            return null;
        }
        return list.get(0);
    }
}
