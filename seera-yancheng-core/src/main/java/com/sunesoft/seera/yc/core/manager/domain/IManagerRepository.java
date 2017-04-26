package com.sunesoft.seera.yc.core.manager.domain;

import com.sunesoft.seera.fr.ddd.infrastructure.IRepository;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.manager.application.criteria.ManagerCriteria;

/**
 * 员工仓储接口
 * Created by zhaowy on 2016/7/11.
 */
public interface IManagerRepository extends IRepository<InnerManager,Long> {
    /**
     * 保存，或者修改，或者增加
     * @param emp
     * @return
     */
    Long save(InnerManager emp);

    /**
     * 删除
     * @param id
     */
    void delete(Long id);

    /**
     * 获取
     * @param inventorId
     * @return
     */
    InnerManager get(Long inventorId);

    /**
     * 条件查询管理员
     * @param criteria
     * @return
     */
    PagedResult<InnerManager> findManagers(ManagerCriteria criteria);

}
