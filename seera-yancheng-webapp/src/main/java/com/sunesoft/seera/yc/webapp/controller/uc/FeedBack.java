package com.sunesoft.seera.yc.webapp.controller.uc;

import com.sunesoft.seera.yc.core.product.application.dtos.FeedBackDto;

import java.util.List;

/**
 * Created by temp on 2016/10/18.
 */
public class FeedBack {

    private List<FeedBackDto> feedBackDtos;


    public List<FeedBackDto> getFeedBackDtos() {
        return feedBackDtos;
    }

    public void setFeedBackDtos(List<FeedBackDto> feedBackDtos) {
        this.feedBackDtos = feedBackDtos;
    }
}
