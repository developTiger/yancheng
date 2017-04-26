package com.sunesoft.seera.yc.core.parameter.domain;

/**
 * Created by zy on 2016/6/2.
 */
public interface ParameterTypeRepository {

    Long save(ParameterType parameterType);

    void delete(Long  parameterTypeId);

    ParameterType get(Long parameterTypeId);

   // List<Parameter> getAllParameter();
}
