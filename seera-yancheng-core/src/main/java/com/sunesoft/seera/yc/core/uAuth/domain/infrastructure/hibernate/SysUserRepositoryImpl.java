package com.sunesoft.seera.yc.core.uAuth.domain.infrastructure.hibernate;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateRepository;
import com.sunesoft.seera.yc.core.uAuth.domain.SysUser;
import com.sunesoft.seera.yc.core.uAuth.domain.SysUserRepository;
import com.sunesoft.seera.yc.core.uAuth.domain.SysUserRepository;
import org.springframework.stereotype.Service;

/**
 * Created by zhouz on 2016/5/12.
 */
@Service("sysUserRepository")
public class SysUserRepositoryImpl extends GenericHibernateRepository<SysUser, Long> implements SysUserRepository {
}
