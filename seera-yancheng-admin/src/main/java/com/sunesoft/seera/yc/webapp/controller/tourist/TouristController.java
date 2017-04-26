package com.sunesoft.seera.yc.webapp.controller.tourist;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.JsonHelper;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.tourist.application.ITouristService;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristDto;
import com.sunesoft.seera.yc.core.tourist.domain.TouristStatus;
import com.sunesoft.seera.yc.core.tourist.domain.criteria.TouristCriteria;
import com.sunesoft.seera.yc.webapp.controller.Layout;
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
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 游客用户
 * Created by liulin on 2016/7/16.
 */
@Controller
public class TouristController extends Layout {

    @Autowired
    ITouristService iTouristService;


    @RequestMapping(value = "sra_t_tourist")
    public ModelAndView index(Model model) {
        return view(layout, "tourist/touristIndex", model);
    }

    @RequestMapping(value = "ajax_tourist_query_list")
    public void queryTouristData(HttpServletResponse response, HttpServletRequest request, TouristCriteria touristCriteria) {

        String status = request.getParameter("statusB");
        if (status != null && (status.equals("1"))) {
            touristCriteria.setStatus(TouristStatus.Normal);
        }
        if ((status != null && status.equals("0"))) {
            touristCriteria.setStatus(TouristStatus.Forbidden);
        }

        String beginTime = request.getParameter("beginTime");
        String endTime = request.getParameter("endTime");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        touristCriteria.setFromRegisterDate(DateHelper.parse(beginTime, "yyyy-MM-dd"));
        touristCriteria.setEndRegisterDate(DateHelper.parse(endTime, "yyyy-MM-dd"));

        PagedResult pagedResult = iTouristService.findTourists(touristCriteria);
        String json = JsonHelper.toJson(pagedResult);
        AjaxResponse.write(response, json);
    }

    @RequestMapping(value = "_addTouristForm")
    public ModelAndView addTouristForm(Model model) {
        return view("tourist/_addTouristForm", model);
    }

    @RequestMapping(value = "_editTouristForm")
    public ModelAndView editTouristForm(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        UniqueResult<TouristDto> uniqueResult = iTouristService.getTourist(Long.parseLong(id));
        model.addAttribute("beans", uniqueResult.getT());
        return view("tourist/_editTouristForm", model);
    }

    @RequestMapping(value = "ajax_add_update_tourist", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addOrUpdateTourist(TouristDto touristDto, HttpServletRequest request) {
        Long id = touristDto.getId();
        CommonResult commonResult = null;
        String yearCarExpireDateStr = request.getParameter("yearCardExpireDateStr");
        if (null != id) {
            UniqueResult result = iTouristService.getTourist(id);
            TouristDto tourist = (TouristDto) result.getT();
            tourist.setEmail(touristDto.getEmail());
            tourist.setRealName(touristDto.getRealName());
            tourist.setMobilePhone(touristDto.getMobilePhone());
            tourist.setGender(touristDto.getGender());
            tourist.setIdCardNo(touristDto.getIdCardNo());
            if (!StringUtils.isNullOrWhiteSpace(touristDto.getYearCardInfo()))
                tourist.setYearCardInfo(touristDto.getYearCardInfo().trim());
            if (!StringUtils.isNullOrWhiteSpace(yearCarExpireDateStr))
                tourist.setYearCardExpireDate(DateHelper.parse(yearCarExpireDateStr, "yyyy-MM-dd hh:mm:ss"));
            commonResult = iTouristService.update(tourist);
        } else {
            if (!StringUtils.isNullOrWhiteSpace(yearCarExpireDateStr))
                touristDto.setYearCardExpireDate(DateHelper.parse(yearCarExpireDateStr, "yyyy-MM-dd hh:mm:ss"));
            commonResult = iTouristService.create(touristDto);
        }
        return commonResult;
    }


    @RequestMapping(value = "ajax_disabled_touristStatus")
    @ResponseBody
    public CommonResult disabledTouristStatus(HttpServletRequest request) {
        String id = request.getParameter("id");
        CommonResult commonResult = null;
        commonResult = iTouristService.setStatus(Long.parseLong(id), TouristStatus.Forbidden);
        return commonResult;
    }

    @RequestMapping(value = "ajax_enable_touristStatus")
    @ResponseBody
    public CommonResult enableTouristStatus(HttpServletRequest request) {
        String id = request.getParameter("id");
        CommonResult commonResult = null;
        commonResult = iTouristService.setStatus(Long.parseLong(id), TouristStatus.Normal);
        return commonResult;
    }

    @RequestMapping(value = "_resetTouristPassword")
    public ModelAndView resetPassword(Model model,HttpServletRequest request){
        model.addAttribute("id",request.getParameter("id"));
        return view("tourist/_resetPassword",model);
    }

    @RequestMapping(value = "ajax_resetPassword_user" ,method = RequestMethod.POST)
    @ResponseBody
    public CommonResult resetPassword(HttpServletRequest request,String password) {
        String id = request.getParameter("id");
        CommonResult commonResult = null;
        if (!StringUtils.isNullOrWhiteSpace(id)) {
            commonResult = iTouristService.restPassword(Long.parseLong(id), password);
        }
        return commonResult;
    }

}
