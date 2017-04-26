package com.sunesoft.seera.yc.core.parameter.domain.infrastructure.hibernate;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateRepository;
import com.sunesoft.seera.yc.core.parameter.domain.ParameterType;
import com.sunesoft.seera.yc.core.parameter.domain.ParameterTypeRepository;
import com.sunesoft.seera.yc.core.parameter.domain.ParameterTypeRepository;
import org.springframework.stereotype.Service;

/**
 * Created by zy on 2016/6/2.
 */
@Service("parameterTypeRepositoryImpl")
public class ParameterTypeRepositoryImpl extends GenericHibernateRepository<ParameterType,Long> implements ParameterTypeRepository {


}
