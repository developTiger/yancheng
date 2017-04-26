package com.sunesoft.seera.yc.core.product.domain.infrastructure.hibernate;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateRepository;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.product.domain.FeedBack;
import com.sunesoft.seera.yc.core.product.domain.IFeedBackRepository;
import com.sunesoft.seera.yc.core.product.domain.Product;
import com.sunesoft.seera.yc.core.product.domain.ProductItem;
import com.sunesoft.seera.yc.core.product.domain.criteria.FeedBackCriteria;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

/**
 * Created by zhaowy on 2016/7/26.
 */
@Service("iFeedBackRepository")

public class FeedBackRepositoryImpl extends GenericHibernateRepository<FeedBack,Long>
        implements IFeedBackRepository {
    /**
     * 查询商品评论
     *
     * @param criteria
     * @return 商品评论信息集合
     */
    @Override
    public PagedResult<FeedBack> findFeedBacks(FeedBackCriteria criteria) {
        Criteria criterion = getSession().createCriteria(FeedBack.class);
//                criterion.createAlias("product","product");
//        criterion.add(Restrictions.eq("product.id", criteria.getProductId()));
        criterion.add(Restrictions.eq("productId", criteria.getProductId()));
        int totalCount = ((Long) criterion.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criterion.setProjection(null);
        criterion.addOrder(
                criteria.isAscOrDesc()?
                        Order.asc(criteria.getOrderByProperty()==null?"createDateTime":criteria.getOrderByProperty())
                        :Order.desc(criteria.getOrderByProperty()==null?"createDateTime":criteria.getOrderByProperty()));
        criterion.setFirstResult(
                (criteria.getPageNumber() - 1) * criteria.getPageSize()).setMaxResults(criteria.getPageSize());
        return new PagedResult<>(criterion.list(), criteria.getPageNumber(), criteria.getPageSize(), totalCount);
    }
}
