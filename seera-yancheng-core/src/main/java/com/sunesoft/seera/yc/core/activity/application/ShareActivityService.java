package com.sunesoft.seera.yc.core.activity.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.activity.application.dtos.ShareActivityDto;
import com.sunesoft.seera.yc.core.activity.domain.creteria.ShareActivityCriteria;

/**
 * Created by zwork on 2016/12/7.
 */
public interface ShareActivityService {
    /**
     * 新增分享活动
     * @param dto
     * @return
     */
       CommonResult addShare(ShareActivityDto dto);


    /**
     * 查看活动页面
     * @param sharedUserAppId 分享人的Appid
     * @return
     */
     ShareActivityDto getShare(String sharedUserAppId);
    /**
     * 分享计数加1
     * @param inComeOpenId 查看人的Appid
     * @param inComeWxName 查看人的微信名称
     * @param sharedUserAppId 分享人的Appid
     * @return
     */
    CommonResult likeShare(String inComeOpenId,String inComeWxName,String sharedUserAppId);

    /**
     * 查询活动情况
     * @param criteria 查询条件
     * @return
     */
    PagedResult<ShareActivityDto> getShareDtoPaged(ShareActivityCriteria criteria);


    /**
     *
     * @param inComeOpenId
     * @return
     */
    Boolean isLiked(String inComeOpenId, String sharedUserAppId);
}
