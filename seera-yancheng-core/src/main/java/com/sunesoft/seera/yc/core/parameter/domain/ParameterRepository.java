package com.sunesoft.seera.yc.core.parameter.domain;

/**
 * Created by user on 2016/6/2.
 */
public interface ParameterRepository {

    Long save(Parameter parameter);

    void delete(Long  parameterId);

    Parameter get(Long parameterId);

    // List<Parameter> getAllParameter();
}
