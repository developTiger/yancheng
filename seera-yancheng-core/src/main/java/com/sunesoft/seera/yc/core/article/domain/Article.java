package com.sunesoft.seera.yc.core.article.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;
import com.sunesoft.seera.fr.utils.NumGenerator;

import javax.persistence.*;

/**
 * Created by xiazl on 2016/8/8.
 */
@Entity
@Table(name = "article")
public class Article extends BaseEntity {

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

    @Basic(fetch = FetchType.EAGER)
    @Column(columnDefinition="text")
    private String content;

    public Article() {
        this.num= NumGenerator.generate();
        this.type=ArticleType.HelpCenter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
