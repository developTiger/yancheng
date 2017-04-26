package com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate;

import com.sunesoft.seera.fr.ddd.BaseEntity;

/**
 * <p>基于 Hibernate 实现并优化的针对 DDD 框架中的聚合根实体的仓库实现的基类，
 * 继承者可以通过该基类来简化仓库的实现代码。
 * </p>
 * <p>通常情况下，建议使用该类型作为 DDD 聚合根实体仓库实现的基类。对于要求软删除实体数据的场景，
 * 聚合根实体仓库实现必须从该类型继承。
 * </p>
 * <p><b>注意：目前的实现要求当前上下文中已经启用了事务管理。</b>
 * </p>
 *
 * @param <E> 实体类型（DDD 的实体类型或聚合根类型）。
 * @author zhouzh
 * @since 0.1
 */
public class GenericHibernateRepositoryForBaseEntity<E extends BaseEntity> extends GenericHibernateRepository<E, Long> {

    @Override
    public void delete(Long id) {
        E entity = load(id);
        entity.markAsRemoved();
        save(entity);
    }

    @Override
    public Long save(E entity) {
        Long beforeSaveId = entity.getId();
        getSession().saveOrUpdate(entity);
        Long afterSaveId = entity.getId();
        if (beforeSaveId != null && afterSaveId != null && beforeSaveId.equals(afterSaveId)) {
            getSession().flush();
        }

        return (Long) getSession().getIdentifier(entity);
    }
}
