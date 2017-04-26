package com.sunesoft.seera.yc.core.eHr.application.uAuth;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.uAuth.application.SysPermissionGroupService;
import com.sunesoft.seera.yc.core.uAuth.application.SysRoleService;

import com.sunesoft.seera.yc.core.uAuth.application.criteria.RoleCriteria;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.PermissionGroupDto;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.RoleDto;
import com.sunesoft.seera.yc.core.uAuth.domain.SysPermissionGroup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;


/**
* Created by xiazl on 2016/5/26.
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class SysRoleTest {

    @Autowired
    SysRoleService roleService;

    @Autowired
    SysPermissionGroupService permissionGroupService;

    @Test
    public void roleServiceAddTest() {

        RoleDto dto = new RoleDto();
        dto.setName("x-3-x");
        dto.setIdCode("xx");
        dto.setDescription("xx");
        dto.setSort(12.0);
        List<PermissionGroupDto> list=new ArrayList<PermissionGroupDto>();
        SysPermissionGroup p=permissionGroupService.getById(1L);

        PermissionGroupDto pd=new PermissionGroupDto();
        pd.setName(p.getName());
        list.add(pd);

        dto.setPrivilegeGroupListDtos(list);
        Long l = -1L;
        l = roleService.addRole(dto);
        System.out.println("ServiceAddRole()测试:" + l);

    }

    @Test
    public void roleServiceDeleteTest() {
        Long[] ids = new Long[]{63L,64L};
        Boolean  b=roleService.deleteRole(ids);
            System.out.println("ServiceDeleteRole()测试:" +b.booleanValue());
    }



    @Test
    public void roleServiceUpdatessTest() {
        Long l = -1L;
        RoleDto r=roleService.getRoleById(64L);
        SysPermissionGroup p=permissionGroupService.getById(3L);
        PermissionGroupDto pd=new PermissionGroupDto();
        pd.setId(p.getId());
        pd.setName(p.getName());

        List<PermissionGroupDto> list=new ArrayList<PermissionGroupDto>();
        list.add(pd);
       r.setPrivilegeGroupListDtos(list);
        l = roleService.updateRole(r);
        System.out.println("ServiceUpdateRoless()测试:" + l);
    }

    @Test
    public void roleServiceGetByIdTest() {
        Long l = 67L;
        RoleDto dto= roleService.getRoleById(l);
        System.out.println("ServiceGetById()测试:" + dto.getId());
    }

    @Test
    public void roleServiceSaveTest() {
        RoleDto dto = new RoleDto();
        dto.setId(62L);
        dto.setName("d-d");
        dto.setIdCode("d-d");
        dto.setSort(2.2);
        dto.setIdCode("2.2");
        Long l = -1L;
        l = roleService.save(dto);
        System.out.println("ServiceSave()测试:" + l);
    }

    @Test
    public void roleServiceAddOrUpdateTest() {
        RoleDto roleDto = new RoleDto();

        roleDto.setName("shexle");
        roleDto.setIdCode("kei");
        roleDto.setSort(1.01);
        roleDto.setOperate(1);
        roleDto.setDescription("mma");
        roleDto.setPrivilegeGroupListDtos(null);

        Long l = -1L;
        l = roleService.addOrUpdate(roleDto);
        System.out.println("roleServiceAddOrUpdate()测试:" + l);
    }

    @Test
    public void roleServiceGetAllRoletest() {
        List<RoleDto> list = roleService.getAllRole();
        for (RoleDto dto : list) {
            System.out.println("roleServiceGetAllRole()测试:" +dto.getId());
        }
    }

    @Test
    public void roleServiceGetRolePageTest() {
        RoleCriteria rCriteria = new RoleCriteria();
//        rCriteria.setRoleName("x");
        PagedResult<RoleDto> pResult = roleService.getRolePage(rCriteria);
        System.out.println("roleServiceGetRolePage()测试:" +pResult.getPageSize()+"\t"+pResult.getPageNumber()+"\t"+pResult.getPagesCount());
    }

    @Test
    public void userServiceByNamesTest(){

        List<RoleDto> list=roleService.getAllRole("x");
        System.out.println(list.size());
        for(RoleDto dto:list){
            System.out.println(dto.getId()+"\t"+dto.getName());
        }
    }


}
