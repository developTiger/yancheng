package com.sunesoft.seera.yc.webapp.controller;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.activity.application.IActivityService;
import com.sunesoft.seera.yc.core.activity.application.dtos.ActivityDto;
import com.sunesoft.seera.yc.core.picture.application.IPictureService;
import com.sunesoft.seera.yc.core.picture.application.dto.PictureDto;
import com.sunesoft.seera.yc.core.product.application.IProductService;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductDto;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductSimpleDto;
import com.sunesoft.seera.yc.core.product.domain.ProductKind;
import com.sunesoft.seera.yc.core.product.domain.ProductStatus;
import com.sunesoft.seera.yc.core.product.domain.ProductType;
import com.sunesoft.seera.yc.core.product.domain.criteria.ProductCriteria;
import com.sunesoft.seera.yc.webapp.model.PictureAllFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jade on 16/8/18.
 */
@Controller
public class IndexController extends BaseController {


    @Autowired
    private IProductService iProductService;
    @Autowired
    private IActivityService activityService;

//    @Autowired
//    private PictureAllFactory pictureAllFactory;

    @Autowired
    private IPictureService pictureService;

    /**
     * 首页
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/")
    public ModelAndView index(Model model) {
        ProductCriteria productCriteria = new ProductCriteria();
        productCriteria.setStatus(ProductStatus.OnSale);
        productCriteria.setKind(ProductKind.Recommended);
        productCriteria.setPageSize(4);
        productCriteria.setOrderByProperty("onSaleTime");
        productCriteria.setAscOrDesc(false);
        //推荐商品
        PagedResult<ProductSimpleDto> productSimpleDtoPagedResult = iProductService.findProductsSimple(productCriteria);
        if (productSimpleDtoPagedResult != null) {
            model.addAttribute("recommendedProducts", productSimpleDtoPagedResult.getItems());
        }
        //热卖商品
        productCriteria.setKind(ProductKind.HotSell);
        PagedResult<ProductSimpleDto> saleProductsPageResult = iProductService.findProductsSimple(productCriteria);
        if (saleProductsPageResult != null) {
            model.addAttribute("saleProducts", saleProductsPageResult.getItems());
        }
        //组合商品
        productCriteria.setType(ProductType.GroupProduct);
        productCriteria.setPageSize(3);
        PagedResult<ProductSimpleDto> groupProductsPageResult = iProductService.findProductsSimple(productCriteria);
        if (groupProductsPageResult != null) {
            model.addAttribute("groupProducts", groupProductsPageResult.getItems());
        }
        List<PictureDto> picMap = pictureService.getByLocationMobile("mobileIndex");
        model.addAttribute("p_2", picMap);
        return view("index/index", model);
    }


    /**
     * 首页查询结果
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/search.html")
    public ModelAndView search_view(Model model, HttpServletRequest request) {
//        try {
        String name1 = request.getParameter("name");
        //String name1 = new String(name.getBytes("ISO-8859-1"), "utf-8");


        ProductCriteria productCriteria = new ProductCriteria();
        productCriteria.setStatus(ProductStatus.OnSale);
        productCriteria.setPageSize(1000);
        productCriteria.setName(name1);
        PagedResult<ProductSimpleDto> pr = iProductService.findProductsSimple(productCriteria);
        model.addAttribute("list", pr.getItems());
        model.addAttribute("name", name1);

        return view("/index/result", model);
    }


    /**
     * 首页查询结果
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/searchtype")
    public ModelAndView search_byType(Model model, HttpServletRequest request) {
//        try {

        //String name1 = new String(name.getBytes("ISO-8859-1"), "utf-8");


        ProductCriteria productCriteria = new ProductCriteria();
        productCriteria.setStatus(ProductStatus.OnSale);
        productCriteria.setPageSize(1000);
        String type = request.getParameter("type");
        if(!StringUtils.isNullOrWhiteSpace(type))
       productCriteria.setType(ProductType.valueOf(type));

        String kind = request.getParameter("kind");
        if(!StringUtils.isNullOrWhiteSpace(kind))
            productCriteria.setKind(ProductKind.valueOf(kind));

        PagedResult<ProductSimpleDto> pr = iProductService.findProductsSimple(productCriteria);
        model.addAttribute("list", pr.getItems());

//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        return view("index/result", model);
    }

    /**
     * 关于我们
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "about.html")
    public ModelAndView about_view(Model model) {
        return view("about", model);
    }


    @RequestMapping(value = "/active/ActivityDetails/{id}")
    public ModelAndView Activity_Details(Model model, @PathVariable("id") Long id) {
        ActivityDto activityDto = activityService.getById(id);
        model.addAttribute("activityDto", activityDto);
        return view("index/acticityDetailsIndex", model);
    }

    @RequestMapping(value = "/errorinfo")
    public ModelAndView Activity_Details(Model model, HttpServletRequest request) {
        String msg = request.getParameter("msg");

        model.addAttribute("msg", msg);
        return view("error", model);
    }


}
