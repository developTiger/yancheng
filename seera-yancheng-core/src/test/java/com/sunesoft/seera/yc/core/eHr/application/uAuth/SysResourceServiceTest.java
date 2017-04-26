package com.sunesoft.seera.yc.core.eHr.application.uAuth;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.JsonHelper;
import com.sunesoft.seera.yc.core.uAuth.application.SysResourceService;
import com.sunesoft.seera.yc.core.uAuth.application.criteria.ResourceCriteria;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.ResourceDto;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.ResourceDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangkefan on 2016/5/27.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class SysResourceServiceTest {

    @Autowired
    SysResourceService resourceService;

    @Test
    public void getResources() throws Exception {
        List<ResourceDto> dtoList = new ArrayList<ResourceDto>();
        dtoList = resourceService.getResources("");
        System.out.println(JsonHelper.toJson(dtoList));
    }

    @Test
    public void getResourcePaged() throws Exception {

        PagedResult<ResourceDto>  result=resourceService.getResourcePaged(new ResourceCriteria());

        System.out.println(result.getItems());

        System.out.println(JsonHelper.toJson(result));
    }
    @Test
    public void getResourceList() throws Exception{
        List<ResourceDto> l =  resourceService.getResourceList(new ResourceCriteria());
    }

}