package com.sunesoft.seera.yc.core.tourist.domain;

import com.sunesoft.seera.fr.ddd.infrastructure.IRepository;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.yc.core.tourist.domain.Tourist;
import com.sunesoft.seera.yc.core.tourist.domain.YearCardInfo;
import com.sunesoft.seera.yc.core.tourist.domain.criteria.TouristCriteria;

/**
 * Created by zhaowy on 2016/7/11.
 */
public interface YearCardInfoRepository extends IRepository<YearCardInfo,Long> {

    YearCardInfo getYearCartByPhone(String phone);

}
