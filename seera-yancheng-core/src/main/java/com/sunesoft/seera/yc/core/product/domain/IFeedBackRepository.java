package com.sunesoft.seera.yc.core.product.domain;

import com.sunesoft.seera.fr.ddd.infrastructure.IRepository;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.product.domain.criteria.FeedBackCriteria;

/**
 * 商品数据仓储接口
 * Created by zhaowy on 2016/7/11.
 */
public interface IFeedBackRepository extends IRepository<FeedBack,Long> {

    /**
     * 查询商品评论
     *
     * @param criteria
     * @return 商品评论信息集合
     */
    public PagedResult<FeedBack> findFeedBacks(FeedBackCriteria criteria);

}
