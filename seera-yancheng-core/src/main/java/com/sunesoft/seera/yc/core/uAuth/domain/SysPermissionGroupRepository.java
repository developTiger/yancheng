package com.sunesoft.seera.yc.core.uAuth.domain;

import java.util.List;

/**
 * Created by zhouz on 2016/5/12.
 */
public interface SysPermissionGroupRepository {


    Long save(SysPermissionGroup emp);

    void delete(Long empId);

    SysPermissionGroup get(Long inventorId);

    List<SysPermissionGroup> getAllName();



}
