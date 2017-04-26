package com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate;

import com.sunesoft.seera.fr.results.PagedResult;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>基于 Hibernate 实现的 Query 框架中的查询器实现的基类，
 * 继承者可以通过该基类来简化仓库的实现代码。
 * </p>
 * <p><b>注意：目前的实现要求当前上下文中已经启用了事务管理。</b>
 * </p>
 *
 * @author zhouzh
 * @since 0.1
 */
public class GenericHibernateFinder {

    @Autowired
    protected SessionFactory sessionFactory;

    /**
     * <p>获取与当前仓库关联的 {@code Session} 对象。
     * </p>
     * <p><b>注意：目前的实现要求当前上下文中已经启用了事务管理。</b>
     * </p>
     *
     * @return 返回当前的 {@code Session} 对象。
     */
    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 创建一个基于 HQL 语句的查询对象。
     *
     * @param hql    HQL 查询语句。
     * @param params HQL 查询语句使用的参数。
     * @return 返回创建的查询对象。
     */
    protected Query createQuery(String hql, Object... params) {
        Query q = getSession().createQuery(hql);
        int idx = 0;
        for (Object obj : params) {
            q.setParameter(idx++, obj);
        }
        return q;
    }

    /**
     * 创建一个基于 HQL 语句的查询对象。
     *
     * @param hql    HQL 查询语句。
     * @param params HQL 查询语句使用的命名参数。
     * @return 返回创建的查询对象。
     */
    protected Query createQuery(String hql, Map<String, Object> params) {
        Query q = getSession().createQuery(hql);
        for (String key : params.keySet()) {
            q.setParameter(key, params.get(key));
        }
        return q;
    }
    public <E> PagedResult<E> pagingBySql(int pageIndex, int pageSize, Class<E> clazz, String sql, Map<String, Object> params) {
        int totalCount = getCountBySql(sql, params);
        SQLQuery query = getSession().createSQLQuery(sql);
        setParams(query, params);
        query.setFirstResult((pageIndex - 1) * pageSize);
        query.setMaxResults(pageSize);
        List<E> results = query.list();
        return new PagedResult<E>( results,pageIndex, pageSize, totalCount);
    }

    public int getCountBySql(String sql, Map<String, Object> params) {
        String countSql = sql.substring(0, sql.toLowerCase().indexOf("from "));
        if (null == countSql || countSql.trim().equals("")) {
            countSql = "select count(*) " + sql;
        } else {
            countSql = sql.replace(countSql, "select count(*) ");
        }
        if (countSql.toLowerCase().contains(" order ")) {
            countSql = countSql.substring(0, countSql.toLowerCase().indexOf("order"));
        }
        SQLQuery query = getSession().createSQLQuery(countSql);
        setParams(query, params);
        return Integer.parseInt(query.uniqueResult().toString()) ;
    }

    protected <E> List<E> queryForObjects(Class<E> clazz, String sql, Map<String, Object> params) {
        SQLQuery sqlQuery = getSession().createSQLQuery(sql);
        setParams(sqlQuery, params);
        addScalar(sqlQuery, clazz);
        sqlQuery.setResultTransformer(Transformers.aliasToBean(clazz));
        return sqlQuery.list();
    }

    private void setParams(SQLQuery query, Map<String, Object> params) {
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (param.getValue() instanceof Collection)
                    query.setParameterList(param.getKey(), (Collection) param.getValue());
                else
                    query.setParameter(param.getKey(), param.getValue());
            }
        }
    }


    public <E> void addScalar(SQLQuery sqlQuery, Class<E> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if ((field.getType() == long.class) || (field.getType() == Long.class)) {
                sqlQuery.addScalar(field.getName(), new LongType());
            } else if ((field.getType() == int.class) || (field.getType() == Integer.class)) {
                sqlQuery.addScalar(field.getName(), new IntegerType());
            } else if ((field.getType() == char.class) || (field.getType() == Character.class)) {
                sqlQuery.addScalar(field.getName(), new CharacterType());
            } else if ((field.getType() == short.class) || (field.getType() == Short.class)) {
                sqlQuery.addScalar(field.getName(), new ShortType());
            } else if ((field.getType() == double.class) || (field.getType() == Double.class)) {
                sqlQuery.addScalar(field.getName(), new DoubleType());
            } else if ((field.getType() == float.class) || (field.getType() == Float.class)) {
                sqlQuery.addScalar(field.getName(), new FloatType());
            } else if ((field.getType() == boolean.class) || (field.getType() == Boolean.class)) {
                sqlQuery.addScalar(field.getName(), new BooleanType());
            } else if (field.getType() == String.class) {
                sqlQuery.addScalar(field.getName(), new StringType());
            } else if (field.getType() == Date.class) {
                sqlQuery.addScalar(field.getName(), new TimestampType());
            }
        }
    }
}
