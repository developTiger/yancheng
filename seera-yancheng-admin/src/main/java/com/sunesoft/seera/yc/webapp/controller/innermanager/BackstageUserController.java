package com.sunesoft.seera.yc.webapp.controller.innermanager;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.utils.JsonHelper;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.manager.application.IManagerService;
import com.sunesoft.seera.yc.core.manager.application.criteria.ManagerCriteria;
import com.sunesoft.seera.yc.core.manager.application.dtos.InnerManagerDto;
import com.sunesoft.seera.yc.core.uAuth.application.SysRoleService;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.RoleDto;
import com.sunesoft.seera.yc.webapp.controller.Layout;
import com.sunesoft.seera.yc.webapp.function.ResouceFactory;
import com.sunesoft.seera.yc.webapp.function.UserSession;
import com.sunesoft.seera.yc.webapp.utils.AjaxResponse;
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
 * Created by admin on 2016/7/20.
 */
@Controller
public class BackstageUserController extends Layout {

    @Autowired
    IManagerService iManagerService;

    @Autowired
    SysRoleService roleService;

    @Autowired
    ResouceFactory resouceFactory;

    @Autowired
    UserSession us;

    @RequestMapping(value = "sra_t_b")
    public ModelAndView index(Model model){
        return view(layout,"innermanager/index",model);
    }

    @RequestMapping(value = "ajax_backstageUser_query_list")
    public void queryData(HttpServletResponse response,ManagerCriteria managerCriteria,HttpServletRequest request){

        String status = request.getParameter("statusB");
        if(status.equals("0")){
            managerCriteria.setStatus(false);
        }
        if(status.equals("1")){
            managerCriteria.setStatus(true);
        }

        PagedResult pagedResult = iManagerService.findUser(managerCriteria);
        if(pagedResult==null) pagedResult= new PagedResult(1,10);
        String json = JsonHelper.toJson(pagedResult);
        AjaxResponse.write(response,json);
    }

    @RequestMapping(value = "_addBackstageUserForm")
    public ModelAndView addBackstageForm(Model model){
        List<RoleDto> roles=roleService.getAllRole();
        model.addAttribute("roles",roles);
        return view("innermanager/_addBackstageUserForm",model);
    }

    @RequestMapping(value = "sra_changPassword")
    public ModelAndView sra_changPassword(Model model){

        return view(layout,"common/changePassword",model);
    }


    @RequestMapping(value = "_editBackstageUserForm")
    public ModelAndView editBackstageForm(Model model,HttpServletRequest request){
        String id = request.getParameter("id");
        if (!StringUtils.isNullOrWhiteSpace(id)){
            InnerManagerDto innerManagerDto=iManagerService.getById(Long.parseLong(id));
//        roleService.getRoleById(Long.parseLong(id));
            model.addAttribute("beans",innerManagerDto);
        }

        return view("innermanager/_editBackstageUserForm",model);
    }

