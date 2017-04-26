package com.sunesoft.seera.yc.webapp.controller.addpicture;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.JsonHelper;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.fileSys.FileService;
import com.sunesoft.seera.yc.core.fileSys.FileType;
import com.sunesoft.seera.yc.core.picture.application.IPictureService;
import com.sunesoft.seera.yc.core.picture.application.dto.PictureDto;
import com.sunesoft.seera.yc.core.picture.domain.PictureType;
import com.sunesoft.seera.yc.core.picture.domain.criteria.PictureCriteria;
import com.sunesoft.seera.yc.webapp.controller.Layout;
import com.sunesoft.seera.yc.webapp.utils.AjaxResponse;
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
import java.util.Date;
import java.util.List;

/**
 * Created by liulin on 2016/7/20.
 */
@Controller
public class EditWebPictureController extends Layout {

    @Autowired
    IPictureService iPictureService;

    @Autowired
    FileService fileService;

    @RequestMapping(value = "sra_t_addpicture")
    public ModelAndView index(Model model) {

        return view(layout, "addpicture/editPicture", model);
    }

    @RequestMapping(value="ajax_editPicture_query_list")
    @ResponseBody
    public PagedResult<PictureDto> ajax_editPicture_query_list(Model model,PictureCriteria pictureCriteria){

        return iPictureService.page(pictureCriteria);
    }

    @RequestMapping(value="ajax_update_picture")
    @ResponseBody
    public CommonResult ajax_update_picture(HttpServletRequest request){
        String id=request.getParameter("id");
        String title=request.getParameter("title");
        PictureDto pictureDto=new PictureDto();
        pictureDto.setId(Long.parseLong(id));
        pictureDto.setPictureStatus(Boolean.valueOf(title));
        CommonResult commonResult=iPictureService.update(pictureDto);
        return commonResult;
    }

    @RequestMapping(value = "sra_t_editPictureInfo")
    public ModelAndView sra_t_editPictureInfo(Model model,HttpServletRequest request) {
        String id=request.getParameter("id");
        PictureDto pictureDto = new PictureDto();
        if(!StringUtils.isNullOrWhiteSpace(id)) {
            pictureDto= iPictureService.getById(Long.parseLong(id));
        }
        model.addAttribute("picture", pictureDto);
        return view(layout, "addpicture/editPictureInfo", model);
    }


    @RequestMapping(value = "  ajax_add_webPicture", method = RequestMethod.POST)
    public void addProductPictureForm(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String id = request.getParameter("id");
        String fileType = request.getParameter("fileType");
        String fileName = "";
        String subPath = "";
        CommonResult result;

        /*if (StringUtils.isNullOrWhiteSpace(id)) {
            CommonResult res = new CommonResult(false, "sorry！请先添加商品！");
            AjaxResponse.write(response, JsonHelper.toJson(res));
            return;
        }
        response.setContentType("text/plain; charset=UTF-8");

        //设置响应给前台内容的PrintWriter对象
        */

        PrintWriter out = response.getWriter();
        //上传文件的原名(即上传前的文件名字)
        String originalFilename = null;
        List<MultipartFile> images = ((DefaultMultipartHttpServletRequest) request).getFiles("image");
        for (MultipartFile myfile : images) {
            if (myfile.isEmpty()) {
                CommonResult res = new CommonResult(false, "请选择要上传的文件！");
                AjaxResponse.write(response, JsonHelper.toJson(res));
                return;
            } else {
                try {
                    String originalFilaName=myfile.getOriginalFilename();
                    String extension = originalFilaName.substring(originalFilaName.lastIndexOf(".") + 1);
                    fileName = DateHelper.formatDate(new Date(), "yyyyMMddhhmmSSsss");
                    fileName = fileName+"."+extension;
                    fileService.upload(fileName, subPath, myfile.getInputStream(), FileType.Oth);
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

        String location=request.getParameter("location");
        String pictureType=request.getParameter("pictureType");

        String position = request.getParameter("position");

        String name = request.getParameter("name");

        String link = request.getParameter("link");

        String description=request.getParameter("description");

        String filefullPath = "";
        if (!StringUtils.isNullOrWhiteSpace(subPath)) {
            filefullPath = subPath + "_" + fileName;
        } else
            filefullPath = fileName;
        if (!StringUtils.isNullOrWhiteSpace(id)) {
            PictureDto pictureDto = new PictureDto();
            pictureDto.setId(Long.parseLong(id));
            pictureDto.setPath(filefullPath);
            pictureDto.setLocation(location);
            pictureDto.setName(name);
            pictureDto.setPosition(Integer.parseInt(position));
            pictureDto.setLink(link);
            pictureDto.setDescription(description);
            pictureDto.setType(PictureType.valueOf(pictureType));
            out.flush();
            CommonResult result1 = iPictureService.edit(pictureDto);
            AjaxResponse.write(response, JsonHelper.toJson(result1));
            return;
        }else{
            PictureDto pictureDto=new PictureDto();
            pictureDto.setPath(filefullPath);
            pictureDto.setName(name);
            pictureDto.setLocation(location);
            pictureDto.setPosition(Integer.parseInt(position));
            pictureDto.setLink(link);
            pictureDto.setDescription(description);
            pictureDto.setType(PictureType.valueOf(pictureType));
            out.flush();
            CommonResult result1 = iPictureService.create(pictureDto);
            AjaxResponse.write(response, JsonHelper.toJson(result1));
            return;
        }
    }

    @RequestMapping(value = "ajax_remove_webPicture")
    @ResponseBody
    public CommonResult removePic(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String id = request.getParameter("id");
        String file = request.getParameter("fileName");

        PictureDto pictureDto = iPictureService.getById(Long.valueOf(id));

        pictureDto.setPath(null);

        return    iPictureService.edit(pictureDto);
    }

}

