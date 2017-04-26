package com.sunesoft.seera.yc.core.lottery.application;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateFinder;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.yc.core.lottery.application.dtos.SubjectDto;
import com.sunesoft.seera.yc.core.lottery.application.lotteryFactory.SubjectFactory;
import com.sunesoft.seera.yc.core.lottery.domain.Subject;
import com.sunesoft.seera.yc.core.lottery.domain.SubjectRepository;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kkk on 2017/1/9.
 */
@Service("subjectService")
public class SubjectServiceImpl extends GenericHibernateFinder implements SubjectService {

    @Autowired
    SubjectRepository repository;


    @Override
    public CommonResult create(SubjectDto dto) {
        Subject subject = SubjectFactory.convert(dto);
        return ResultFactory.commonSuccess(repository.save(subject));
    }

    @Override
    public CommonResult remove(List<Long> ids) {
        Criteria criterion = getSession().createCriteria(Subject.class);
        criterion.add(Restrictions.eq("isActive", true));
        criterion.add(Restrictions.in("id", ids));
        List<Subject> list = criterion.list();
        if (list != null && list.size() > 0) {
//            list.stream().forEach(i->repository.delete(i.getId()));
            list.stream().forEach(i -> {
                i.setIsActive(false);
                repository.save(i);
            });
            return ResultFactory.commonSuccess();
        }
        return ResultFactory.commonError("请选择题目项");
    }

    @Override
    public CommonResult edit(SubjectDto dto) {
        return null;
    }

    @Override
    public SubjectDto getById(Long id) {
        Subject subject=repository.get(id);
        return SubjectFactory.convert(subject);
    }

    @Override
    public List<SubjectDto> getAll() {
        Criteria criterion = getSession().createCriteria(Subject.class);
        criterion.add(Restrictions.eq("isActive", true));
        List<Subject> list = criterion.list();
        if (list != null && list.size() > 0)
            return SubjectFactory.convert(list);
        return null;
    }

    @Override
    public List<SubjectDto> getByType(Integer type) {
        Criteria criterion = getSession().createCriteria(Subject.class);
        criterion.add(Restrictions.eq("isActive", true));
//        criterion.add(Restrictions.eq("type",type));
//        if(type==0) {
//            criterion.add(Restrictions.eq("status", true));
//        }
        criterion.addOrder(Order.asc("id"));
        List<Subject> list = criterion.list();
        if (list != null && list.size() > 0)
            return SubjectFactory.convert(list);
        return null;
    }



    @Override
    public CommonResult updateStatus(Long typeId,Long userId) {
        String hql = "update subject set status=0,answerUserId="+userId+" where id="+typeId;
        Query query = this.getSession().createSQLQuery(hql);
        Integer temp=query.executeUpdate();
        if(temp>0){
            return new CommonResult(true);
        }else{
            return new CommonResult(false,"更新失败");
        }
    }

    @Override
    public Boolean checkStatus(long typeId) {
        Subject subject=repository.get(typeId);
        if(subject.getStatus()){
            return true;
        }
        return false;
    }

    @Override
    public void updateReceive(long typeId) {
        Subject subject=repository.get(typeId);
        subject.setReceive(false);
        repository.save(subject);
    }
}