    @RequestMapping(value = "ajax_add_update_backstageUser",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addOrUpdateBackstageUser(InnerManagerDto innerManagerDto,HttpServletResponse response,HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        CommonResult commonResult=null;
        if(!StringUtils.isNullOrWhiteSpace(id)){
            commonResult = iManagerService.updateUser(innerManagerDto);
        }else{
            try {
                commonResult=iManagerService.addUser(innerManagerDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return commonResult;
    }



    @RequestMapping(value = "ajax_disabled_backstageUserStatus")
    @ResponseBody
    public CommonResult disabledTouristStatus(HttpServletRequest request){
        String id = request.getParameter("id");
        List<Long> list = new ArrayList<>();
        list.add(Long.parseLong(id));
        CommonResult commonResult = null;
        commonResult = iManagerService.setUserStatus(list,false);
        return commonResult;
    }

    @RequestMapping(value = "ajax_enable_backstageUserStatus")
    @ResponseBody
    public CommonResult enableTouristStatus(HttpServletRequest request){
        String id = request.getParameter("id");
        List<Long> list = new ArrayList<>();
        list.add(Long.parseLong(id));
        CommonResult commonResult = null;
        commonResult = iManagerService.setUserStatus(list,true);
        return commonResult;
    }

    @RequestMapping(value = "_resetInnerManagerPassword")
    public ModelAndView resetPassword(Model model,HttpServletRequest request){
        model.addAttribute("id",request.getParameter("id"));
        return view("innermanager/_resetPassword",model);
    }


    @RequestMapping(value = "ajax_changePassword" ,method = RequestMethod.POST)
    @ResponseBody
    public CommonResult ajax_changePassword(HttpServletRequest request) {

        Long id = us.getCurrentUser(request).getId();
        String oldPwd= request.getParameter("oldPassword");
        String newPwd= request.getParameter("newPassword");
        CommonResult commonResult =iManagerService.checkPwd(id, oldPwd);

        if(commonResult.getIsSuccess()){
            commonResult= iManagerService.changePassword(id, newPwd);
        }
        return commonResult;
    }


    @RequestMapping(value = "ajax_resetPassword_backstageUser" ,method = RequestMethod.POST)
    @ResponseBody
    public CommonResult resetPassword(HttpServletRequest request,String password) {
        String id = request.getParameter("id");
        CommonResult commonResult = null;
        if (!StringUtils.isNullOrWhiteSpace(id)) {
            commonResult = iManagerService.changePassword(Long.parseLong(id), password);
        }
        return commonResult;
    }

    @RequestMapping(value = "ajax_delete_backstageUser")
    @ResponseBody
    public CommonResult deleteBackstageUser(HttpServletRequest request){
        String id = request.getParameter("id");
        String[] ids = id.split(",");
        List<Long> list = new ArrayList<>();
        CommonResult commonResult = null;
        for(int i=0;i<ids.length;i++){
            list.add(Long.parseLong(ids[i]));
        }
        if(!StringUtils.isNullOrWhiteSpace(id)){
            commonResult=iManagerService.delete(list);
        }
        return commonResult;
    }

    @RequestMapping(value = "ajax_deleteBackstageUsers")
    @ResponseBody
    public CommonResult deleteBackstageUsers(HttpServletRequest request){
        String id = request.getParameter("ids");
        String[] ids = id.split(",");
        List<Long> list = new ArrayList<>();
        CommonResult commonResult = null;
        for(int i=0;i<ids.length;i++){
            list.add(Long.parseLong(ids[i]));
        }
        if(!StringUtils.isNullOrWhiteSpace(id)){
            commonResult=iManagerService.delete(list);
        }
        return commonResult;
    }



    @RequestMapping(value = "_addUserRoleForm")
    public ModelAndView addOrUpdateManagerRoleForm(Model model,HttpServletRequest request){
        String id = request.getParameter("id");
        if (!StringUtils.isNullOrWhiteSpace(id)){
            InnerManagerDto innerManagerDto=iManagerService.getById(Long.parseLong(id));
            model.addAttribute("innerManager",innerManagerDto);
            if(innerManagerDto.getUserRoleListDto()!=null&&innerManagerDto.getUserRoleListDto().size()>0)
                model.addAttribute("roleId",innerManagerDto.getUserRoleListDto().get(0).getId());
            List<RoleDto> roles = roleService.getAllRole();
            model.addAttribute("roles",roles);
        }

        return view("innerManager/_addUserRoleForm",model);
    }

    @RequestMapping(value = "ajax_update_manager_role",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addOrUpdateManagerRole(HttpServletRequest request,HttpServletResponse response){

        String mangerIdStr = request.getParameter("id");
        String roleIdStr = request.getParameter("roleId");
        if(!StringUtils.isNullOrWhiteSpace(mangerIdStr) && !StringUtils.isNullOrWhiteSpace(roleIdStr))
        {
            Long managerId = Long.parseLong(mangerIdStr);
            Long roleId1 = Long.parseLong(roleIdStr);
            resouceFactory.clearSource();
            return iManagerService.SetManagerRole(managerId,roleId1);
        }
        return ResultFactory.commonError("参数错误");

    }

    @RequestMapping(value = "ajax_clearCache",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult ajax_clearCache(HttpServletRequest request,HttpServletResponse response){
        resouceFactory.clearSource();

        return ResultFactory.commonSuccess();

    }

}
