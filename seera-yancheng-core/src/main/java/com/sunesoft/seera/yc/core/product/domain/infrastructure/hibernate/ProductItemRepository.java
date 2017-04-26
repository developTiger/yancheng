package com.sunesoft.seera.yc.core.product.domain.infrastructure.hibernate;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateRepository;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.product.domain.IProductItemRepository;
import com.sunesoft.seera.yc.core.product.domain.ProductItem;
import com.sunesoft.seera.yc.core.product.domain.criteria.ProductItemCriteria;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

/**
 * 商品项数据仓储Hibernate实现
 * Created by zhaowy on 2016/7/11.
 */
@Service("iProductItemRepository")
public class ProductItemRepository
        extends GenericHibernateRepository<ProductItem,Long> implements IProductItemRepository {

    /**
     * @param num 商品编码
     * @return 匹配的商品信息，否则范围NULL
     */
    @Override
    public ProductItem GetByNum(String num) {
        Criteria criterion = getSession().createCriteria(ProductItem.class);
        criterion.add(Restrictions.eq("num", num));
        return (ProductItem) criterion.uniqueResult();
    }

    /**
     * 查询商品
     *
     * @param criteria
     * @return 商品项简要信息集合
     */
    @Override
    public PagedResult<ProductItem> findProductsItems(ProductItemCriteria criteria) {
        Criteria criterion = getSession().createCriteria(ProductItem.class);
        criterion.add(Restrictions.eq("isActive",true));
        if (!StringUtils.isNullOrWhiteSpace(criteria.getName()))
            criterion.add(Restrictions.like("name", "%" + criteria.getName() + "%"));
        if (!StringUtils.isNullOrWhiteSpace(criteria.getNum()))
            criterion.add(Restrictions.like("num", "%" + criteria.getNum() + "%"));
        if (!StringUtils.isNullOrWhiteSpace(criteria.getSeller()))
            criterion.add(Restrictions.like("seller", "%" + criteria.getSeller() + "%"));
        if (null != criteria.getType())
            criterion.add(Restrictions.eq("productItemType", criteria.getType()));
        int totalCount = ((Long) criterion.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criterion.setProjection(null);
        criterion.addOrder(
                criteria.isAscOrDesc()?
                         Order.asc(criteria.getOrderByProperty()==null?"id":criteria.getOrderByProperty())
                        :Order.desc(criteria.getOrderByProperty()==null?"id":criteria.getOrderByProperty()));
        criterion.setFirstResult(
                (criteria.getPageNumber() - 1) * criteria.getPageSize()).setMaxResults(criteria.getPageSize());
        return new PagedResult<>(criterion.list(), criteria.getPageNumber(), criteria.getPageSize(), totalCount);
    }

}
