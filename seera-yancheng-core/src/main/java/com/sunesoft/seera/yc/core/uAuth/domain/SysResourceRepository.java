package com.sunesoft.seera.yc.core.uAuth.domain;

/**
 * Created by zhouz on 2016/5/12.
 */
public interface SysResourceRepository {


    Long save(SysResource emp);

    void delete(Long empId);

    SysResource get(Long inventorId);

}
