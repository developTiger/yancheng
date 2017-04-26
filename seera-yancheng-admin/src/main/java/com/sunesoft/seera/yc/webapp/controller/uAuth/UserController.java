package com.sunesoft.seera.yc.webapp.controller.uAuth;

import com.sunesoft.seera.fr.loggers.Logger;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.uAuth.application.SysRoleService;
import com.sunesoft.seera.yc.core.uAuth.application.SysUserService;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.RoleDto;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.UserDto;
import com.sunesoft.seera.yc.webapp.controller.Layout;
import com.sunesoft.seera.yc.webapp.function.UserSession;
import com.sunesoft.seera.yc.webapp.utils.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouz on 2016/5/19.
 */

@Controller
public class UserController extends Layout {

    @Autowired
    Logger logger;

    @Autowired
    SysRoleService sysRoleService;



    @Autowired
    SysUserService userService;

    @Autowired
    UserSession us;

    @RequestMapping(value = "sra_demo")
    public ModelAndView demo(Model model,HttpServletRequest request,HttpServletResponse response) {

        return view(layout, "uAuth/test", model);
    }

    @RequestMapping(value="sra_u_userInfo")
    public ModelAndView userInfo(Model model,HttpServletRequest request,HttpServletResponse response){
        String id=request.getParameter("id");
        if(id==null){
            id="";
        }
        model.addAttribute("empId", id);
        return view(layout,"/uAuth/userInfo",model);
    }

    @RequestMapping(value = "_addUserForm")
    public ModelAndView addUserForm(Model model,HttpServletRequest request,HttpServletResponse response){
        String id=request.getParameter("id");
        if(!StringUtils.isNullOrWhiteSpace(id)){
            UserDto sysUser=userService.getById(Long.parseLong(id));
            model.addAttribute("sysUser",sysUser);
        }
        List<RoleDto> list= sysRoleService.getAllRole();
        model.addAttribute("beansRole", list);
        return view("/uAuth/_addUserForm",model);
    }



    @RequestMapping(value = "ajax_setUserStstus", method = RequestMethod.POST)
    public void setUserStstus(HttpServletRequest request, HttpServletResponse response) {
        Boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));
        String ids = request.getParameter("ids");
        List<Long> listid = new ArrayList<>();
        String[] pids = ids.split(",");
        for (String id : pids) {
            listid.add(Long.parseLong(id));
        }
        if (listid.size() == 0) {
            AjaxResponse.write(response, "text", "请选择要操作的用户");
        } else {
////            Boolean result = userService.setUserStatus(listid, isActive?0:1);
//            AjaxResponse.write(response, "text", result ? "success" : "error");
        }
    }

    @RequestMapping(value = "ajax_add_update_user", method = RequestMethod.POST)
    public void addOrUpdateUser(HttpServletRequest request, HttpServletResponse response) {
         String userName = request.getParameter("loginName");
        String realName = request.getParameter("realName");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String id = request.getParameter("id");
        String RoleId=request.getParameter("roleName");
        String sort=request.getParameter("sort");
        List<RoleDto> list=new ArrayList<>();
        RoleDto roleDto=new RoleDto();
        if(RoleId==null || "".equals(RoleId))
            RoleId="1";
        roleDto.setId(Long.parseLong(RoleId));
        roleDto.setSort(1D);
        list.add(roleDto);
        UserDto user = new UserDto();
        user.setName(realName);
        user.setLoginName(userName);
        user.setMobile(phone);
        user.setEmail(email);
        user.setUserRoleList(list);
        if (!StringUtils.isNullOrWhiteSpace(id)) {
            user.setId(Long.parseLong(id));
        }
//        String result = userService.addOrUpdate(user) > 0 ? "success" : "error";
//        System.out.println(result);
//        AjaxResponse.write(response, "text", result);
    }

}
