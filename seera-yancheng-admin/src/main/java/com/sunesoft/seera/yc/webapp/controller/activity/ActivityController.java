package com.sunesoft.seera.yc.webapp.controller.activity;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.JsonHelper;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.activity.application.IActivityService;
import com.sunesoft.seera.yc.core.activity.application.ShareActivityService;
import com.sunesoft.seera.yc.core.activity.application.dtos.ActivityDto;
import com.sunesoft.seera.yc.core.activity.application.dtos.ShareActivityDto;
import com.sunesoft.seera.yc.core.activity.domain.ActivityStatus;
import com.sunesoft.seera.yc.core.activity.domain.creteria.ActivityCriteria;
import com.sunesoft.seera.yc.core.activity.domain.creteria.ShareActivityCriteria;
import com.sunesoft.seera.yc.core.fileSys.FileService;
import com.sunesoft.seera.yc.core.fileSys.FileType;
import com.sunesoft.seera.yc.core.product.application.IProductService;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductSimpleDto;
import com.sunesoft.seera.yc.core.product.domain.criteria.ProductCriteria;
import com.sunesoft.seera.yc.webapp.controller.Layout;
import com.sunesoft.seera.yc.webapp.utils.AjaxResponse;
import com.sunesoft.seera.yc.webapp.utils.Helper;
import com.sunesoft.seera.yc.webapp.utils.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/7/21.
 */
@Controller
public class ActivityController extends Layout {

    @Autowired
    IActivityService iActivityService;

    @Autowired
    IProductService productService;

    @Autowired
    FileService fileService;

    @Autowired
    ShareActivityService shareActivityService;


    @RequestMapping(value = "sra_t_shareActivity")
    public ModelAndView shareActivity(Model model,HttpServletRequest request) {
        return view(layout, "activity/shareActivity", model);
    }
    /**
     * 分享活动获取
     * @param
     * @return
     */
    @RequestMapping(value="ajax_share_activity_Page")
    @ResponseBody
    public PagedResult<ShareActivityDto> ajax_share_activity_Page(Model model,HttpServletRequest request,ShareActivityCriteria criteria){

        if(!StringUtils.isNullOrWhiteSpace(criteria.getActivityName()))
            criteria.setActivityName(URI.deURI(criteria.getActivityName()).trim());
        if(!StringUtils.isNullOrWhiteSpace(criteria.getWxName()))
            criteria.setWxName(URI.deURI(criteria.getWxName()).trim());
        PagedResult<ShareActivityDto>  result = shareActivityService.getShareDtoPaged(criteria);
        return result;
    }

    @RequestMapping(value = "sra_t_AddOrUpActicity")
    public ModelAndView sra_t_AddOrUpActicity(Model model,HttpServletRequest request) {

        String id=request.getParameter("id");
        if(!StringUtils.isNullOrWhiteSpace(id)) {
            ActivityDto activityDto = iActivityService.getById(Long.parseLong(id));
            model.addAttribute("beans", activityDto);
        }
        ProductCriteria criteria = new ProductCriteria();
        criteria.setPageSize(1000);
        PagedResult<ProductSimpleDto> pagedResult = productService.findProductsSimple(criteria);
        model.addAttribute("products", pagedResult.getItems());

        return view(layout, "activity/editActivityInfo", model);
    }

    @RequestMapping(value = "sra_t_editPage")
    public ModelAndView sra_t_editPage(Model model,HttpServletRequest request) {

        String id=request.getParameter("id");
        if(!StringUtils.isNullOrWhiteSpace(id)) {
            ActivityDto activityDto = iActivityService.getById(Long.parseLong(id));
            model.addAttribute("beans", activityDto);
        }
        return view(layout, "activity/editActivityPageInfo", model);
    }

    @RequestMapping(value = "sra_t_MobilePage")
    public ModelAndView sra_t_MobilePage(Model model,HttpServletRequest request) {

        String id=request.getParameter("id");
        if(!StringUtils.isNullOrWhiteSpace(id)) {
            ActivityDto activityDto = iActivityService.getById(Long.parseLong(id));
            model.addAttribute("beans", activityDto);
        }
        return view(layout, "activity/editActivityMobileInfo", model);
    }



    /**
     * 文章内容编辑
     * @param
     * @return
     */
    @RequestMapping(value="ajax_activity_Page")
    @ResponseBody
    public CommonResult ajax_activity_Page(Model model,HttpServletRequest request,ActivityDto activityDto){

        CommonResult  result = iActivityService.updatePage(activityDto);
        return result;
    }

    @RequestMapping(value="ajax_activity_Mobile_Page")
    @ResponseBody
    public CommonResult ajax_activity_Mobile_Page(Model model,HttpServletRequest request,ActivityDto activityDto){

        CommonResult  result = iActivityService.updatePage(activityDto);
        return result;
    }



    @RequestMapping(value = "sra_t_t")
    public ModelAndView index(Model model) {
        return view(layout, "activity/index", model);
    }

