package com.sunesoft.seera.yc.core.article.application.DocumentFactory;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.Factory;
import com.sunesoft.seera.yc.core.article.application.dtos.ArticleDto;
import com.sunesoft.seera.yc.core.article.domain.Article;
import com.sunesoft.seera.yc.core.parameter.application.factory.DtoFactory;

import java.util.List;

/**
 * Created by xiazl on 2016/8/8.
 */
public class ArticleFactory extends Factory{

    /**
     * 将DocumentDto->Document
     * @param dto
     * @return
     */
    public static Article convert(ArticleDto dto){
        return Factory.convert(dto,Article.class);
    }

    /**
     * 这个方法主要使用在修改的时候，防止报错同样id的对象已经存在
     * 将DocumentDto->Document
     * @param dto
     * @return
     */
    public static Article convert(ArticleDto dto,Article article){
        article = DtoFactory.convert(dto, article);
        return article;
    }

    /**
     * 将Document->DocumentDto
     * @param article
     * @return
     */
    public static ArticleDto convert(Article article){
        return Factory.convert(article,ArticleDto.class);
    }

    /**
     * 将List<Document> -> List<DocumentDto>
     * @param articles
     * @return
     */
    public static List<ArticleDto> convertList(List<Article> articles){
        return Factory.convert(articles,ArticleDto.class);
    }

    /**
     * PagedResult<Document>->PagedResult<DocumentDto>
     * @param pg
     * @return
     */
    public static PagedResult<ArticleDto> convertPg(PagedResult<Article> pg){
        return Factory.convert(pg,ArticleDto.class);
    }

}
