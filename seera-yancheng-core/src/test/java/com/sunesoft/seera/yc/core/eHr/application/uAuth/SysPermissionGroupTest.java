package com.sunesoft.seera.yc.core.eHr.application.uAuth;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.JsonHelper;
import com.sunesoft.seera.yc.core.uAuth.application.SysPermissionGroupService;
import com.sunesoft.seera.yc.core.uAuth.application.criteria.PermissionGroupCriteria;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.PermissionGroupDto;
import com.sunesoft.seera.yc.core.uAuth.domain.SysPermissionGroup;
import com.sunesoft.seera.yc.core.uAuth.domain.SysResourceRepository;
import com.sunesoft.seera.yc.core.uAuth.application.SysPermissionGroupService;
import com.sunesoft.seera.yc.core.uAuth.application.criteria.PermissionGroupCriteria;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.PermissionGroupDto;
import com.sunesoft.seera.yc.core.uAuth.domain.SysPermissionGroup;
import com.sunesoft.seera.yc.core.uAuth.domain.SysResourceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jiangkefan on 2016/5/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class SysPermissionGroupTest {
    @Autowired
    SysPermissionGroupService sysPermissionGroupService;
    @Autowired
    SysResourceRepository sysResourceRepository;

    @Test
    public void testPermissionGroupTest_add(){
        SysPermissionGroup s = new SysPermissionGroup("jkf",2);
        s.setLastUpdateTime(new java.util.Date());
        sysPermissionGroupService.add(s);
        System.out.print(s.getName());
    }
    @Test
    public void testPermissionGroupTest_del(){
        Long l[] = {1l,3l,7l};
        sysPermissionGroupService.delete(l);
    }
    @Test
    public void testPermissionGroupTest_findById(){
         SysPermissionGroup sysPermissionGroup = sysPermissionGroupService.getById(1l);
    }
    @Test
    public void testPermissionGroupTest_update(){
         PermissionGroupDto p = new PermissionGroupDto();
         p.setId(1l);

         p.setName("li");
         boolean b = sysPermissionGroupService.update(p);
    }
    @Test
    public void testPermissionGroupTest_addOrUpdate(){
        PermissionGroupDto p = new PermissionGroupDto();
        p.setId(1l);
        p.setName("wang");
        sysPermissionGroupService.addOrUpdate(p);
    }
    @Test
    public void testPermissionGroupTest_addOrUpdates(){
        PermissionGroupDto p = new PermissionGroupDto();
        p.setSort(1);
        p.setName("jkf");
        List<Long> listsId = new ArrayList<Long>();
        //模拟菜单id
        listsId.add(1L);
        listsId.add(10L);

        p.setMenuIds(listsId);
        Long l = sysPermissionGroupService.addOrUpdate(p);
        System.out.println(l);
    }

    @Test
    public void TestPermissionGroup_getPermissionGroupPaged(){
        PermissionGroupCriteria p = new PermissionGroupCriteria();
        p.setPermissionGroupName("");
        PagedResult  result = sysPermissionGroupService.getPermissionGroupPaged(p);
        System.out.println(result.getPageNumber());
        System.out.println(result.getPageSize());
        System.out.println(result.getItems().size());
    }
    @Test
    public void TestPermissionGroup_getPermissionGroup(){
        List<PermissionGroupDto> doList = new ArrayList<PermissionGroupDto>();
        doList = sysPermissionGroupService.getPermissionGroup("");
        System.out.print(JsonHelper.toJson(doList));
    }
    @Test
    public void TestPermissionGroup_getAll(){
        List<PermissionGroupDto> l =  sysPermissionGroupService.getAllPermissionGroupNames();
    }

}
