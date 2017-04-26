package com.sunesoft.seera.yc.webapp.controller.managerCoupon;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.utils.Factory;
import com.sunesoft.seera.fr.utils.JsonHelper;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.fr.utils.excel.ExpotExcel;
import com.sunesoft.seera.yc.core.coupon.application.ICouponService;
import com.sunesoft.seera.yc.core.coupon.application.dto.CouponDto;
import com.sunesoft.seera.yc.core.coupon.domain.criteria.CouponCriteria;
import com.sunesoft.seera.yc.core.manager.application.IManagerService;
import com.sunesoft.seera.yc.core.manager.application.criteria.ManagerCriteria;
import com.sunesoft.seera.yc.core.manager.application.dtos.InnerManagerDto;
import com.sunesoft.seera.yc.webapp.controller.Layout;
import com.sunesoft.seera.yc.webapp.utils.AjaxResponse;
import com.sunesoft.seera.yc.webapp.utils.URI;
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
 * Created by xiazl on 2016/9/8.
 */
@Controller
public class ManagerCouponController extends Layout {
    @Autowired
    ICouponService service;
    @Autowired
    IManagerService managerService;

    //优惠券领取清单
    @RequestMapping(value = "sra_m_c")
    public ModelAndView managerCouponIndex(Model model) {
        return view(layout, "managerCoupon/managerCouponIndex", model);
    }

    @RequestMapping(value = "ajax_managerCoupon_query_list")
    public void queryType(HttpServletResponse response, CouponCriteria criteria, HttpServletRequest request) {
        if (!StringUtils.isNullOrWhiteSpace(criteria.getStaffRealName()))
            criteria.setStaffRealName(URI.deURI(criteria.getStaffRealName()));
        PagedResult pagedResult = service.findManagerCoupons(criteria);
        String json = JsonHelper.toJson(pagedResult);
        AjaxResponse.write(response, json);
    }

    @RequestMapping(value = "_addOrUpManagerCoupon")
    public ModelAndView addOrUpManagerCouponType(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (!StringUtils.isNullOrWhiteSpace(id)) {
            CouponDto dto = service.getById(Long.parseLong(id));
            model.addAttribute("beans", dto);
        }
        //未禁用的管理员
        ManagerCriteria managerCriteria = new ManagerCriteria();
        managerCriteria.setStatus(true);
        List<InnerManagerDto> staffs = managerService.findUser(managerCriteria).getItems();
        model.addAttribute("staffs", staffs);
        CouponCriteria criteria = new CouponCriteria();
        criteria.setPageSize(10);
        PagedResult<CouponDto> pagedResult = service.findManagerCoupons(criteria);
        model.addAttribute("managerCoupons", pagedResult.getItems());
        return view("/managerCoupon/_addOrUpManagerCoupon", model);
    }

    @RequestMapping(value = "ajax_add_update_managerCoupon", method = RequestMethod.POST)
    public void ajax_add_update_Managercoupon(CouponDto dto, HttpServletResponse response, HttpServletRequest request) throws Exception {
        CommonResult result = null;
        if (dto.getId() == null) {
            result = service.addCoupon(dto);
        } else {
            result = service.updateCoupon(dto);
        }
        String json = JsonHelper.toJson(result);
        AjaxResponse.write(response, json);
    }

    @RequestMapping(value = "ajax_delete_managerCoupon")
    public void ajax_delete_managerCoupon(HttpServletResponse response, HttpServletRequest request) {
        String id = request.getParameter("id");
        CommonResult result = ResultFactory.commonError("请传入优惠券的id");
        if (!StringUtils.isNullOrWhiteSpace(id))
            result = service.deleteById(Long.parseLong(id));
        String json = JsonHelper.toJson(result);
        AjaxResponse.write(response, json);

    }

    //*****************************EXCEL的导出*******************
    //excel 导出
    @RequestMapping(value = "ajax_sra_m_c_down")
    public void download(CouponCriteria criteria, Model model, HttpServletRequest request, HttpServletResponse response) {


        if (!StringUtils.isNullOrWhiteSpace(criteria.getStaffRealName()))
            criteria.setStaffRealName(URI.deURI(criteria.getStaffRealName()));
        criteria.setPageSize(99999999);
        PagedResult<CouponDto> pagedResult = service.findManagerCoupons(criteria);
        List<ManagerCouponExcel> list = new ArrayList<>();
        if (pagedResult != null && pagedResult.getItems() != null && pagedResult.getItems().size() > 0) {
            list = Factory.convert(pagedResult.getItems(), ManagerCouponExcel.class);
        }
        ExpotExcel<ManagerCouponExcel> expotExcel = new ExpotExcel<>();
        String[] header = new String[]{"编号", "金额（元）", "最低消费（元）", "状态", "截止日期", "关联的员工"};
        expotExcel.doExportExcel("部门考核表", header, list, "yyyy-MM-dd", response);
    }
}
