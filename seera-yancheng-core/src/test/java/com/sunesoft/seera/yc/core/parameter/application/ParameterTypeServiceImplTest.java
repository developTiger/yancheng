package com.sunesoft.seera.yc.core.parameter.application;

import com.sunesoft.seera.yc.core.parameter.domain.ParameterType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by user on 2016/6/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ParameterTypeServiceImplTest {

    @Autowired
    ParameterTypeService service;
    @Test
    public void testAddParameterType() throws Exception {
        ParameterType parameterType =new ParameterType();
      //  Parameter parameter =new Parameter();

        parameterType.setParamTypeName("小明");
        parameterType.setParamDesc("身高是1米78");
      //  parameterType.setParameters();
        System.out.println("?????????????????????????????????"+parameterType);
    }
}