package com.sunesoft.seera.yc.core.article.domain.criteria;

import com.sunesoft.seera.fr.results.PagedCriteria;
import com.sunesoft.seera.yc.core.article.domain.ArticleType;

import java.util.Date;

/**
 * Created by xiazl on 2016/8/8.
 */
public class ArticleCriteria extends PagedCriteria {

    /**
     * 文章标题
     */
    private String title;
    /**
     * 文章类型
     */
    private ArticleType type;
    /**
     * 文章作者
     */
    private String author;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArticleType getType() {
        return type;
    }

    public void setType(ArticleType type) {
        this.type = type;
    }
}
