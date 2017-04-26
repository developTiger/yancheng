package com.sunesoft.seera.yc.core.findbackpassword.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristDto;
import com.sunesoft.seera.yc.core.tourist.domain.criteria.TouristCriteria;

/**
 * 游客服务接口
 * Created by zhaowy on 2016/7/11.
 */
public interface FindBackPasswordService {

    /**
     * 检查账号是否存在
     */
    public CommonResult checkAccount(TouristCriteria touristCriteria);
    public TouristDto checkAccount(String email);

    public boolean send(String mail);

    CommonResult checkApprovedFindBack(String key,String token);

    CommonResult updatePassword(String userName, String password);
}
