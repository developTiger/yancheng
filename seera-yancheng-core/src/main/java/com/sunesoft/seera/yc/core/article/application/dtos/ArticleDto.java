package com.sunesoft.seera.yc.core.article.application.dtos;


import com.sunesoft.seera.yc.core.article.domain.ArticleType;

import java.util.Date;

/**
 * Created by xiazl on 2016/8/8.
 */
public class ArticleDto {

    /**
     * 文章编号
     */
    private String num;

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
     * 文章内容
     */
    private String content;

    private Long id;

    /**
     * 创建时间
     */
    private Date createDateTime ; // 创建时间

    /**
     * 最后修改时间
     */
    private Date lastUpdateTime; // 最后修改时间

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
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
