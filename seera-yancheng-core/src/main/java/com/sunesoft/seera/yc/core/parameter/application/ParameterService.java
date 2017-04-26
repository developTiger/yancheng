package com.sunesoft.seera.yc.core.parameter.application;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.parameter.application.criteria.ParameterCriteria;
import com.sunesoft.seera.yc.core.parameter.application.dtos.ParameterDto;
import com.sunesoft.seera.yc.core.parameter.application.criteria.ParameterCriteria;
import com.sunesoft.seera.yc.core.parameter.application.dtos.ParameterDto;

import java.util.List;

/**
 * Created by zy on 2016/6/2.
 */
public interface ParameterService {



    /**
     * 增加数据
     * @param dto
     * @return
     */
    public Long addParameter(ParameterDto dto);

    /**
     * 根据id删除数据
     * @param parameterId
     * @return
     */
    public boolean deleteParameter(Long[] parameterId);

    /**
     * 修改数据
     * @param dto
     * @return
     */
    public Long updateParameter(ParameterDto dto);

    /**
     * 获取所用数据
     * @return
     */
    public List<ParameterDto> getAllparameter();

    /**
     * 根据id获取数据
     * @param id
     * @return
     */
    public ParameterDto getById(Long id);

    /**
     * 分页查询
     * @param criteria 查询条件
     * @return
     */
    public PagedResult<ParameterDto> FindParam(ParameterCriteria criteria);

    /**
     * 根据name'获取数据
     * @param parameterName
     * @return
     */
    List<ParameterDto> getAllParameterType(String parameterName);


}
