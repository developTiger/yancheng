package com.sunesoft.seera.yc.webapp.function;

import com.sunesoft.seera.yc.core.manager.application.IManagerService;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.ResourceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouz on 2016/7/14.
 */
@Component
public class ResouceFactory {


    @Autowired
    IManagerService managerService;

    private static Map<Long, List<ResourceDto>> factory = new HashMap<>();


    public void clearSource(){
        factory = new HashMap<>();
    }

    public Map<Long, List<ResourceDto>> getResourceMap() {
        if (factory.size() == 0) {
            factory = managerService.getAllAuthInfoByRole();
        }
        return factory;
    }


    public List<ResourceDto> getResourceById(Long magerId) {
        return this.getResourceMap().get(magerId);


    }
}
