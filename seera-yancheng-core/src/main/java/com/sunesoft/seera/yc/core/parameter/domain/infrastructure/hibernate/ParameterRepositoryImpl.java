package com.sunesoft.seera.yc.core.parameter.domain.infrastructure.hibernate;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateRepository;
import com.sunesoft.seera.yc.core.parameter.domain.Parameter;
import com.sunesoft.seera.yc.core.parameter.domain.ParameterRepository;
import com.sunesoft.seera.yc.core.parameter.domain.ParameterRepository;
import org.springframework.stereotype.Service;

/**
 * Created by user on 2016/6/2.
 */

@Service("parameterTypeRepository")
public class ParameterRepositoryImpl  extends GenericHibernateRepository<Parameter,Long> implements ParameterRepository {
}
