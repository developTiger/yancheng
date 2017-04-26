package com.sunesoft.seera.yc.core.lottery.application.dtos;

/**
 * Created by kkk on 2017/1/9.
 */
public class SubjectDto {


    private Long id;
    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     *标识
     */
    private String identity;

    /**
     *答案
     */
    private String answer;

    /**
     * 0：原创题; 1:普通题
     */
    private Integer type;

    /**
     * true 可答题，false 已答完。
     */
    private Boolean status;


    private Long answerUserId;
    /**
     * false 不可领奖，true 可领奖
     * @return
     */
    private Boolean receive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
