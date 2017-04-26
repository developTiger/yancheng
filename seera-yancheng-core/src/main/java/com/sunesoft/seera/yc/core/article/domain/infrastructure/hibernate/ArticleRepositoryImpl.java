package com.sunesoft.seera.yc.core.article.domain.infrastructure.hibernate;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateRepository;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.article.domain.Article;
import com.sunesoft.seera.yc.core.article.domain.ArticleType;
import com.sunesoft.seera.yc.core.article.domain.IArticleRepository;
import com.sunesoft.seera.yc.core.article.domain.criteria.ArticleCriteria;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 仓储实现
 * Created by xiazl on 2016/8/8.
 */
@Service("iArticleRepository")
public class ArticleRepositoryImpl extends GenericHibernateRepository<Article, Long> implements IArticleRepository {

    /**
     * 根据编号查询
     *
     * @param num
     * @return
     */
    public Article getByNum(String num) {
        Criteria criterion = getSession().createCriteria(Article.class);
        criterion.add(Restrictions.eq("isActive", true));
        criterion.add(Restrictions.eq("num", num));
        List<Article> list = criterion.list();
        if (list != null && list.size() > 0) return list.get(0);
        return null;
    }

    /**
     * 根据编辑人查询
     *
     * @param author
     * @return
     */
    @Override
    public List<Article> getByAuthor(String author) {
        Criteria criterion = getSession().createCriteria(Article.class);
        criterion.add(Restrictions.eq("isActive", true));
        criterion.add(Restrictions.eq("author", author));
        List<Article> list = criterion.list();
        if (list != null && list.size() > 0) return list;
        return null;
    }

    /**
     * 根据类型查询
     *
     * @param type
     * @return
     */
    @Override
    public List<Article> getByType(ArticleType type) {
        Criteria criterion = getSession().createCriteria(Article.class);
        criterion.add(Restrictions.eq("isActive", true));
        criterion.add(Restrictions.eq("type", type));
        List<Article> list = criterion.list();
        if (list != null && list.size() > 0) return list;
        return null;
    }

    /**
     * 根据类型查询
     *
     * @param title
     * @return
     */
    public List<Article> getByTitle(String title) {
        Criteria criterion = getSession().createCriteria(Article.class);
        criterion.add(Restrictions.eq("isActive", true));
        criterion.add(Restrictions.eq("title", title));
        List<Article> list = criterion.list();
        if (list != null && list.size() > 0) return list;
        return null;
    }

    /**
     * 分页查询
     *
     * @return
     */
    @Override
    public PagedResult<Article> findPage(ArticleCriteria criteria) {
        Criteria criterion = getSession().createCriteria(Article.class);
        criterion.add(Restrictions.eq("isActive", true));
        if (criteria.getType() != null)
            criterion.add(Restrictions.eq("type", criteria.getType()));
        if (!StringUtils.isNullOrWhiteSpace(criteria.getAuthor()))
            criterion.add(Restrictions.like("author", "%" + criteria.getAuthor() + "%"));
        if (!StringUtils.isNullOrWhiteSpace(criteria.getTitle()))
            criterion.add(Restrictions.eq("title", criteria.getTitle()));
        if (criteria.getStartTime() != null)
            criterion.add(Restrictions.ge("createDateTime", criteria.getStartTime()));
        if (criteria.getEndTime() != null)
            criterion.add(Restrictions.le("createDateTime", criteria.getEndTime()));
        int totalCount = ((Long) criterion.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criterion.setProjection(null);
        criterion.addOrder(Order.desc("createDateTime"));
        criterion.setFirstResult((criteria.getPageNumber() - 1) * criteria.getPageSize()).setMaxResults(criteria.getPageSize());
        return new PagedResult<Article>(criterion.list(), criteria.getPageNumber(), criteria.getPageSize(), totalCount);
    }

    /**
     * 获取所有文章
     *
     * @return
     */
    public List<Article> getAll() {
        Criteria criteria = getSession().createCriteria(Article.class);
        criteria.add(Restrictions.eq("isActive", true));
        List<Article> list = criteria.list();
        if (list == null || list.isEmpty()) return null;
        return list;
    }
}
