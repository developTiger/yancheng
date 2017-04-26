package com.sunesoft.seera.yc.core.article.domain;

import com.sunesoft.seera.fr.ddd.infrastructure.IRepository;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.article.domain.criteria.ArticleCriteria;

import java.util.List;


/**
 * 文章仓储存接口
 * Created by xiazl on 2016/8/8.
 */
public interface IArticleRepository extends IRepository<Article, Long> {

    /**
     * 根据编号查询
     * @param num
     * @return
     */
    public Article getByNum(String num);


    /**
     * 根据编辑人查询
     * @param author
     * @return
     */
    public List<Article> getByAuthor(String author);
    /**
     * 根据类型查询
     * @param type
     * @return
     */
    public List<Article> getByType(ArticleType type);
    /**
     * 根据类型查询
     * @param title
     * @return
     */
    public List<Article> getByTitle(String title);

    /**
     * 分页查询
     * @return
     */
    public PagedResult<Article> findPage(ArticleCriteria criteria);

    /**
     * 获取所有文章
     * @return
     */
    public List<Article> getAll();
}
