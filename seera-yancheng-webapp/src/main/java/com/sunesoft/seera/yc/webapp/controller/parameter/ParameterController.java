package com.sunesoft.seera.yc.webapp.controller.parameter;

import com.sunesoft.seera.yc.core.parameter.application.ParameterService;
import com.sunesoft.seera.yc.core.parameter.application.criteria.ParameterCriteria;
import com.sunesoft.seera.yc.webapp.controller.Layout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.sunesoft.seera.fr.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by MJ006 on 2016/6/1.
 */
@Controller
public class ParameterController extends Layout {
    @Autowired
    ParameterService parameterService;
    @RequestMapping(value = "sra_ptype")
    public ModelAndView menuIndex(Model modal, HttpServletRequest request, HttpServletResponse response) {
        return view(layout, "parameter/paramtype", modal);
    }
    @RequestMapping(value = "sra_ptypemanage")
    public ModelAndView manage(Model modal, HttpServletRequest request, HttpServletResponse response) {
        return view(layout, "parameter/ptypemanage", modal);
    }


    @RequestMapping(value="_deleteParameterForm")
    public ModelAndView deleteRoleForm(Model model, HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("id");
        model.addAttribute("idTemp", id);
        return view("parameter/_deleteParameterForm", model);
    }
    @RequestMapping(value="_addParameter")
    public ModelAndView addMenuForm(Model model, HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        if (!StringUtils.isNullOrWhiteSpace(id)) {

        }

        return view("parameter/_addParameterForm", model);
    }
    @RequestMapping("queryParameter")
    public void queryParameter(Model model, HttpServletRequest request, HttpServletResponse response, ParameterCriteria criteria,String name){

//       PagedResult<Parameter> list=new PagedResult<Parameter>(parameterService.getAllUser(criteria),1,10,1);
//        //PagedResult<ParameterDto> list=new PagedResult<ParameterDto>(parameterService.getAllparameter(),1,10,1);
//        String json= JsonHelper.toJson(list);
//        System.out.println(json);
//        AjaxResponse.write(response,json);
    }
}
