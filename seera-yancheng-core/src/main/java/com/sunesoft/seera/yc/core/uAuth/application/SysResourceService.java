package com.sunesoft.seera.yc.core.uAuth.application;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.uAuth.application.criteria.ResourceCriteria;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.ResourceDto;
import com.sunesoft.seera.yc.core.uAuth.domain.SysResource;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.ResourceDto;
import com.sunesoft.seera.yc.core.uAuth.domain.SysResource;

import java.util.List;

/**
 * Created by zhouz on 2016/5/25.
 */
public interface SysResourceService {
    /**
     * 新增菜单
     * @param menu
     * @return
     */
    public Long addMenu(ResourceDto menu);

    /**
     *
     * @param menu
     * @return
     */
    public Boolean save(ResourceDto menu);

    /**
     *
     * @param id
     * @return
     */
    public SysResource getByKey(Long id);
    /**
     * 删除菜单
     * @param id id列表
     * @return Boolean
     */
    public Boolean deleteMenu(Long[] id);


    public Long addOrUodate(ResourceDto dto);

    /**
     *
     * @param id
     * @param menuUrl
     * @param menuName
     * @return
     */
    public Boolean updateResource(Long id, String menuUrl, String menuName);


    public Boolean updateResource(Long id, String menuUrl, String menuName, int sort);
    /**
     *
     * @return
     */
    public List<ResourceDto> getResources( String menuName);

    /**
     *
     * @param criteria
     * @return
     */
    public PagedResult<ResourceDto> getResourcePaged(ResourceCriteria criteria);

    /**
     *
     * @param criteria
     * @return
     */
    public List<ResourceDto> getResourceList(ResourceCriteria criteria);



}
