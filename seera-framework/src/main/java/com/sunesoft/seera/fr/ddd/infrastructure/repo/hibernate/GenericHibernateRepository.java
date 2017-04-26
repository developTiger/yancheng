package com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

import com.sunesoft.seera.fr.ddd.infrastructure.IRepository;
import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 基于 Hibernate 实现了仓库的基本操作方法：{@link #load(java.io.Serializable)}、
 * {@link #get(java.io.Serializable)}、 {@link #exists(java.io.Serializable)}、
 * {@link #save(Object)} 和 {@link #delete(java.io.Serializable)}， 继承者可以通过该基类来简化仓库的实现。
 * </p>
 * <p>
 * <b>注意：目前的实现要求当前上下文中已经启用了事务管理。</b>
 * </p>
 * 
 * @param <E>
 *            实体类型（DDD 的实体类型或聚合根类型）。
 * @param <K>
 *            实体标识的类型。
 * @author zhouzh
 * @since 0.1
 */
public class GenericHibernateRepository<E, K extends Serializable>
        implements IRepository<E, K>{

	@Autowired
	protected SessionFactory sessionFactory;
	private Class<E> clazz;
	private Session newSession;

	/**
	 * 初始化 {@link GenericHibernateRepository} 的新实例。
	 */
/*	@SuppressWarnings("unchecked")*/
	public GenericHibernateRepository() {
		this.clazz = ((Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}

	/**
	 * <p>
	 * 从仓库加载指定标识的实体对象。
	 * </p>
	 * <p>
	 * 该方法首先检查当前 {@code Session} 管理的一级缓存，如果存在指定标识的对象则返回它，
	 * 否则创建并返回实体类型的代理对象。后续访问代理对象的属性时，才会进一步检查由 {@code SessionFactory}
	 * 管理的全局共享二级缓存或者直接访问数据库。如果二级缓存命中了指定标识的实体数据，
	 * 则使用此数据填充代理对象，否则从数据库中直接获取指定标识的实体数据，此时如果数据库中也不存在，
	 * 则将会引发异常。该方法并不是始终返回代理对象的，只有当检查一级缓存且不存在指定标识的对象时， 才会返回实体代理对象，该特性为 Hibernate
	 * 的延迟加载技术。
	 * </p>
	 * <p>
	 * 通常情况下，建议使用该方法而非 {@link #get(java.io.Serializable)} 方法， 这样可以充分利用 Hibernate
	 * 提供的延迟加载特性，并提高复杂实体的加载性能。
	 * </p>
	 * <p>
	 * 如果需要从返回值检查指定标识的实体对象是否存在，则应该使用 {@link #get(java.io.Serializable)} 方法。
	 * </p>
	 * <p>
	 * <b>注意：{@code SessionFactory} 管理的全局共享二级缓存需要配置之后才能生效。</b>
	 * </p>
	 * 
	 * @param id
	 *            实体对象的标识。
	 * @return 返回仓库中存储的实体对象。
	 */
/*	@SuppressWarnings("unchecked")*/
    @Override
	public E load(K id) {
		return (E) getSession().load(clazz, id);
	}

	/**
	 * 使用特定的锁选项从仓库获取指定标识的实体对象。
	 * 
	 * @param id
	 *            实体对象的标识。
	 * @param lockOptions
	 *            锁选项。
	 * @return 返回仓库中存储的实体对象。
	 * @see #load(java.io.Serializable)
	 */
	@SuppressWarnings("unchecked")
	protected E load(K id, LockOptions lockOptions) {
		return (E) getSession().load(clazz, id, lockOptions);
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
	/**
	 * <p>
	 * 从仓库获取指定标识的实体对象。
	 * </p>
	 * <p>
	 * 该方法首先检查当前 {@code Session} 管理的一级缓存，如果存在指定标识的对象则返回它， 否则检查
	 * {@code SessionFactory} 管理的全局共享二级缓存，如果存在指定标识的对象则返回它，
	 * 否则直接访问数据库获取指定标识的实体数据，此时如果数据库中也不存在，则返回 null。
	 * 该方法并不是始终返回实体对象，如果在检查一级缓存时已经存在之前被 {@code load} 使用过，
	 * 或者被其它关联实体延迟加载过，则返回的还是代理对象，只不过已经加载了实体数据。
	 * </p>
	 * <p>
	 * 通常情况下，建议使用 {@link #load(java.io.Serializable)} 方法而非该方法， 这样可以充分利用 Hibernate
	 * 提供的延迟加载特性，并提高复杂实体的加载性能。
	 * </p>
	 * <p>
	 * 该方法的返回值可用于检查指定标识的实体对象是否存在，返回值为 null 则表明实体对象不存在。
	 * </p>
	 * <p>
	 * <b>注意：{@code SessionFactory} 管理的全局共享二级缓存需要配置之后才能生效。</b>
	 * </p>
	 * 
	 * @param id
	 *            实体对象的标识。
	 * @return 返回仓库中存储的实体对象，如果不存在则返回 null。
	 */
    @Override
	public E get(K id) {
		return (E) getSession().get(clazz, id);
	}

	/**
	 * 使用特定的锁选项从仓库获取指定标识的实体对象。
	 * 
	 * @param id
	 *            实体对象的标识。
	 * @param lockOptions
	 *            锁选项。
	 * @return 返回仓库中存储的实体对象，如果不存在则返回 null。
	 */
	@SuppressWarnings("unchecked")
    	protected E get(K id, LockOptions lockOptions) {
		return (E) getSession().get(clazz, id, lockOptions);
	}

	/**
	 * 检查指定标识的实体对象是否存在。该方法将忽略 Hibernate 的缓存机制，直接访问数据库进行检查。
	 * 
	 * @param id
	 *            实体对象的标识。
	 * @return 实体对象存在返回 true，否则返回 false。
	 */
    @Override
	public boolean exists(K id) {
		LockOptions lockOptions = new LockOptions(LockMode.READ);
		return get(id, lockOptions) != null;
	}

	/**
	 * 从仓库删除指定标识的实体对象。
	 * 
	 * @param id
	 *            实体对象的标识。
	 */
    @Override
	public void delete(K id) {
		Object entity = get(id);
		if (entity != null)
			getSession().delete(entity);
	}

	/**
	 * <p>
	 * 将实体对象保存（添加或更新）到仓库。
	 * </p>
	 * <p>
	 * 该方法对于瞬时状态或删除状态的实体对象执行 insert 操作， 对于持久状态或脱管状态的实体对象执行 update
	 * 操作，操作完成后实体对象均转为持久状态。
	 * </p>
	 * 
	 * @param entity
	 *            实体对象。
	 * @return 返回实体对象的标识。
	 */
    @Override
	public K save(E entity) {
		getSession().saveOrUpdate(entity);
		getSession().flush();
        
		return (K) getSession().getIdentifier(entity);
	}

    /**
	 * <p>
	 * 获取与当前仓库关联的 {@code Session} 对象。
	 * </p>
	 * <p>
	 * <b>注意：目前的实现要求当前上下文中已经启用了事务管理。</b>
	 * </p>
	 * 
	 * @return 返回当前的 {@code Session} 对象。
	 */
	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 提交事务。
	 */
	protected void commitTransaction() {
		Transaction transaction = getSession().getTransaction();
		if (transaction != null && transaction.isActive()) {
			transaction.commit();
		}
	}

	protected void commitAndReOpenTransaction() {
		Transaction transaction = getSession().getTransaction();
		if (transaction != null && transaction.isActive()) {
			transaction.commit();

		}
	}

}
