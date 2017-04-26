package com.sunesoft.seera.yc.core.uAuth.application;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateFinder;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.yc.core.uAuth.domain.SysResource;
import com.sunesoft.seera.yc.core.uAuth.domain.WxToken;
import com.sunesoft.seera.yc.core.uAuth.domain.WxTokenRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by zhouz on 2016/5/25.
 */

@Service("wxTokenService")
public class WxTokenServiceImpl extends GenericHibernateFinder implements WxTokenService {

    @Autowired
    WxTokenRepository wxTokenRepository;

    public CommonResult AddOrUpdateToken(WxToken token) {

        WxToken nowToken = this.getTocken();
        if (nowToken != null) {
            nowToken.setToken(token.getToken());
            nowToken.setTicket(token.getTicket());
            nowToken.setTimeStamp(token.getTimeStamp());
            nowToken.setLastUpdateTime(new Date());
            nowToken.setAttr1(token.getAttr1());
            nowToken.setAttr2(token.getAttr2());

        } else
            nowToken = token;
        wxTokenRepository.save(nowToken);

        return ResultFactory.commonSuccess();
    }

    public WxToken getTocken() {
        Criteria criterion = getSession().createCriteria(WxToken.class);
        List<WxToken> list = criterion.list();
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else
            return null;
    }


}
