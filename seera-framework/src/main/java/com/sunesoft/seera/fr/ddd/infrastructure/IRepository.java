package com.sunesoft.seera.fr.ddd.infrastructure;

import java.io.Serializable;

/**
 * Created by zhaowy on 2016/7/12.
 */
public interface IRepository<E, K extends Serializable> {

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
    public E load(K id);

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
    @SuppressWarnings("unchecked")
    public E get(K id);


    /**
     * 检查指定标识的实体对象是否存在。该方法将忽略 Hibernate 的缓存机制，直接访问数据库进行检查。
     *
     * @param id
     *            实体对象的标识。
     * @return 实体对象存在返回 true，否则返回 false。
     */
    public boolean exists(K id);

    /**
     * 从仓库删除指定标识的实体对象。
     *
     * @param id
     *            实体对象的标识。
     */
    public void delete(K id) ;

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
    @SuppressWarnings("unchecked")
    public K save(E entity);

}
