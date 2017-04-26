package com.sunesoft.seera.yc.core.lottery.domain.infrastructure.hibernate;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateRepository;

import com.sunesoft.seera.yc.core.lottery.domain.Subject;
import com.sunesoft.seera.yc.core.lottery.domain.SubjectRepository;
import org.springframework.stereotype.Service;

@Service("subjectRepository")
public class SubjectRepositoryImpl extends GenericHibernateRepository<Subject, Long> implements SubjectRepository {


}
