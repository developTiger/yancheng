package com.sunesoft.seera.yc.core.tourist.domain.infrastructure.hibernate;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateRepository;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.fr.utils.MD5;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.tourist.domain.ITouristRepository;
import com.sunesoft.seera.yc.core.tourist.domain.Tourist;
import com.sunesoft.seera.yc.core.tourist.domain.TouristStatus;
import com.sunesoft.seera.yc.core.tourist.domain.criteria.TouristCriteria;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhaowy on 2016/7/11.
 */
@Service("iTouristRepository")
public class TouristRepositoryImpl
        extends GenericHibernateRepository<Tourist, Long> implements ITouristRepository {
    /**
     * 校验游客Token与密码匹配
     *
     * @param openId userName|mobilePhone|email
     * @return
     */
    @Override
    public Tourist checkOpenId(String openId) {
        Criteria criterion = getSession().createCriteria(Tourist.class);
        criterion.add(Restrictions.eq("openid", openId));
        return null != criterion.uniqueResult() ? (Tourist) criterion.uniqueResult() : null;
    }

    @Override
    public UniqueResult<Tourist> login(String token, String pwd) {
        Criteria criterion = getSession().createCriteria(Tourist.class);
        criterion.add(Restrictions.eq("password",  MD5.GetMD5Code(pwd)));
        criterion.add(Restrictions.eq("status", TouristStatus.Normal));

        Criterion hsUserName = Restrictions.eq("userName", token);
        Criterion hsMobile = Restrictions.eq("openid", token);

        LogicalExpression tokenHs = Restrictions.or(hsUserName, hsMobile);
        criterion.add(tokenHs);

        if(criterion.uniqueResult()==null){
            return new UniqueResult<Tourist>("用户名或密码错误！");
        }

        return  new UniqueResult<Tourist>((Tourist)criterion.uniqueResult());
    }

    /**
     * 校验游客Token存在性
     *
     * @param token userName|mobilePhone|email
     * @return
     */
    @Override
    public Tourist check(String token) {
        if (StringUtils.isNullOrWhiteSpace(token)) return null;
        Criteria criterion = getSession().createCriteria(Tourist.class);
        //criterion.add(Restrictions.not(Restrictions.eq("userName","")));
        //criterion.add(Restrictions.eq("userName",token));

        Criterion hsUserName = Restrictions.eq("userName", token);
        Criterion hsMobile = Restrictions.eq("mobilePhone", token);
        Criterion hsEmail = Restrictions.eq("email", token);

        LogicalExpression tokenHs = Restrictions.or(hsUserName, hsMobile);
        tokenHs = Restrictions.or(tokenHs, hsEmail);

        criterion.add(tokenHs);
        List<Tourist> list=criterion.list();
        if(list!=null&&list.size()>0){
            return list.get(0);
        }

        return null;
    }

    /**
     * 查询游客信息
     *
     * @param criteria
     * @return 游客信息集合
     */
    @Override
    public PagedResult<Tourist> findTourists(TouristCriteria criteria) {
        Criteria criterion = getSession().createCriteria(Tourist.class);
        //region token param
        if (!StringUtils.isNullOrWhiteSpace(criteria.getToken())) {
            Criterion hsUserName = Restrictions.like("userName", "%" + criteria.getToken() + "%");
            Criterion hsMobile = Restrictions.like("mobilePhone", "%" + criteria.getToken() + "%");
            Criterion hsWxName = Restrictions.like("wxName", "%" + criteria.getToken() + "%");
            Criterion hsEmail = Restrictions.like("email", "%" + criteria.getToken() + "%");
            Criterion hsRealName = Restrictions.like("realName", "%" + criteria.getToken() + "%");
            LogicalExpression tokenHs = Restrictions.or(hsUserName, hsMobile);
            tokenHs = Restrictions.or(tokenHs, hsUserName);
            tokenHs = Restrictions.or(tokenHs, hsMobile);
            tokenHs = Restrictions.or(tokenHs, hsWxName);
            tokenHs = Restrictions.or(tokenHs, hsEmail);
            tokenHs = Restrictions.or(tokenHs, hsRealName);
            criterion.add(tokenHs);
        }
        //endregion

        //region other param
        if (null != criteria.getStatus())
            criterion.add(Restrictions.eq("status", criteria.getStatus()));

        if (null != criteria.getFromRegisterDate()) {
            criterion.add(Restrictions.ge("createDateTime", criteria.getFromRegisterDate()));
        }
        if (null != criteria.getEndRegisterDate()) {
            criterion.add(Restrictions.le("createDateTime", criteria.getEndRegisterDate()));
        }
        //endregion

        int totalCount = ((Long) criterion.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criterion.setProjection(null);
        criterion.addOrder(
                criteria.isAscOrDesc() ?
                        Order.asc(criteria.getOrderByProperty() == null ? "id" : criteria.getOrderByProperty())
                        : Order.desc(criteria.getOrderByProperty() == null ? "id" : criteria.getOrderByProperty()));
        criterion.setFirstResult(
                (criteria.getPageNumber() - 1) * criteria.getPageSize()).setMaxResults(criteria.getPageSize());
        return new PagedResult<>(criterion.list(), criteria.getPageNumber(), criteria.getPageSize(), totalCount);

    }


    @Override
    public List<Tourist> findTouristsByPhone(List<String> phones) {
        if (phones==null ||phones.size()==0) return null;
        Criteria criterion = getSession().createCriteria(Tourist.class);
        //criterion.add(Restrictions.not(Restrictions.eq("userName","")));
        //criterion.add(Restrictions.eq("userName",token));

        criterion.add( Restrictions.eq("isActive", true));
        criterion.add( Restrictions.in("mobilePhone", phones));
        List<Tourist> list=criterion.list();

        return list;
    }
}
