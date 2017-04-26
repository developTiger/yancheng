package com.sunesoft.seera.yc.webapp.model;

import com.sunesoft.seera.yc.core.lottery.application.dtos.SubjectDto;

import java.util.List;

/**
 * Created by kkk on 2017/1/9.
 */
public class Light {

    private SubjectDto subjectDto;
    private List<SubjectDto> list;

    public SubjectDto getSubjectDto() {
        return subjectDto;
    }

    public void setSubjectDto(SubjectDto subjectDto) {
        this.subjectDto = subjectDto;
    }

    public List<SubjectDto> getList() {
        return list;
    }

    public void setList(List<SubjectDto> list) {
        this.list = list;
    }
}
