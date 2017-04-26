package com.sunesoft.seera.yc.core.order.domain.infrastructure.hibernate;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateRepository;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.order.domain.IOrderProductRepository;
import com.sunesoft.seera.yc.core.order.domain.OrderProduct;
import com.sunesoft.seera.yc.core.order.domain.OrderProductStatus;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xiazl on 2016/7/25.
 */
@Service("iOrderProductRepository")
public class OrderProductRepositoryImpl extends GenericHibernateRepository<OrderProduct,Long> implements IOrderProductRepository {

    /**
     * 根据原始商品id获取订单商品
     * @param originalId
     * @return
     */
    @Override
    public List<OrderProduct> getByProductHistory(Long originalId) {
        Criteria criterion=getSession().createCriteria(OrderProduct.class);
        criterion.createAlias("ProductHistory","ProductHistory");
        criterion.add(Restrictions.eq("product.originalId",originalId));
        return criterion.list();
    }


    /**
     * 订单商品查询
     * @param orderProductStatus
     * @return
     */
    @Override
    public List<OrderProduct> findOrderProduct(OrderProductStatus orderProductStatus) {
        Criteria criterion=getSession().createCriteria(OrderProduct.class);
        criterion.add(Restrictions.eq("isActive",true));
        if(orderProductStatus!=null)
        criterion.add(Restrictions.eq("status",orderProductStatus));
        criterion.addOrder(Order.desc("lastUpdateTime"));
        return criterion.list();
    }
}
