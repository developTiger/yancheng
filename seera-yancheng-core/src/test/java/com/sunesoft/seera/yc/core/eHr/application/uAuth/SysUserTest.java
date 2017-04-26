package com.sunesoft.seera.yc.core.eHr.application.uAuth;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.uAuth.application.SysRoleService;
import com.sunesoft.seera.yc.core.uAuth.application.SysUserService;
import com.sunesoft.seera.yc.core.uAuth.application.criteria.UserCriteria;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.RoleDto;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.UserDto;
import com.sunesoft.seera.yc.core.uAuth.domain.SysUser;
import com.sunesoft.seera.yc.core.uAuth.application.SysRoleService;
import com.sunesoft.seera.yc.core.uAuth.application.SysUserService;
import com.sunesoft.seera.yc.core.uAuth.application.criteria.UserCriteria;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.RoleDto;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.UserDto;
import com.sunesoft.seera.yc.core.uAuth.domain.SysUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouz on 2016/5/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class SysUserTest {
    @Autowired
    SysUserService userService;
    @Autowired
    SysRoleService sysRoleService;

    @Test
    public void addUserTest() {
        UserDto dto = new UserDto();
        dto.setLoginName("x1");
        dto.setName("x1");
        dto.setEmail("ede@qq.com");
        dto.setPhone("666");
        System.out.println( userService.addorUpdateUser(dto));

    }

    @Test
    public void userQueryTest() {
        UserCriteria criteria = new UserCriteria();
        criteria.setLoginName("kkk");

        PagedResult result = userService.FindUser(criteria);
        System.out.println("这个长度为：" + result.getItems().size());
    }

    @Test
    public void userServiceChangePasswordTest() {
        boolean b = false;
//        b = userService.changePassword(92L, "kkk");
        System.out.println("运行是否成功‘false’or‘true’：" + b);
    }



    @Test
    public void userServiceSetUserStatusesTest() {
        boolean b = false;
        List<Long> ids = new ArrayList<Long>();
        ids.add(96L);
        ids.add(97L);
//        b = userService.setUserStatus(ids,1);
        System.out.println("statuses是否设置成功：" + b);
    }

    @Test
    public void userServiceAddOrUpdateTest() {
        UserDto dto = new UserDto();

        dto.setLoginName("aaa");
        dto.setName("ddlxdx");
        Long l = -1L;
//        l = userService.addOrUpdate(dto);
        System.out.println("addOrUpdate()测试" + l);
    }

    @Test
    public void userServiceDeleteTest() {
        Long[] ids = {98L, 99L};
        boolean b = false;
//        b = userService.delete(ids);
        System.out.println("serviceDelete()测试" + b);
    }

    @Test
    public void userServiceUpdateTest() {
        boolean b = false;
        UserDto u =userService.getById(95L);
        RoleDto r=sysRoleService.getRoleById(63L);
        List<RoleDto> list=new ArrayList<RoleDto>();
        list.add(r);

        u.setUserRoleList(list);
//        b = userService.updateUser(u);
        System.out.println("serviceUpdate()测试" + b);
    }

    @Test
    public void userServiceGetAllUserTest() {
        List<UserDto> list = userService.getAllUser();
        for (UserDto dto : list) {
            System.out.println("serviceGetAllUser()测试:" + dto.getId());
        }
    }

    @Test
    public void userServiceGetByIdTest() {
        UserDto dto = userService.getById(100L);
        if (dto != null) {
            System.out.println("serviceGetById()测试:" + dto.getId());
        }
    }

    @Test
    public void userServiceSaveTest() {
        UserDto dto = new UserDto();

        dto.setName("tt");
        dto.setPassword("tt");

        dto.setEmail("tt");
        dto.setLoginName("tt");

        Long l = -1L;
//        l = userService.save(dto);
        System.out.println("serviceSave()测试:" + l);

    }

    @Test
    public void userServiceByNamesTest() {

        List<UserDto> list = userService.getAllUser("k");
        System.out.println(list.size());
        for (UserDto dto : list) {
            System.out.println(dto.getName());
        }
    }

    @Test
    public void userServiceLogin(){
//        SysUser user=userService.wxoauth("kkk","kkk");
//        if(user!=null) {
//            System.out.println(user.getId()+"\t"+user.getLoginCount());
//        }else {
//            System.out.println("空值");
//        }
    }


}
