package com.sunesoft.seera.yc.core.uAuth.domain.infrastructure.hibernate;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateRepository;
import com.sunesoft.seera.yc.core.uAuth.domain.SysResource;
import com.sunesoft.seera.yc.core.uAuth.domain.SysResourceRepository;
import com.sunesoft.seera.yc.core.uAuth.domain.SysResource;
import org.springframework.stereotype.Service;

/**
 * Created by zhouz on 2016/5/12.
 */
@Service("sysResourceRepository")
public class SysResourceRepositoryImpl extends GenericHibernateRepository<SysResource,Long> implements SysResourceRepository {

}
