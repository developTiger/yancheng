package com.sunesoft.seera.yc.webapp.controller.couponType;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.ListResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.JsonHelper;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.coupon.application.ICouponTypeService;
import com.sunesoft.seera.yc.core.coupon.application.dto.CouponTypeDto;
import com.sunesoft.seera.yc.core.coupon.application.dto.UploadCoupon;
import com.sunesoft.seera.yc.core.coupon.application.factory.CouponUploadFactory;
import com.sunesoft.seera.yc.core.coupon.domain.CouponTypeStatus;
import com.sunesoft.seera.yc.core.coupon.domain.criteria.CouponTypeCriteria;
import com.sunesoft.seera.yc.webapp.controller.Layout;
import com.sunesoft.seera.yc.webapp.utils.AjaxResponse;
import com.sunesoft.seera.yc.webapp.utils.Helper;
import com.sunesoft.seera.yc.webapp.utils.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券领取活动
 * Created by xiazl on 2016/9/5.
 */
@Controller
public class CouponTypeController extends Layout {

    @Autowired
    ICouponTypeService service;

    @RequestMapping(value = "sra_t_c")
    public ModelAndView index(Model model) {
        return view(layout, "couponType/couponTypeIndex", model);
    }

    @RequestMapping(value = "ajax_couponType_query_list")
    public void queryType(HttpServletResponse response, CouponTypeCriteria criteria, HttpServletRequest request) {
        String beginTime = request.getParameter("start_Time");
        String endTime = request.getParameter("over_Time");
        String status=request.getParameter("status");
        String startQuota=request.getParameter("start_quota");
        String endQuota=request.getParameter("end_quota");
        if (!StringUtils.isNullOrWhiteSpace(beginTime))
            criteria.setStartTime(DateHelper.parse(beginTime, "yyyy-MM-dd"));
        if (!StringUtils.isNullOrWhiteSpace(endTime))
            criteria.setEndTime(DateHelper.parse(endTime, "yyyy-MM-dd"));
        if(!StringUtils.isNullOrWhiteSpace(status))
            criteria.setStatus(CouponTypeStatus.valueOf(status));
        if(!StringUtils.isNullOrWhiteSpace(startQuota))
            criteria.setStartQuota(BigDecimal.valueOf(Double.parseDouble(startQuota)));
        if(!StringUtils.isNullOrWhiteSpace(endQuota))
            criteria.setEndQuota(BigDecimal.valueOf(Double.parseDouble(endQuota)));
        if(!StringUtils.isNullOrWhiteSpace(criteria.getCouponTypeName()))
            criteria.setCouponTypeName(URI.deURI(criteria.getCouponTypeName()));
        PagedResult pagedResult = service.findPage(criteria);
        String json = JsonHelper.toJson(pagedResult);
        AjaxResponse.write(response, json);

    }

    @RequestMapping(value = "_addOrUpCouponType")
    public ModelAndView addOrUpCouponType(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (!StringUtils.isNullOrWhiteSpace(id)) {
            CouponTypeDto dto = service.getById(Long.parseLong(id));
            model.addAttribute("beans", dto);
        }
        model.addAttribute("helper", Helper.class);
        CouponTypeCriteria criteria = new CouponTypeCriteria();
        criteria.setPageSize(10);
        PagedResult<CouponTypeDto> pagedResult = service.findPage(criteria);
        model.addAttribute("couponTypes", pagedResult.getItems());
        return view("/couponType/_addOrUpCouponType", model);
    }

    //优惠券活动的增加与修改
    @RequestMapping(value = "ajax_add_update_CouponType", method = RequestMethod.POST)
    public void ajax_add_update_couponType( CouponTypeDto dto,HttpServletResponse response, HttpServletRequest request) throws Exception {
        CommonResult result = null;

        String beginTime = request.getParameter("start_Time");
        String endTime = request.getParameter("over_Time");
        String couponStart = request.getParameter("coupon_start");
        String couponOver = request.getParameter("coupon_over");

        if (!StringUtils.isNullOrWhiteSpace(couponStart))
            dto.setCouponStart(DateHelper.parse(couponStart, "yyyy-MM-dd"));
        if (!StringUtils.isNullOrWhiteSpace(couponOver))
            dto.setCouponOver(DateHelper.parse(couponOver, "yyyy-MM-dd"));
        if (!StringUtils.isNullOrWhiteSpace(beginTime))
            dto.setStartTime(DateHelper.parse(beginTime, "yyyy-MM-dd"));
        if (!StringUtils.isNullOrWhiteSpace(endTime))
            dto.setOverTime(DateHelper.parse(endTime, "yyyy-MM-dd"));

        if (dto.getId() == null) {
            result = service.create(dto);
        } else {
            result = service.edit(dto);
        }
        String json = JsonHelper.toJson(result);
        AjaxResponse.write(response, json);
    }
//多个删除
    @RequestMapping(value = "ajax_delete_couponType")
    public void ajax_delete_couponType(HttpServletResponse response, HttpServletRequest request) {
        String ids = request.getParameter("ids");
        List<Long> listid = new ArrayList<>();
        String[] pids = ids.split(",");
        for (String id : pids) {
            listid.add(Long.parseLong(id));
        }
        CommonResult result = service.delete(listid);
        String json = JsonHelper.toJson(result);
        AjaxResponse.write(response, json);

    }
//单个修改状态
    @RequestMapping(value = "ajax_couponType_change_status")
    public void ajax_couponType_change_status(HttpServletResponse response, HttpServletRequest request) {
        String id = request.getParameter("id");
        String status = request.getParameter("status");
        CommonResult result = service.setStatus(Long.parseLong(id), CouponTypeStatus.valueOf(status));
        String json = JsonHelper.toJson(result);
        AjaxResponse.write(response, json);
    }


    @RequestMapping("ajax_coupon_upload")
    public void upload(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

        CommonResult result = null;

        //上传文件的原名(即上传前的文件名字)
        String originalFilename = null;
        List<MultipartFile> myfiles = ((DefaultMultipartHttpServletRequest) request).getFiles("myfiles");
        for (MultipartFile myfile : myfiles) {
            if (myfile.isEmpty()) {
                result = new CommonResult(false, "请选择要上传的文件！");
                AjaxResponse.write(response, JsonHelper.toJson(result));
                return;
            } else {
//                originalFilename = myfile.getOriginalFilename();
                try {
                    CouponUploadFactory factory = new CouponUploadFactory();
                    ListResult<UploadCoupon> dtos = factory.readExcel(myfile.getInputStream());
                    if (dtos.getIsSuccess()) {
                        result = service.uploadManagerCoupons(dtos.getItems());
                        request.setAttribute("isUpload", true);
                    } else
                        result = new CommonResult(false, dtos.getMsg());

                } catch (IOException e) {
                    System.out.println("文件[" + originalFilename + "]上传失败,堆栈轨迹如下");
                    e.printStackTrace();
                    result = new CommonResult(false, "文件上传失败，请重试！！");
                    AjaxResponse.write(response, JsonHelper.toJson(result));
                    return;
                }
            }
        }
        AjaxResponse.write(response, JsonHelper.toJson(result));
        return;
    }

}
