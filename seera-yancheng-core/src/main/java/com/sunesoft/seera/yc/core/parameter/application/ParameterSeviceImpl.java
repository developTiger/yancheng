package com.sunesoft.seera.yc.core.parameter.application;


import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateFinder;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.parameter.application.criteria.ParameterCriteria;
import com.sunesoft.seera.yc.core.parameter.application.dtos.ParameterDto;
import com.sunesoft.seera.yc.core.parameter.domain.Parameter;
import com.sunesoft.seera.yc.core.parameter.domain.ParameterRepository;
import com.sunesoft.seera.yc.core.parameter.domain.ParameterType;
import com.sunesoft.seera.yc.core.parameter.application.criteria.ParameterCriteria;
import com.sunesoft.seera.yc.core.parameter.application.dtos.ParameterDto;
import com.sunesoft.seera.yc.core.parameter.domain.ParameterRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zy on 2016/6/2.
 */
@Service("parametersevice")
public class ParameterSeviceImpl  extends GenericHibernateFinder implements ParameterService {

    @Autowired
    ParameterRepository parameterRepository;

    /**
     * 添加数据
     * @param dto
     * @return
     */
    @Override
    public Long addParameter(ParameterDto dto) {
        return parameterRepository.save(convertFromDto(dto));
    }

    /**
     * 群删
     * @param parameterId
     * @return
     */
    @Override
    public boolean deleteParameter(Long[] parameterId) {
        Criteria criterion = getSession().createCriteria(ParameterType.class);
        if (parameterId != null && parameterId.length > 0) {
            criterion.add(Restrictions.in("id", parameterId));
        }
        List<Parameter> list = criterion.list();
        for (Parameter parameter : list) {
            parameter.setIsActive(false);
            parameter.setLastUpdateTime(new Date());
            parameterRepository.save(parameter);
        }
        return true;
    }
    /**
     * 更新数据
     * @param dto
     * @return
     */    @Override
    public Long updateParameter(ParameterDto dto) {
        Parameter parameter=parameterRepository.get(dto.getId());
        if(parameter==null) {

            return parameterRepository.save(convertFromDto(dto));
        }
        else
        {
            if(dto.getParamName()!=null){
                parameter.setParamName(parameter.getParamName());
            }
            if(dto.getParamDesc()!=null) {
                parameter.setParamDesc(parameter.getParamDesc());
            }
            return 1L;
        }
    }
    /**
     * 获得所有数据
     * isActive为true
     * @return
     */
    @Override
    public List<ParameterDto> getAllparameter() {
        Criteria criterion = getSession().createCriteria(Parameter.class);
        criterion.add(Restrictions.eq("isActive",true));
        List<Parameter> list1 = criterion.list();
        List<ParameterDto> list = new ArrayList<ParameterDto>();
        for (Parameter p : list1) {
            list.add(convertToDto(p));
        }
        return list;
    }

    /**
     * 获取数据
     * @param id
     * @return
     */
    @Override
    public ParameterDto getById(Long id) {
        return convertToDto(parameterRepository.get(id));
    }

    @Override
    public PagedResult<ParameterDto> FindParam(ParameterCriteria criteria) {
        Criteria criterion = getSession().createCriteria(Parameter.class);
        criterion.add(Restrictions.eq("isActive",true));
        if (!StringUtils.isNullOrWhiteSpace(criteria.getParamName())) {
            criterion.add(Restrictions.like("param_name", "%" + criteria.getParamName() + "%"));
        }
        //获取总记录数
        int totalCount = ((Long) criterion.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criterion.setProjection(null);
        criterion.setFirstResult((criteria.getPageNumber() - 1) * criteria.getPageSize()).setMaxResults(criteria.getPageSize());

        List<ParameterDto> parameterListDto = new ArrayList<ParameterDto>();
        List<Parameter> parameterList = criterion.list();
        for (Parameter param : parameterList ) {
            if(param.getIsActive()!=null && param.getIsActive()!=false){
                parameterListDto.add(convertToDto(param));
            }
        }
        return new PagedResult<ParameterDto>(parameterListDto, criteria.getPageNumber(), criteria.getPageSize(), totalCount);
    }

    @Override
    public List<ParameterDto> getAllParameterType(String parameterName) {
        Criteria criterion = getSession().createCriteria(Parameter.class);
        criterion.add(Restrictions.eq("isActive",true));
        if(!StringUtils.isNullOrWhiteSpace(parameterName)){
            criterion.add(Restrictions.like("parameterName", "%" +parameterName + "%"));
        }
        List<Parameter> list1 = criterion.list();
        List<ParameterDto> list = new ArrayList<ParameterDto>();
        for (Parameter paramter : list1) {
            list.add(convertToDto(paramter));
        }
        return list;
    }

    //调用方法1 dto转实体
    private Parameter convertFromDto(ParameterDto dto) {
        Parameter parameter = new Parameter();
        if (dto.getId() != null && dto.getId() > 0) {
            parameter = parameterRepository.get(dto.getId());
        }
        if(dto.getParamTypeId() != null && dto.getParamTypeId() > 0) {
            ParameterType par = new ParameterType();   //转换
            par.setId(dto.getParamTypeId());
            par.setParamTypeName(dto.getParamTypeName());

            parameter.setParameterType(par);
        }
            parameter.setParamName(dto.getParamName());
            parameter.setParamDesc(dto.getParamDesc());
            parameter.setRemark(dto.getRemark());
        return parameter;
    }

    //调用方法2 实体转dto
    private ParameterDto convertToDto(Parameter param) {
        ParameterDto dto = new ParameterDto();
        if (dto.getId() != null && dto.getId() > 0) {
            dto.setId(param.getId());
        }
        dto.setParamName(param.getParamName());
        dto.setParamDesc(param.getParamDesc());
        dto.setRemark(param.getRemark());

        ParameterType parameterType=param.getParameterType();
       // dto.getParamTypeId(parameterType.);
        dto.setParamTypeName(parameterType.getParamTypeName());
        return dto;
    }
}
