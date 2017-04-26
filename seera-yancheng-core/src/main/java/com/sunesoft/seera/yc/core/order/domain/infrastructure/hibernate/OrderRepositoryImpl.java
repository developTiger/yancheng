package com.sunesoft.seera.yc.core.order.domain.infrastructure.hibernate;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateRepository;
import com.sunesoft.seera.yc.core.order.domain.IOrderRepository;
import com.sunesoft.seera.yc.core.order.domain.OrderInfo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

/**
 * Created by zhaowy on 2016/7/11.
 */
@Service("iOrderRepository")
public class OrderRepositoryImpl
        extends GenericHibernateRepository<OrderInfo,Long> implements IOrderRepository {
    @Override
    public OrderInfo get(String orderNum) {

        Criteria criterion = getSession().createCriteria(OrderInfo.class);
        criterion.add(Restrictions.eq("num", orderNum));
        return (OrderInfo) criterion.uniqueResult();
    }
}
