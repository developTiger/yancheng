package com.sunesoft.seera.yc.core.tourist.domain;

import com.sunesoft.seera.fr.ddd.infrastructure.IRepository;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristSimpleDto;
import com.sunesoft.seera.yc.core.tourist.domain.criteria.TouristCriteria;

import java.util.List;

/**
 * Created by zhaowy on 2016/7/11.
 */
public interface ITouristRepository extends IRepository<Tourist,Long> {

    /**
     * 校验游客Token与密码匹配
     * @param openId  userName|wxName|mobilePhone|email
     * @return
     */
    public Tourist checkOpenId(String openId);

    /**
     * wxoauth
     * @param token
     * @param pwd
     * @return
     */
    public UniqueResult<Tourist>  login(String token, String pwd);

    /**
     * 校验游客Token存在性
     *
     * @param token userName|wxName|mobilePhone|email
     * @return
     */
    public Tourist check(String token);

    /**
     * 查询游客信息
     *
     * @param criteria
     * @return 游客信息集合
     */
    public PagedResult<Tourist> findTourists(TouristCriteria criteria);


    /**
     * 根据电话获取游客列表
     * @param phones
     * @return
     */
    public List<Tourist> findTouristsByPhone(List<String> phones);


}
