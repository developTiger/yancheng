package com.sunesoft.seera.yc.webapp.controller.picture;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.JsonHelper;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.picture.application.IPictureService;
import com.sunesoft.seera.yc.core.picture.application.dto.PictureDto;
import com.sunesoft.seera.yc.core.picture.domain.criteria.PictureCriteria;
import com.sunesoft.seera.yc.webapp.controller.Layout;
import com.sunesoft.seera.yc.webapp.utils.AjaxResponse;
import com.sunesoft.seera.yc.webapp.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiazl on 2016/9/6.
 */
public class PictureController extends Layout {
    @Autowired
    IPictureService service;

    @RequestMapping(value = "sra_p_picture")
    public ModelAndView index(Model model) {
        return view(layout, "picture/index", model);
    }

    @RequestMapping(value = "ajax_picture_query_list")
    public void queryPicture(HttpServletResponse response, PictureCriteria criteria) {
        PagedResult pagedResult = service.page(criteria);
        String json = JsonHelper.toJson(pagedResult);
        AjaxResponse.write(response, json);

    }

    @RequestMapping(value = "_addOrUpPicture")
    public ModelAndView addOrUpActivity(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (!StringUtils.isNullOrWhiteSpace(id)) {
            PictureDto dto = service.getById(Long.parseLong(id));
            model.addAttribute("beans", dto);
        }
        model.addAttribute("helper", Helper.class);
        PictureCriteria criteria = new PictureCriteria();
        criteria.setPageSize(1000);
        PagedResult<PictureDto> pagedResult = service.page(criteria);
        model.addAttribute("pictures", pagedResult.getItems());
        return view("/picture/_addOrUpPicture", model);
    }

    @RequestMapping(value = "ajax_add_update_picture", method = RequestMethod.POST)
    public void ajax_add_update_activity(PictureDto dto, HttpServletResponse response, HttpServletRequest request) throws IOException {
        CommonResult result = null;
        if (dto.getId() == null) {
            result = service.create(dto);
        } else {
            result = service.edit(dto);
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
        CommonResult result = service.remove(listid);
        String json = JsonHelper.toJson(result);
        AjaxResponse.write(response, json);
    }

    @RequestMapping(value = "get_页面名称_pictures")
    public ModelAndView get_index_pictures(Model model, HttpServletRequest request) {
        String location = request.getParameter("location");
        List<PictureDto> dtos = service.getByLocation(location);
        model.addAttribute("pictures", dtos);
        return view(layout, "对应的页面", model);
    }

}
