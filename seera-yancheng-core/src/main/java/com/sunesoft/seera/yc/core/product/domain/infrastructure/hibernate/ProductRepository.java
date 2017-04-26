package com.sunesoft.seera.yc.core.product.domain.infrastructure.hibernate;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateRepository;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.product.domain.IProductRepository;
import com.sunesoft.seera.yc.core.product.domain.Product;
import com.sunesoft.seera.yc.core.product.domain.ProductStatus;
import com.sunesoft.seera.yc.core.product.domain.criteria.ProductCriteria;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 商品数据仓储Hibernate实现
 * Created by zhaowy on 2016/7/11.
 */
@Service("iProductRepository")
public class ProductRepository
    extends GenericHibernateRepository<Product,Long> implements IProductRepository {

    @Override
    public Boolean check(String name) {
        Criteria criteria=getSession().createCriteria(Product.class);
        criteria.add(Restrictions.eq("isActive",true));
        criteria.add(Restrictions.eq("name",name));
        if(criteria.list()!=null&&criteria.list().size()>0)
            return true;
        return false;
    }

    /**
     * @param num 商品编码
     * @return 匹配的商品信息，否则范围NULL
     */
    @Override
    public Product getByNum(String num) {
        Criteria criterion = getSession().createCriteria(Product.class);
        criterion.add(Restrictions.eq("num", num));
        return (Product) criterion.uniqueResult();
    }

    /**
     * 查询商品
     *
     * @param criteria
     * @return 商品信息集合
     */
    @Override
    public PagedResult<Product> findProducts(ProductCriteria criteria) {
        Criteria criterion = getSession().createCriteria(Product.class);
        criterion.add(Restrictions.eq("isActive",true));
        if(!StringUtils.isNullOrWhiteSpace(criteria.getRejectAreas())){//不限定限定区域的情况是为null
            if("-1".equals(criteria.getRejectAreas())){
                //这种情况是:
                // 不限定区域的商品
                criterion.add(Restrictions.or(Restrictions.isNull("rejectAreas"),Restrictions.eq("rejectAreas", "")));
            }else if("0".equals(criteria.getRejectAreas())){
                //限定区域的情况
                criterion.add(Restrictions.and(Restrictions.isNotNull("rejectAreas"),Restrictions.ne("rejectAreas","")));
            }else {
                //限定某个限定区域的情况
                criterion.add(Restrictions.like("rejectAreas","%"+criteria.getRejectAreas()+"%"));
            }
        }

        if (!StringUtils.isNullOrWhiteSpace(criteria.getName()))
            criterion.add(Restrictions.like("name", "%" + criteria.getName() + "%"));
        if (!StringUtils.isNullOrWhiteSpace(criteria.getNum()))
            criterion.add(Restrictions.like("num", "%" + criteria.getNum() + "%"));
        if (null != criteria.getType())
            criterion.add(Restrictions.eq("type", criteria.getType()));
        if(null!=criteria.getKind())
            criterion.add(Restrictions.eq("kind", criteria.getKind()));
        if (null != criteria.getStatus())
            criterion.add(Restrictions.eq("status", criteria.getStatus()));
        if(criteria.getStartTime()!=null)
           criterion.add(Restrictions.ge("onSaleTime", criteria.getStartTime()));
        if(criteria.getEndTime()!=null)
            criterion.add(Restrictions.le("onSaleTime", criteria.getEndTime()));
        criterion.add(Restrictions.or(Restrictions.isNull("ctEndDate"), Restrictions.ge("ctEndDate", DateHelper.parse(DateHelper.formatDate(new Date(), "yyyy-MM-dd 23:59:59")))));
    //    criterion.list();
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


    /**
     * 查询包含指定商品项标识的所有商品
     * @param productItemId
     * @return
     */
    public List<Product> getProductWithItemId(Long productItemId)
    {
        Criteria criterion = getSession().createCriteria(Product.class);
        criterion.createAlias("productItemList","t", JoinType.INNER_JOIN);
        criterion.add(Restrictions.eq("t.id", productItemId));
       return criterion.list();
    }

}
