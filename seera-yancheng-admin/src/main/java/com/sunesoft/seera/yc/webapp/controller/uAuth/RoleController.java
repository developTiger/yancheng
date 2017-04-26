package com.sunesoft.seera.yc.webapp.controller.uAuth;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.JsonHelper;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.uAuth.application.SysPermissionGroupService;
import com.sunesoft.seera.yc.core.uAuth.application.SysRoleService;
import com.sunesoft.seera.yc.core.uAuth.application.criteria.RoleCriteria;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.PermissionGroupDto;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.RoleDto;
import com.sunesoft.seera.yc.webapp.controller.Layout;
import com.sunesoft.seera.yc.webapp.function.ResouceFactory;
import com.sunesoft.seera.yc.webapp.utils.AjaxResponse;
import com.sunesoft.seera.yc.webapp.utils.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouz on 2016/5/19.
 */
@Controller
public class RoleController extends Layout {

    @Autowired
    SysPermissionGroupService sysPermissionGroupService;
    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    ResouceFactory resouceFactory;


    @RequestMapping(value="_deleteRoleForm")
    public void deleteRoleForm(Model model, HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("id");
        List<Long> listId = new ArrayList<>();
        String[]  roleIds = id.split(",");
        for(String d : roleIds){
            listId.add(Long.parseLong(d));
        }
        if(listId.size() == 0){
            AjaxResponse.write(response,"text","请选择要操作的角色");
        }
        else{
            System.out.print("删除要选择的数据");
            Boolean result = sysRoleService.deleteRole(listId.toArray(new Long[listId.size()]));
            resouceFactory.clearSource();
            AjaxResponse.write(response,"text",true?"success":"error");
        }
//
//        model.addAttribute("idTemp", id);
//        return view("uAuth/_deleteRoleForm", model);
    }

    @RequestMapping(value="_addRoleForm")
    public ModelAndView addMenuForm(Model model, HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        if (!StringUtils.isNullOrWhiteSpace(id)) {
            RoleDto roleDto = sysRoleService.getRoleById(Long.parseLong(id));


            List<PermissionGroupDto> list1 = roleDto.getPrivilegeGroupListDtos();
            List<String> list = new ArrayList<>();
            if (list1 != null) {
                for (int i = 0; i < list1.size(); i++) {
                    Long groupId = list1.get(i).getId();

                    list.add(groupId.toString());
                }
//            int size = list.size();


//            String[] arr = (String[])list.toArray(new String[size]);//使用了第二种接口，返回值和参数均为结果
                model.addAttribute("permissionGroupName", list);
            }
            model.addAttribute("bean", roleDto);
        }
        List<PermissionGroupDto> list=sysPermissionGroupService.getPermissionGroup("");
        model.addAttribute("permGroup",list);
        return view("uAuth/_addRoleForm", model);
    }

    @RequestMapping(value = "sra_role")
    public ModelAndView roleInfos(Model model) {
        return view(layout, "uAuth/queryRole", model);
    }

    @RequestMapping(value = "ajax_role_query_list")
    public void roleQuery_pagelist(RoleCriteria criteria, HttpServletRequest request, HttpServletResponse response) {
        String roleName = URI.deURI(request.getParameter("roleName"));

        criteria.setRoleName(roleName);

        PagedResult PagedResult = sysRoleService.getRolePage(criteria);
        String json = JsonHelper.toJson(PagedResult);
        System.out.println(json);
        AjaxResponse.write(response, json);
    }
    @RequestMapping(value="ajax_deleteMenu", method = RequestMethod.POST)
    public void ajax_deleteMenu(HttpServletRequest request,HttpServletResponse response){
        String ids = request.getParameter("ids");
        List<Long> listId = new ArrayList<>();
        if(ids!=null && !"".equals(ids)) {
            String[] stringDs = ids.split(",");
            for (String id : stringDs) {
                listId.add(Long.parseLong(id));
            }
        }
        resouceFactory.clearSource();
        if (listId.size() == 0) {
            AjaxResponse.write(response, "text", "请选择要操作的用户");
        } else {
            Boolean result = sysRoleService.deleteRole((Long[])listId.toArray(new Long[listId.size()]));
            AjaxResponse.write(response, "text", result ? "success" : "error");
        }
    }
    @RequestMapping(value = "ajax_add_update_role", method = RequestMethod.POST)
    public void addOrUpdateUser(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String roleName = request.getParameter("roleName");
        String idCode=request.getParameter("idCode");
        String description=request.getParameter("description");
        String pid = request.getParameter("pid");
        String [] pids = pid.split(",");
        List<PermissionGroupDto> list=new ArrayList<>();

        for(String s :pids) {
            if(!StringUtils.isNullOrWhiteSpace(s)) {
                PermissionGroupDto permissionGroupDto=new PermissionGroupDto();
                permissionGroupDto.setId(Long.parseLong(s));
                permissionGroupDto.setSort(1);
                list.add(permissionGroupDto);
            }
        }
        RoleDto roleDto=new RoleDto();
        roleDto.setName(roleName);
        roleDto.setIdCode(idCode);
        roleDto.setSort(1D);
        roleDto.setDescription(description);
        roleDto.setPrivilegeGroupListDtos(list);
        if (!StringUtils.isNullOrWhiteSpace(id)) {
            roleDto.setId(Long.parseLong(id));
        }
        String result = sysRoleService.addOrUpdate(roleDto) > 0 ? "success" : "error";
        System.out.println(result);
        resouceFactory.clearSource();
        AjaxResponse.write(response, "text", result);
    }

    @RequestMapping(value = "ajax_delete_roleManager")
    @ResponseBody
    public String deleteRoleManager(HttpServletRequest request){
        String id = request.getParameter("id");

        List<Long> role = new ArrayList<>();
        role.add(Long.parseLong(id));
        resouceFactory.clearSource();
        sysRoleService.deleteRole((Long[])role.toArray(new Long[role.size()]));
        return "success";
    }
}
