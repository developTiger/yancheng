package com.sunesoft.seera.yc.webapp.controller.coupon;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.JsonHelper;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.coupon.application.ICouponService;
import com.sunesoft.seera.yc.core.coupon.application.ICouponTypeService;
import com.sunesoft.seera.yc.core.coupon.application.dto.CouponDto;
import com.sunesoft.seera.yc.core.coupon.domain.Coupon;
import com.sunesoft.seera.yc.core.coupon.domain.CouponStatus;
import com.sunesoft.seera.yc.core.coupon.domain.CouponType;
import com.sunesoft.seera.yc.core.coupon.domain.criteria.CouponCriteria;
import com.sunesoft.seera.yc.core.manager.application.IManagerService;
import com.sunesoft.seera.yc.core.manager.application.criteria.ManagerCriteria;
import com.sunesoft.seera.yc.core.manager.application.dtos.InnerManagerDto;
import com.sunesoft.seera.yc.core.manager.domain.InnerManager;
import com.sunesoft.seera.yc.core.tourist.application.ITouristService;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristSimpleDto;
import com.sunesoft.seera.yc.core.tourist.domain.criteria.TouristCriteria;
import com.sunesoft.seera.yc.webapp.controller.Layout;
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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by xiazl on 2016/9/8.
 */
@Controller
public class CouponController extends Layout {
    @Autowired
    ICouponService service;
    @Autowired
    ICouponTypeService couponTypeService;
    @Autowired
    IManagerService managerService;

    @Autowired
    ITouristService touristService;
    //优惠券领取清单
    @RequestMapping(value = "sra_t_re")
    public ModelAndView couponIndex(Model model){

       List<CouponType> couponTypes= couponTypeService.getAll();

        model.addAttribute("couponTypes",couponTypes);
        return view(layout,"coupon/couponIndex",model);
    }

    @RequestMapping(value = "ajax_coupon_query_list")
    public void queryType(HttpServletResponse response, CouponCriteria criteria, HttpServletRequest request) {
      //  criteria.setStaffRealName(URI.deURI(criteria.getStaffRealName()));
        PagedResult pagedResult = service.findCoupons(criteria);
        String json = JsonHelper.toJson(pagedResult);
        AjaxResponse.write(response, json);
    }

    @RequestMapping(value = "_addOrUpCoupon")
    public ModelAndView addOrUpCouponType(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (!StringUtils.isNullOrWhiteSpace(id)) {
            CouponDto dto = service.getById(Long.parseLong(id));
            model.addAttribute("beans", dto);
        }
//        //未禁用的管理员
//        TouristCriteria managerCriteria=new TouristCriteria();
//
//        managerCriteria.setPageSize(99999999);
//        List<TouristSimpleDto>  tourists=touristService.findSimpleTourists(managerCriteria).getItems();
//        model.addAttribute("tourists", tourists);
//        CouponCriteria criteria = new CouponCriteria();
//        criteria.setPageSize(10);
//        PagedResult<CouponDto> pagedResult = service.findCoupons(criteria);
//        model.addAttribute("coupons", pagedResult.getItems());
        return view("/coupon/_addOrUpCoupon", model);
    }

    @RequestMapping(value = "ajax_add_update_Coupon", method = RequestMethod.POST)
    public void ajax_add_update_couponType( CouponDto dto,HttpServletResponse response, HttpServletRequest request) throws Exception {
        CommonResult result = null;
        if (dto.getId() == null) {
            result = service.addCoupon(dto);
        } else {
            result = service.updateCoupon(dto);
        }
        String json = JsonHelper.toJson(result);
        AjaxResponse.write(response, json);
    }

    @RequestMapping(value = "sra_createActivityCoupon")
    @ResponseBody
    public CommonResult createActivityCoupon(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");

        String key = request.getParameter("key");
        CouponDto dto1 = new CouponDto();

        dto1.setCouponStatus(CouponStatus.Valid);
        dto1.setCouponTypeId(Long.parseLong(id));
        dto1.setCreateDateTime(new Date());
        dto1.setNum("20170124");
        dto1.setUseCondition(BigDecimal.valueOf(190));
        dto1.setQuota(BigDecimal.valueOf(20));
        dto1.setGqDate("2017-02-26 00:00:00");
        service.addCoupon(dto1);
        if(!StringUtils.isNullOrWhiteSpace(key)&&key.equals("zhouzh123")) {
            for (Integer i = 0; i < 8000; i++) {
                CouponDto dto = new CouponDto();

                dto.setCouponStatus(CouponStatus.Valid);
                dto.setCouponTypeId(Long.parseLong(id));
                dto.setCreateDateTime(new Date());
                dto.setNum(getCouponNum(i+1));
                dto.setUseCondition(BigDecimal.valueOf(190));
                dto.setQuota(BigDecimal.valueOf(20));
                dto.setGqDate("2017-02-26 00:00:00");
                service.addCoupon(dto);
            }
            return ResultFactory.commonSuccess();
        }
        return ResultFactory.commonError("您没权限");

    }


    private String  getCouponNum(Integer i){
        String num="CQLYSINA";
        if(i<10)
         num = num +"000"+i;

        if(i>=10&&i<100)
            num = num +"00"+i;
        if(i>=100&&i<1000)
            num = num +"0"+i;
        if(i>=1000&&i<10000)
            num = num +i;

        return num;

    }

}
