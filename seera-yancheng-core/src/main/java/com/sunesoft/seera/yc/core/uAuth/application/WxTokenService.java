package com.sunesoft.seera.yc.core.uAuth.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.uAuth.application.criteria.ResourceCriteria;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.ResourceDto;
import com.sunesoft.seera.yc.core.uAuth.domain.SysResource;
import com.sunesoft.seera.yc.core.uAuth.domain.WxToken;

import java.util.List;

/**
 * Created by zhouz on 2016/5/25.
 */
public interface WxTokenService {


    CommonResult AddOrUpdateToken(WxToken token);

    WxToken getTocken();




}
