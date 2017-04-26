package com.sunesoft.seera.yc.core.manager.domain.infrastructure.hibernate;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateRepository;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.manager.application.criteria.ManagerCriteria;
import com.sunesoft.seera.yc.core.manager.domain.IManagerRepository;
import com.sunesoft.seera.yc.core.manager.domain.InnerManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 员工仓储实现类
 * Created by zhaowy on 2016/7/11.
 */
@Service("iManagerRepository")
public class ManagerRepositoryImpl
        extends GenericHibernateRepository<InnerManager, Long> implements IManagerRepository {
    /**
     * 条件查询管理员
     *
     * @param criteria
     * @return
     */
    @Override
    public PagedResult<InnerManager> findManagers(ManagerCriteria criteria) {
        Criteria criterion = getSession().createCriteria(InnerManager.class);
        criterion.add(Restrictions.eq("isActive", true));
        if (criteria.getStatus() != null) {
            criterion.add(Restrictions.eq("status", criteria.getStatus()));
        }
        if (!StringUtils.isNullOrWhiteSpace(criteria.getPhone())) {
            criterion.add(Restrictions.like("phone", "%" + criteria.getPhone() + "%"));
        }
        if (!StringUtils.isNullOrWhiteSpace(criteria.getUserName())) {
            criterion.add(Restrictions.like("userName", "%" + criteria.getUserName() + "%"));
        }

        int totalCount = ((Long) criterion.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criterion.setProjection(null);
        criterion.setFirstResult((criteria.getPageNumber() - 1) * criteria.getPageSize()).setMaxResults(criteria.getPageSize());
        return new PagedResult<>(criterion.list(), criteria.getPageNumber(), criteria.getPageSize(), totalCount);
    }
}
