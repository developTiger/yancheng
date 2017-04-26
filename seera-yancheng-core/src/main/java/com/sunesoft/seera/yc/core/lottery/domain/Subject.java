package com.sunesoft.seera.yc.core.lottery.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;
import com.sunesoft.seera.yc.core.activity.domain.ActivityType;

import javax.persistence.Entity;

/**
 * Created by kkk on 2017/1/9.
 */

@Entity(name = "subject")
public class Subject extends BaseEntity {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     *答案
     */
    private String answer;

    /**
     *标识
     */
    private String identity;

    /**
     *  0:原创题; 1：普通题
     */
    private Integer type;

    /**
     * true 可答题，false 已答完。
     */
    private Boolean status;

    /**
     * true 可领奖，false 不可领奖
     * @return
     */
    private Boolean receive;


    private Long answerUserId;

    public Subject() {
        setIsActive(true);
        this.status=true;
        this.receive=true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getReceive() {
        return receive;
    }

    public void setReceive(Boolean receive) {
        this.receive = receive;
    }

    public Long getAnswerUserId() {
        return answerUserId;
    }

    public void setAnswerUserId(Long answerUserId) {
        this.answerUserId = answerUserId;
    }
}
