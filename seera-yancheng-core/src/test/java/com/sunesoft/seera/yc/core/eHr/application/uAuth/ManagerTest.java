package com.sunesoft.seera.yc.core.eHr.application.uAuth;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.manager.application.IManagerService;
import com.sunesoft.seera.yc.core.manager.application.criteria.ManagerCriteria;
import com.sunesoft.seera.yc.core.manager.application.dtos.InnerManagerDto;
import com.sunesoft.seera.yc.core.manager.application.dtos.ManagerSessionDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ManagerTest {

    @Autowired
    IManagerService managerService;
    @Test
    public void addUserTest() throws Exception {
        for(int i=10;i<30;i++) {
            InnerManagerDto dto = new InnerManagerDto();
            dto.setUserName("x"+i);
            dto.setPhone("12365478932");
            dto.setPassword("1");
            dto.setRealName("x"+i);
            CommonResult c=managerService.addUser(dto);
            if (c != null) {
                System.out.println(c.getId() + "\t" + c.getIsSuccess() + "\t" + c.getMsg());
            }
        }
    }
    @Test
    public void UpdateUserTest() {
        InnerManagerDto dto=new InnerManagerDto();
        dto.setId(8l);
        dto.setUserName("x8");
        dto.setPhone("88865478932");
        dto.setRealName("xx");
        CommonResult c=managerService.updateUser(dto);
        if(c!=null){
            System.out.println(c.getId()+"\t"+c.getIsSuccess()+"\t"+c.getMsg());
        }
    }
    @Test
    public void getAllUserTest() {
        List<InnerManagerDto> list=managerService.getAllUser("x1");
        if(list!=null)
        System.out.println(list.size());
    }

    @Test
    public void loginTest() {
//        ManagerSessionDto dto=managerService.l("x3","123456");
//        if(dto!=null)
//            System.out.println(dto.getUserName());
    }
    @Test
    public void setUserStatusTest() {
        List<Long> list=new ArrayList<>();
        list.add(3L);
        list.add(1L);
        CommonResult c=managerService.setUserStatus(list,false);
        if(c!=null){
            System.out.println(c.getId()+"\t"+c.getIsSuccess()+"\t"+c.getMsg());
        }
    }
    @Test
    public void FindUserTest() {
        ManagerCriteria criteria=new ManagerCriteria();
        criteria.setUserName("xx");
        PagedResult<InnerManagerDto> pg=managerService.findUser(criteria);
        if(pg!=null)
            System.out.println(pg.getItems().size()+"\t"+pg.getPagesCount()+"\t"+pg.getPageNumber()+"\t"+pg.getPageSize());

    }
    @Test
    public void deleteTest() {
        List<Long> list=new ArrayList<>();
        list.add(1L);
        CommonResult c=managerService.delete(list);
        if(c!=null){
            System.out.println(c.getId()+"\t"+c.getIsSuccess()+"\t"+c.getMsg());
        }
    }
    @Test
    public void getUserTest() {
        List<InnerManagerDto> list=managerService.getAllUser();
        if(list!=null)
            System.out.println(list.size());

    }
    @Test
    public void getByIdTest() {
        InnerManagerDto dto=managerService.getById(3L);
        if(dto!=null)
            System.out.println(dto.getId());
        if(dto==null)
            System.out.println("no");
    }
    @Test
    public void GetUserSessionDtoByIdTest() {

        ManagerSessionDto  dto=managerService.GetUserSessionDtoById(3l);
        if(dto!=null)
            System.out.println(dto.getId());
        if(dto==null)
            System.out.println("no");

    }

    @Test
    public void passwordTest() {
        CommonResult c=managerService.changePassword(1l,"change");
        if(c!=null){
            System.out.println(c.getId()+"\t"+c.getIsSuccess()+"\t"+c.getMsg());
        }
    }
}
