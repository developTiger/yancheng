package com.sunesoft.seera.yc.core.parameter.application;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.parameter.application.criteria.ParameterCriteria;
import com.sunesoft.seera.yc.core.parameter.application.dtos.ParameterDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by xiazl on 2016/7/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ParameterServiceTest {

    @Autowired
    ParameterService service;
    @Test
    public void addParameterTest(){
        ParameterDto dto=new ParameterDto();
        dto.setAttrbute1("");
        dto.setParamName("x1");
        dto.setParamValue("1");
        dto.setRemark("ppp");
        Long l=service.addParameter(dto);
        Assert.notNull(l);
        System.out.println(l);

    }
    @Test
    public void deleteParameterTest(){
        Long[] l=new Long[]{1l,2l,3l};
       boolean b= service.deleteParameter(l);
        Assert.isTrue(b);

    }

    @Test
    public void updateParameterTest(){
        ParameterDto dto=new ParameterDto();
        dto.setId(1l);
        dto.setAttrbute1("");
        dto.setParamName("x1");
        dto.setParamValue("1");
        dto.setRemark("ppp");
        Long l=service.updateParameter(dto);
        if(l!=null)
            System.out.println(l);

    }

    @Test
    public void getAllparameterTest(){

        List<ParameterDto> list=service.getAllparameter();
        if(list!=null){
            System.out.println(list.size());
        }

    }

    @Test
    public void getByIdTest(){
        ParameterDto dto=service.getById(1l);
        if(dto!=null){
            System.out.println(dto.getId());
        }
    }

    @Test
    public void FindParamTest(){
        ParameterCriteria criteria=new ParameterCriteria();
        criteria.setParamName("");
        PagedResult<ParameterDto> pg=service.FindParam(criteria);
        if(pg!=null){
            System.out.println(pg.getItems().size()+"\t"+pg.getPageNumber()+"\t"+pg.getPageSize()+"\t"+pg.getPagesCount());
        }
    }

    @Test
    public void getAllParameterTypeTest(){

        List<ParameterDto> list=service.getAllParameterType("");
        if(list!=null){
            System.out.println(list.size());
        }
    }





}


