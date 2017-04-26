package com.sunesoft.seera.yc.webapp.controller.product;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.JsonHelper;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.fileSys.FileService;
import com.sunesoft.seera.yc.core.fileSys.FileType;
import com.sunesoft.seera.yc.core.product.application.IProductItemService;
import com.sunesoft.seera.yc.core.product.application.IProductService;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductDto;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductItemSimpleDto;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductSimpleDto;
import com.sunesoft.seera.yc.core.product.domain.ProductCT;
import com.sunesoft.seera.yc.core.product.domain.ProductStatus;
import com.sunesoft.seera.yc.core.product.domain.ProductType;
import com.sunesoft.seera.yc.core.product.domain.criteria.ProductCriteria;
import com.sunesoft.seera.yc.core.product.domain.criteria.ProductItemCriteria;
import com.sunesoft.seera.yc.webapp.controller.Layout;
import com.sunesoft.seera.yc.webapp.utils.AjaxResponse;
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
import java.util.*;

/**
 * Created by liulin on 2016/7/20.
 */
@Controller
public class ProductManagerController extends Layout {

    @Autowired
    IProductService iProductService;

    @Autowired
    FileService fileService;

    @Autowired
    IProductItemService iProductItemService;

    /**
     * 商品首页
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "sra_t_product")
    public ModelAndView index(Model model) {
        Map<Integer,String> map=iProductService.getAllProvices();
        model.addAttribute("map",map);
        return view(layout, "product/index", model);
    }

    /**
     * 商品页面 数据查询
     *
     * @param response
     * @param criteria
     * @param request
     */
    @RequestMapping(value = "ajax_goods_query")
    public void queryData(HttpServletResponse response, ProductCriteria criteria, HttpServletRequest request) {

//        if (!StringUtils.isNullOrWhiteSpace(request.getParameter("status")))
//            criteria.setStatus(ProductStatus.valueOf(request.getParameter("status")));
        String limitAreas=request.getParameter("limitAreas");
        if(!StringUtils.isNullOrWhiteSpace(limitAreas)){
            criteria.setRejectAreas(limitAreas);
        }
        String beginTime = request.getParameter("begin_time");
        String endTime = request.getParameter("end_time");
        if (!StringUtils.isNullOrWhiteSpace(beginTime)) {
            criteria.setStartTime(DateHelper.parse(beginTime, "yyyy-MM-dd"));
        }
        if (!StringUtils.isNullOrWhiteSpace(endTime)) {
            criteria.setEndTime(DateHelper.parse(endTime, "yyyy-MM-dd"));

        }
        if(!StringUtils.isNullOrWhiteSpace(criteria.getName()))
            criteria.setName(URI.deURI(criteria.getName()));
//        String name = (request.getParameter("goodsName"));
//        if (!StringUtils.isNullOrWhiteSpace(name)) {
//            criteria.setName(URI.deURI(name));
//        }

        PagedResult pagedResult = iProductService.findProducts(criteria);

        String json = JsonHelper.toJson(pagedResult);
        AjaxResponse.write(response, json);
    }

//    /**
//     * 商品 新增页面
//     *
//     * @param model
//     * @return
//     */
//    @RequestMapping(value = "addGoods")
//    public ModelAndView addGoodsManagerForm(Model model) {
//        ProductItemCriteria productItemCriteria = new ProductItemCriteria();
//        productItemCriteria.setPageNumber(0);
//        productItemCriteria.setPageSize(Integer.MAX_VALUE);
//        PagedResult pagedResult = iProductItemService.findProductItems(productItemCriteria);
//        model.addAttribute("beans", pagedResult.getItems());
//        return view(layout, "product/addprobase", model);
//    }

