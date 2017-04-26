package com.sunesoft.seera.yc.core.article.application;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateFinder;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.yc.core.article.application.DocumentFactory.ArticleFactory;
import com.sunesoft.seera.yc.core.article.application.dtos.ArticleDto;
import com.sunesoft.seera.yc.core.article.domain.Article;
import com.sunesoft.seera.yc.core.article.domain.ArticleType;
import com.sunesoft.seera.yc.core.article.domain.IArticleRepository;
import com.sunesoft.seera.yc.core.article.domain.criteria.ArticleCriteria;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by xiazl on 2016/8/8.
 */
@Service("iArticleService")
public class ArticleServiceImpl extends GenericHibernateFinder implements IArticleService {

    @Autowired
    IArticleRepository repository;

    /**
     * 根据文章编号获取文章
     * @param num
     * @return
     */
    public ArticleDto getByNum(String num){
        Article article=repository.getByNum(num);
        if(article!=null)return ArticleFactory.convert(article);
        return null;
    }

    /**
     * 增加文章
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult create(ArticleDto dto) {
        if (dto == null) return ResultFactory.commonError("传入不能为null");
        Article article = ArticleFactory.convert(dto);
        return ResultFactory.commonSuccess(repository.save(article));
    }

    /**
     * 修改文章
     *
     * @param dto
     * @return
     */
    @Override
    public CommonResult edit(ArticleDto dto) {
        if (dto == null) return ResultFactory.commonError("传入不能为null");
        Article article = repository.get(dto.getId());
        if (article != null && article.getIsActive()) {
            article = ArticleFactory.convert(dto, article);
            article.setLastUpdateTime(new Date());
            return ResultFactory.commonSuccess(repository.save(article));
        }
        return ResultFactory.commonError("该文章不存在");
    }

    /**
     * 根据id获取
     *
     * @param id
     * @return
     */
    @Override
    public ArticleDto getById(Long id) {
        Article article = repository.get(id);
        if(article !=null&& article.getIsActive())
        return ArticleFactory.convert(article);
        return null;
    }

    /**
     * 根据ids获取
     *
     * @param ids
     * @return
     */
    @Override
    public List<ArticleDto> getByIds(List<Long> ids) {
        Criteria criterion=getSession().createCriteria(Article.class);
        criterion.add(Restrictions.eq("isActive",true));
        criterion.add(Restrictions.in("id",ids));
        List<Article> list=criterion.list();
        if(list!=null&&list.size()>0)return ArticleFactory.convertList(list);
        return null;
    }

    /**
     * 根据作者获取
     *
     * @param author
     * @return
     */
    @Override
    public List<ArticleDto> getByAuthor(String author) {
        List<Article> articles =repository.getByAuthor(author);
        if(articles !=null)return ArticleFactory.convertList(articles);
        return null;
    }

    /**
     * 根据类型获取
     *
     * @param type
     * @return
     */
    @Override
    public List<ArticleDto> getByType(ArticleType type) {
        List<Article> articles =repository.getByType(type);
        if(articles !=null)return ArticleFactory.convertList(articles);
        return null;
    }
    /**
     * 根据类型获取
     * @param title
     * @return
     */
    public List<ArticleDto> getByTitle(String title){
        List<Article> articles =repository.getByTitle(title);
        if(articles !=null)return ArticleFactory.convertList(articles);
        return null;
    }
    /**
     * 更加ids删除
     *
     * @param ids
     * @return
     */
    @Override
    public CommonResult delete(List<Long> ids) {
        Criteria criterion=getSession().createCriteria(Article.class);
        criterion.add(Restrictions.eq("isActive",true));
        criterion.add(Restrictions.in("id",ids));
        List<Article> list=criterion.list();
        if(list!=null&&list.size()>0){
            list.stream().forEach(i->repository.delete(i.getId()));
            return ResultFactory.commonSuccess();
        }
        return ResultFactory.commonError("请选中删除文章");
    }

    /**
     * 分页查询
     *
     * @param criteria
     * @return
     */
    @Override
    public PagedResult<ArticleDto> findPage(ArticleCriteria criteria) {
        PagedResult<Article> pg=repository.findPage(criteria);
        if(pg!=null)
        return ArticleFactory.convertPg(pg);
        return new PagedResult<ArticleDto>(1,10);
    }

    /**
     * 获取所有文章
     *
     * @return
     */
    public List<ArticleDto> getAll(){
        List<Article> list=repository.getAll();
        if(list==null||list.isEmpty()) return null;
        return ArticleFactory.convertList(list);
    }
}
