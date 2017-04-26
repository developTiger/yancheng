package com.sunesoft.seera.yc.core.uAuth.domain;

import java.util.List;

/**
 * Created by zhouz on 2016/5/12.
 */
public interface SysRoleRepository {


    Long save(SysRole role);

    void delete(Long roleId);

    SysRole get(Long inventorId);

    List<SysRole> getAllRoleName();

}