    /**
     * 商品 基本编辑页面
     *
     * @param model
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "sra_p_add_or_update_product")
    public ModelAndView addProductBase(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (!StringUtils.isNullOrWhiteSpace(id)) {



            ProductDto productDto = iProductService.get(Long.parseLong(id));
            model.addAttribute("beans", productDto);
            List<Integer> keys=new ArrayList<>();
            if(!StringUtils.isNullOrWhiteSpace(productDto.getRejectAreas())){
                List<String> list=Arrays.asList(productDto.getRejectAreas().split(","));
                for(String s:list){
                    keys.add(Integer.parseInt(s));
                }
            }
            model.addAttribute("keys",keys);
            model.addAttribute("isEdit", true);
        }
        List<ProductItemSimpleDto> simpleDtoList = iProductItemService.getProductItemSimpleDtos();
        model.addAttribute("itemList", simpleDtoList);
        Map<Integer,String> map=iProductService.getAllProvices();
        model.addAttribute("map",map);//增加省份
        return view(layout, "product/addprobase", model);
    }

    /**
     * 商品 基本编辑页面提交
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "ajax_add_productBase", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addProductBaseForm(HttpServletRequest request, ProductDto dto) {

        ProductDto productDto = new ProductDto();
        String limitAreas=request.getParameter("limitAreas");
        if(limitAreas!=null){
           productDto.setRejectAreas(limitAreas);
        }

        CommonResult result;
        productDto.setName(dto.getName());
        productDto.setStatus(dto.getStatus());
        productDto.setCanMeal(dto.getCanMeal());
        productDto.setCanReturn(dto.getCanReturn());
        productDto.setDiscountPrice(dto.getDiscountPrice());
        productDto.setPrice(dto.getPrice());
        productDto.setSpecDescription(dto.getSpecDescription());
        productDto.setType(dto.getType());
        productDto.setStock(dto.getStock());
        productDto.setOnSaleTime(DateHelper.parse(request.getParameter("time"),"yyyy-MM-dd"));
        productDto.setKind(dto.getKind());
        productDto.setProductCt(dto.getProductCt());


        String itemIds = request.getParameter("hiddenGoodsItems");//id 12,13,14
        String goodsItemsNum = request.getParameter("hiddenGoodsItemsNums");//num 2,3,4
        String type = request.getParameter("hiddenGoodsItemType");


        String sdate = request.getParameter("sctBeginDate");

        String edate = request.getParameter("sctEndDate");

        if(dto.getProductCt()== ProductCT.TimeSpan) {
            if (!StringUtils.isNullOrWhiteSpace(dto.getCtBeginDates()[0]))
                productDto.setCtBeginDate(DateHelper.parse(dto.getCtBeginDates()[0], "yyyy-MM-dd"));

            if (!StringUtils.isNullOrWhiteSpace(edate))
                productDto.setCtEndDate(DateHelper.parse(edate, "yyyy-MM-dd"));
        }

        if(dto.getProductCt()== ProductCT.Today) {
            if (!StringUtils.isNullOrWhiteSpace(dto.getCtBeginDates()[1])) {
                productDto.setCtBeginDate(DateHelper.parse(dto.getCtBeginDates()[1], "yyyy-MM-dd"));
                productDto.setCtEndDate(DateHelper.parse(dto.getCtBeginDates()[1],"yyyy-MM-dd"));
            }
        }
//        if(dto.getProductCt()== ProductCT.Nomal){
//            productDto.setCtEndDate(null);
//            productDto.setCtBeginDate(null);
//        }
        String[] types = type.split(",");
        if (types.length == 1) {
            if (types[0].equals("Ticket")) {
                productDto.setType(ProductType.Ticket);
            }
            if (types[0].equals("Catering")) {
                productDto.setType(ProductType.Catering);
            }
            if (types[0].equals("Other")) {
                productDto.setType(ProductType.Other);
            }
            if (types[0].equals("Hotel")) {
                productDto.setType(ProductType.Hotel);
            }
            if (types[0].equals("Souvenirs")) {
                productDto.setType(ProductType.Souvenirs);
            }
        } else {
            for (int i = 0; i < types.length; i++) {

                if (types[i] != types[i + 1]) {
                    productDto.setType(ProductType.GroupProduct);
                    break;
                } else {
                    if (types[i].equals("Ticket")) {
                        productDto.setType(ProductType.Ticket);
                    }
                    if (types[i].equals("Catering")) {
                        productDto.setType(ProductType.Catering);
                    }
                    if (types[i].equals("Other")) {
                        productDto.setType(ProductType.Other);
                    }
                    if (types[i].equals("Hotel")) {
                        productDto.setType(ProductType.Hotel);
                    }
                    if (types[i].equals("Souvenirs")) {
                        productDto.setType(ProductType.Souvenirs);
                    }
                }

            }
        }

        String[] ids = itemIds.split(",");
        String[] goodsItemsNums = goodsItemsNum.split(",");
        Map<Long, Integer> map = new HashMap<>();
        for (int i = 0; i < ids.length; i++) {
            Long goodsId = Long.parseLong(ids[i]);
            Integer num = Integer.parseInt(goodsItemsNums[i]);
            map.put(goodsId, num);
        }
        productDto.setIdAndCount(map);//设置商品的id和数量

        String id = request.getParameter("id");
        if (!StringUtils.isNullOrWhiteSpace(id)) {//编辑
            productDto.setId(Long.valueOf(id));
            result = iProductService.edit(productDto);
        } else {

            result = iProductService.create(productDto);

        }
        return result;
    }


    /**
     * 商品 图片编辑页面
     *
     * @param model
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "sra_p_add_or_update_ProPicture")
    public ModelAndView addProductPicture(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (!StringUtils.isNullOrWhiteSpace(id)) {
            ProductDto productDto = iProductService.get(Long.parseLong(id));
            model.addAttribute("id", productDto.getId());
            model.addAttribute("mainPath", productDto.getMainPicturePath());
            model.addAttribute("detailPath", productDto.getDetailPicturesPaths());
        }
        return view(layout, "product/addpropicture", model);
    }

    @RequestMapping(value = "ajax_remove_productPicture")
    @ResponseBody
    public CommonResult removePic(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String id = request.getParameter("id");
        String file = request.getParameter("fileName");

        ProductDto productDto = iProductService.get(Long.valueOf(id));

        if (productDto.getDetailPicturesPaths().size() > 0) {

            List<String> list = new ArrayList<>();
            list.addAll(productDto.getDetailPicturesPaths());

            Iterator<String> it = list.iterator();
            while (it.hasNext()) {
                String  f = it.next();
                if (f.equals(file)) {
                    it.remove();
                    break;
                }

            }
            productDto.setDetailPicturesPaths(list);

        }
        return    iProductService.edit(productDto);
    }


    /**
     * 商品 图片编辑页面提交
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "ajax_add_productPicture", method = RequestMethod.POST)
    public void addProductPictureForm(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String id = request.getParameter("id");
        String fileType = request.getParameter("fileType");
        String fileName = "";
        String subPath = "";
        CommonResult result;
        if (StringUtils.isNullOrWhiteSpace(id)) {
            CommonResult res = new CommonResult(false, "sorry！请先添加商品！");
            AjaxResponse.write(response, JsonHelper.toJson(res));
            return;
        }
        response.setContentType("text/plain; charset=UTF-8");

        //设置响应给前台内容的PrintWriter对象
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
                    fileName = DateHelper.formatDate(new Date(),"yyyyMMddhhmmSSsss");
                    fileName = fileName+"."+extension;
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
        List<String> pas = new ArrayList<String>();
        if (!StringUtils.isNullOrWhiteSpace(id)) {//
            ProductDto productDto = iProductService.get(Long.valueOf(id));
            if (fileType.equals("mainFile")) {
                //TODO 新图片处理
                productDto.setMainPicturePath(filefullPath);
            } else {
                if (productDto.getDetailPicturesPaths() != null && productDto.getDetailPicturesPaths().size() > 0) {
                    pas.addAll(productDto.getDetailPicturesPaths());

                }
                pas.add(filefullPath);
                productDto.setDetailPicturesPaths(pas);

            }

            //List<String> st = Arrays.asList(request.getParameter("s").split(","));
            out.flush();
            CommonResult result1 = iProductService.edit(productDto);
            AjaxResponse.write(response, JsonHelper.toJson(result1));
            return;
        }
        AjaxResponse.write(response, JsonHelper.toJson(new CommonResult(false, id + " 商品标识不能为空")));
    }


    /**
     * 商品 详细信息编辑页面
     *
     * @param model
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "sra_add_or_update_ProDetail")
    public ModelAndView addProductDetail(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (!StringUtils.isNullOrWhiteSpace(id)) {
            ProductDto productDto = iProductService.get(Long.parseLong(id));
            model.addAttribute("id", productDto.getId());
            model.addAttribute("profile", productDto.getProfile());
            model.addAttribute("notice", productDto.getNotice());
            model.addAttribute("trafficGuide", productDto.getTrafficGuide());
        }

        return view(layout, "product/addprodetail", model);
    }

    /**
     * 商品 详细信息编辑页面提交
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "ajax_add_productDetail", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addProductDetailForm(HttpServletRequest request, ProductDto dto) {
        String id = request.getParameter("beanId");
        if (!StringUtils.isNullOrWhiteSpace(id)) {//编辑
            ProductDto productDto = iProductService.get(Long.valueOf(id));
            productDto.setProfile(dto.getProfile());
            productDto.setNotice(dto.getNotice());
            productDto.setTrafficGuide(dto.getTrafficGuide());
            return iProductService.edit(productDto);

        }
        return ResultFactory.commonError(id + " 商品标识不能为空");
    }

    /**
     * 商品 新增页面和修改页面 获取商品项信息
     *
     * @return
     */
    @RequestMapping(value = "ajax_get_goodsItemsInfo")
    @ResponseBody
    public PagedResult getGoodsItemsInfo(ProductItemCriteria productItemCriteria) {

        productItemCriteria.setPageNumber(1);
        productItemCriteria.setPageSize(Integer.MAX_VALUE);
        PagedResult pagedResult = iProductItemService.findProductItems(productItemCriteria);
        return pagedResult;
    }

    /**
     * 商品 页面 单个删除
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "ajax_delete_product")
    @ResponseBody
    public CommonResult deleteGoods(HttpServletRequest request) {
        String id = request.getParameter("id");
        return iProductService.remove(Long.valueOf(id));
    }

    /**
     * 商品 页面 批量删除
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "ajax_delete_product_list")
    @ResponseBody
    public CommonResult deleteProductList(HttpServletRequest request) {
        String id = request.getParameter("ids");
        String[] ids = id.split(",");
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            list.add(Long.parseLong(ids[i]));
        }
        CommonResult commonResult = iProductService.remove(list);
        return commonResult;
    }

    /**
     * 商品 页面 设置商品状态：上架 下架等等
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "ajax_set_goodsStatus")
    @ResponseBody
    public String setGoodsStatus(HttpServletRequest request) {
        ProductStatus status = ProductStatus.valueOf(request.getParameter("status"));
        String id = request.getParameter("id");
        ProductSimpleDto product = iProductService.getSimple(id);
        iProductService.setProductStatus(Long.parseLong(id), status);
        return "success";
    }


}

