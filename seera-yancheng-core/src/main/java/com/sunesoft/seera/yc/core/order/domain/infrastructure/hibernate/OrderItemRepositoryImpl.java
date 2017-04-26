package com.sunesoft.seera.yc.core.order.domain.infrastructure.hibernate;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateRepository;
import com.sunesoft.seera.yc.core.order.domain.IOrderItemRepository;
import com.sunesoft.seera.yc.core.order.domain.IOrderRepository;
import com.sunesoft.seera.yc.core.order.domain.OrderInfo;
import com.sunesoft.seera.yc.core.order.domain.OrderProductItem;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

/**
 * Created by zhaowy on 2016/7/11.
 */
@Service("iOrderItemRepository")
public class OrderItemRepositoryImpl
        extends GenericHibernateRepository<OrderProductItem, Long> implements IOrderItemRepository {

    @Override
    public OrderProductItem getByOriginalId(Long originalId) {
        Criteria criterion = getSession().createCriteria(OrderProductItem.class);
        criterion.add(Restrictions.eq("originalId", originalId));
        return null != criterion.uniqueResult() ? (OrderProductItem) criterion.uniqueResult() : null;
    }

    /**
     * 根据商品商品项提取码获取订单商品项
     *
     * @param takeNum
     * @return
     */
    @Override
    public OrderProductItem get(String takeNum) {
        Criteria criterion = getSession().createCriteria(OrderProductItem.class);
        criterion.add(Restrictions.eq("takeNum", takeNum));
        return null != criterion.uniqueResult() ? (OrderProductItem) criterion.uniqueResult() : null;
    }
}