    @RequestMapping(value = "ajax_titleEvent_query_list")
    public void queryData(HttpServletResponse response, ActivityCriteria activityCriteria, HttpServletRequest request) {
        String beginTime = request.getParameter("start_Time");
        String endTime = request.getParameter("end_Time");
        if (!StringUtils.isNullOrWhiteSpace(beginTime))
            activityCriteria.setBeginTime(DateHelper.parse(beginTime, "yyyy-MM-dd"));
        if (!StringUtils.isNullOrWhiteSpace(endTime))
            activityCriteria.setOverTime(DateHelper.parse(endTime, "yyyy-MM-dd"));
        try {
            activityCriteria.setName(URLDecoder.decode(activityCriteria.getName(), "utf-8") );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        PagedResult pagedResult = iActivityService.findPage(activityCriteria);
        String json = JsonHelper.toJson(pagedResult);
        AjaxResponse.write(response, json);

    }

    @RequestMapping(value="sra_dd")
    public ModelAndView dd_view(){
        return null;
    }


    @RequestMapping(value = "_addOrUpActivity")
    public ModelAndView addOrUpActivity(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (!StringUtils.isNullOrWhiteSpace(id)) {
            ActivityDto activityDto = iActivityService.getById(Long.parseLong(id));
            model.addAttribute("beans", activityDto);
        }
        model.addAttribute("helper", Helper.class);
        ProductCriteria criteria = new ProductCriteria();
        criteria.setPageSize(1000);
        PagedResult<ProductSimpleDto> pagedResult = productService.findProductsSimple(criteria);
        model.addAttribute("products", pagedResult.getItems());
        return view("/activity/_addOrUpActivity", model);
    }

    @RequestMapping(value = "ajax_add_update_activity", method = RequestMethod.POST)
    public void ajax_add_update_activity(ActivityDto activityDto, HttpServletResponse response, HttpServletRequest request) throws IOException {
        CommonResult result = null;

        String beginTime = request.getParameter("start_Time");
        String endTime = request.getParameter("end_Time");
        if (!StringUtils.isNullOrWhiteSpace(beginTime))
            activityDto.setStartTime(DateHelper.parse(beginTime, "yyyy-MM-dd"));
        if (!StringUtils.isNullOrWhiteSpace(endTime))
            activityDto.setEndTime(DateHelper.parse(endTime, "yyyy-MM-dd"));
        String fileName = "";
        String subPath = "";
        response.setContentType("text/plain; charset=UTF-8");

        //设置响应给前台内容的PrintWriter对象
        PrintWriter out = response.getWriter();

        //上传文件的原名(即上传前的文件名字)
        String originalFilename = null;
        List<MultipartFile> images = ((DefaultMultipartHttpServletRequest) request).getFiles("image");
        for (MultipartFile myfile : images) {
            if (!myfile.isEmpty()) {
                try {
                    fileName = myfile.getOriginalFilename();
                    fileService.upload(fileName, subPath, myfile.getInputStream(), FileType.ProductImage);
                } catch (IOException e) {
                    System.out.println("文件[" + originalFilename + "]上传失败,堆栈轨迹如下");
                    e.printStackTrace();
                    out.print("1`文件上传失败，请重试！！");
                    out.flush();
                    CommonResult res = new CommonResult(false, "文件上传失败，请重试！！");
                    AjaxResponse.write(response, JsonHelper.toJson(res));
                    return;
                }
            }
        }
        String filefullPath = "";
        if (!StringUtils.isNullOrWhiteSpace(subPath)) {
            filefullPath = subPath + "_" + fileName;
        } else
            filefullPath = fileName;
        if (!StringUtils.isNullOrWhiteSpace(filefullPath))
            activityDto.setPticturePath(filefullPath);
        if (activityDto.getId() == null) {
            result = iActivityService.create(activityDto);
        } else {
            result = iActivityService.edit(activityDto);
        }
        String json = JsonHelper.toJson(result);
        AjaxResponse.write(response, json);

    }


    @RequestMapping(value = "ajax_delete_activity")
    public void ajax_delete_activity(HttpServletResponse response, HttpServletRequest request) {
        String ids = request.getParameter("ids");
        List<Long> listid = new ArrayList<>();
        String[] pids = ids.split(",");
        for (String id : pids) {
            listid.add(Long.parseLong(id));
        }
        CommonResult result = iActivityService.remove(listid);
        //model.addAttribute("beans", activityDto);
        String json = JsonHelper.toJson(result);
        AjaxResponse.write(response, json);

    }

    @RequestMapping(value = "ajax_activity_change_status")
    public void ajax_activity_change_status(HttpServletResponse response, HttpServletRequest request) {
        String id = request.getParameter("id");
        String status = request.getParameter("status");
        CommonResult result = iActivityService.changeActivityStatuss(Long.parseLong(id), ActivityStatus.valueOf(status));
        String json = JsonHelper.toJson(result);
        AjaxResponse.write(response, json);

    }



}
