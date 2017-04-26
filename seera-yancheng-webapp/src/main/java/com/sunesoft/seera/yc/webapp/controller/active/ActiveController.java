package com.sunesoft.seera.yc.webapp.controller.active;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.activity.application.IActivityService;
import com.sunesoft.seera.yc.core.activity.application.dtos.ActivityDto;
import com.sunesoft.seera.yc.core.activity.domain.ActivityStatus;
import com.sunesoft.seera.yc.core.activity.domain.creteria.ActivityCriteria;
import com.sunesoft.seera.yc.webapp.controller.Layout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zwork on 2016/7/28.
 */
@Controller
@RequestMapping(value = "/active")
public class ActiveController extends Layout {

    @Autowired
    private IActivityService activityService;


    @RequestMapping(value = "ActivityDetails/{id}")
    public ModelAndView Activity_Details(Model model,@PathVariable("id") Long id) {

        List<ActivityDto> activityDto=activityService.getActivityByProductId(id);
        model.addAttribute("activities",activityDto.get(0));
        return view(activeLayout,"active/acticityDetailsIndex", model);
    }

    @RequestMapping(value="ajax_query_activity",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult ajax_query_activity(HttpServletRequest request){
        String id=request.getParameter("id");
        ActivityDto activityDto=activityService.getById(Long.parseLong(id));
        return new CommonResult(true,activityDto.getPageProfile());
    }


    @RequestMapping(value = "/{type}")
    public ModelAndView indexView(@PathVariable("type") String type,Model model, HttpServletRequest request) {
        if(!type.equals("passed")&&!type.equals("activing")){
            type="welcome";
        }
        model.addAttribute("type",type);
        if(type.equals("welcome")){
            model=getActiving(model);
            model=getPassed(model);
            return view(activeLayout, "active/welcome", model);
        }else{
            if(type.equals("activing")){

                ActivityCriteria activityCriteria=new ActivityCriteria();
                activityCriteria.setActivityStatus(ActivityStatus.Run);
                if(!StringUtils.isNullOrWhiteSpace(request.getParameter("pageNumber"))){
                    activityCriteria.setPageNumber(Integer.parseInt(request.getParameter("pageNumber")));
                }
                PagedResult<ActivityDto> pagedResult=activityService.findPage(activityCriteria);
                model.addAttribute("pagedResult",pagedResult);
            }else if(type.equals("passed")){
                ActivityCriteria activityCriteria=new ActivityCriteria();
                activityCriteria.setActivityStatus(ActivityStatus.Complete);
                if(!StringUtils.isNullOrWhiteSpace(request.getParameter("pageNumber"))){
                    activityCriteria.setPageNumber(Integer.parseInt(request.getParameter("pageNumber")));
                }
                PagedResult<ActivityDto> pagedResult=activityService.findPage(activityCriteria);
                model.addAttribute("pagedResult",pagedResult);
            }
            return view(activeLayout, "active/index", model);
        }
    }

    public Model getActiving(Model model){
        //正在进行中的活动
        ActivityCriteria activityCriteria=new ActivityCriteria();
        activityCriteria.setActivityStatus(ActivityStatus.Run);
        activityCriteria.setPageSize(3);
        PagedResult<ActivityDto> pagedResult=activityService.findPage(activityCriteria);
        List<ActivityDto> activings=pagedResult.getItems();
        model.addAttribute("activtings",activings);
        return model;
    }
    public Model getPassed(Model model){
        //已经结束的活动
        ActivityCriteria activityCriteria=new ActivityCriteria();
        activityCriteria.setActivityStatus(ActivityStatus.Complete);
        activityCriteria.setPageSize(3);
        PagedResult<ActivityDto> pagedResult=activityService.findPage(activityCriteria);
        List<ActivityDto> posetActive=pagedResult.getItems();
            model.addAttribute("postActives",posetActive);
        return model;
    }



}
