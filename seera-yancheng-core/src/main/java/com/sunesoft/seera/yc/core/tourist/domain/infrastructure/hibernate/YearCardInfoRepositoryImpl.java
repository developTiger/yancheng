package com.sunesoft.seera.yc.core.tourist.domain.infrastructure.hibernate;

import com.sunesoft.seera.fr.ddd.infrastructure.IRepository;
import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateRepository;
import com.sunesoft.seera.yc.core.tourist.domain.Tourist;
import com.sunesoft.seera.yc.core.tourist.domain.YearCardInfo;
import com.sunesoft.seera.yc.core.tourist.domain.YearCardInfoRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

/**
 * Created by zhaowy on 2016/7/11.
 */
@Service("yearCardInfoRepository")
public class YearCardInfoRepositoryImpl
        extends GenericHibernateRepository<YearCardInfo, Long> implements YearCardInfoRepository {


    @Override
    public YearCardInfo getYearCartByPhone(String phone) {
        Criteria criterion = getSession().createCriteria(YearCardInfo.class);
        criterion.add(Restrictions.eq("phoneNo", phone));
        return null != criterion.uniqueResult() ? (YearCardInfo) criterion.uniqueResult() : null;
    }
}
