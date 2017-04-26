package com.sunesoft.seera.yc.core.activity.domain.infrastructure.hibernate;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateRepository;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.activity.domain.*;
import com.sunesoft.seera.yc.core.activity.domain.creteria.ActivityCriteria;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiazl on 2016/8/6.
 */
@Service("shareActivityRepository")
public class ShareActivityRepositoryImpl extends GenericHibernateRepository<ShareActivity, Long> implements ShareActivityRepository {


}
