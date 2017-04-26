package com.sunesoft.seera.yc.core.article.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.article.application.dtos.ArticleDto;
import com.sunesoft.seera.yc.core.article.domain.ArticleType;
import com.sunesoft.seera.yc.core.article.domain.criteria.ArticleCriteria;


import java.util.List;


/**
 * Created by xiazl on 2016/8/8.
 */
public interface IArticleService {

    /**
     * 根据文章编号获取文章
     * @param num
     * @return
     */
    public ArticleDto getByNum(String num);

    /**
     * 增加文章
     *
     * @param dto
     * @return
     */
    public CommonResult create(ArticleDto dto);

    /**
     * 修改文章
     *
     * @param dto
     * @return
     */
    public CommonResult edit(ArticleDto dto);

    /**
     * 根据id获取
     *
     * @param id
     * @return
     */
    public ArticleDto getById(Long id);

    /**
     * 根据ids获取
     *
     * @param ids
     * @return
     */
    public List<ArticleDto> getByIds(List<Long> ids);

    //region Description 可以从findPage里取，但是这里是方便了前台

    /**
     * 根据作者获取
     *
     * @param author
     * @return
     */
    public List<ArticleDto> getByAuthor(String author);

    /**
     * 根据类型获取
     *
     * @param type
     * @return
     */
    public List<ArticleDto> getByType(ArticleType type);

    /**
     * 根据类型获取
     *
     * @param title
     * @return
     */
    public List<ArticleDto> getByTitle(String title);
    //endregion

    /**
     * 更加ids删除
     *
     * @param ids
     * @return
     */
    public CommonResult delete(List<Long> ids);

    /**
     * 分页查询
     *
     * @param criteria
     * @return
     */
    public PagedResult<ArticleDto> findPage(ArticleCriteria criteria);

    /**
     * 获取所有文章
     *
     * @return
     */
    public List<ArticleDto> getAll();

}
