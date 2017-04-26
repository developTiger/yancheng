package com.sunesoft.seera.yc.webapp.controller;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.activity.application.IActivityService;
import com.sunesoft.seera.yc.core.activity.application.dtos.ActivitySimpleDto;
import com.sunesoft.seera.yc.core.activity.domain.ActivityStatus;
import com.sunesoft.seera.yc.core.activity.domain.creteria.ActivityCriteria;
import com.sunesoft.seera.yc.core.article.application.IArticleService;
import com.sunesoft.seera.yc.core.picture.application.IPictureService;
import com.sunesoft.seera.yc.core.picture.application.dto.PictureDto;
import com.sunesoft.seera.yc.core.product.application.IProductService;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductSimpleDto;
import com.sunesoft.seera.yc.core.product.domain.ProductKind;
import com.sunesoft.seera.yc.core.product.domain.ProductStatus;
import com.sunesoft.seera.yc.core.product.domain.ProductType;
import com.sunesoft.seera.yc.core.product.domain.criteria.ProductCriteria;
import com.sunesoft.seera.yc.webapp.model.PictureAllFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by bing on 16/8/13.
 */
@Controller
public class IndexController extends Layout {

    @Autowired
    private IProductService iProductService;
    @Autowired
    private IActivityService activityService;

    @Autowired
    private IPictureService iPictureService;
    @Autowired
     IArticleService iArticleService;

    @Autowired
    PictureAllFactory pictureAllFactory;


    @RequestMapping("/")
    public ModelAndView index(Model model) throws Exception {


        ProductCriteria productCriteria = new ProductCriteria();
        productCriteria.setStatus(ProductStatus.OnSale);
        productCriteria.setKind(ProductKind.Recommended);
        productCriteria.setPageSize(6);
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
        productCriteria.setKind(null);
        productCriteria.setPageSize(3);
        PagedResult<ProductSimpleDto> groupProductsPageResult = iProductService.findProductsSimple(productCriteria);
        if (groupProductsPageResult != null) {
            model.addAttribute("groupProducts", groupProductsPageResult.getItems());
        }
        //周边商品
//
//        List<PictureDto> pictureDtos=iPictureService.getByLocation("index");
//        if(pictureDtos!=null) {
//            WebImgPosition webImageFactory = new WebImgPosition(pictureDtos);
//            model.addAttribute("webImage", webImageFactory.getPictureDtos());
//
//        }
        Map<Integer, List<PictureDto>> picMap = pictureAllFactory.getPicByLocation("index");

//        if(picMap.get(1)!=null) {
//            model.addAttribute("p_1", picMap.get(1).get(0));
//        }
        if (picMap.get(2) != null)
            model.addAttribute("p_2", picMap.get(2));
        if (picMap.get(3) != null)
            model.addAttribute("p_3", picMap.get(3).get(0));
        if (picMap.get(4) != null)
            model.addAttribute("p_4", picMap.get(4).get(0));
        if (picMap.get(5) != null)
            model.addAttribute("p_5", picMap.get(5).get(0));

//        List<ArticleDto> articleDtoList=iArticleService.getAll();
//        HelpCenterFactory hcFactory1=new HelpCenterFactory(articleDtoList);
//        model.addAttribute("helpCenter",hcFactory1.getListHelpCenter());
//        model.addAttribute("servicePromise",hcFactory1.getListServicePromise());
//        model.addAttribute("siteTerm",hcFactory1.getListSiteTerm());


        //版面图
        return view(activeLayout, "index/index", model);
    }


    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "resetPic")
    public CommonResult resetPic(Model model, HttpServletRequest request, HttpServletResponse response) {
        pictureAllFactory.resetPicMap();
        return new CommonResult(true);
    }


    @RequestMapping("nearProduct")
    public ModelAndView nearProduct(Model model) {
        ProductCriteria criteria = new ProductCriteria();
        criteria.setType(ProductType.Hotel);
        criteria.setStatus(ProductStatus.OnSale);
        criteria.setPageSize(3);
        PagedResult<ProductSimpleDto> pg = iProductService.findProductsSimple(criteria);
        if (pg != null) {
            model.addAttribute("hotels", pg.getItems());
        }

        criteria.setType(ProductType.Souvenirs);
        PagedResult<ProductSimpleDto> pg1 = iProductService.findProductsSimple(criteria);
        if (pg1 != null) {
            model.addAttribute("souvenirs", pg1.getItems());
        }

        criteria.setType(ProductType.Catering);

        PagedResult<ProductSimpleDto> pg2 = iProductService.findProductsSimple(criteria);
        if (pg2 != null) {
            model.addAttribute("caterings", pg2.getItems());
        }

        Map<Integer, List<PictureDto>> picMap = pictureAllFactory.getPicByLocation("nearProduct");

        if (picMap.get(2) != null)
            model.addAttribute("p_2", picMap.get(2));
        if (picMap.get(3) != null)
            model.addAttribute("p_3", picMap.get(3).get(0));
        if (picMap.get(4) != null)
            model.addAttribute("p_4", picMap.get(4).get(0));
        if (picMap.get(5) != null)
            model.addAttribute("p_5", picMap.get(5).get(0));
        return view(activeLayout, "periphery/index", model);
    }

    @RequestMapping("actives")
    public ModelAndView actives(Model model) {
        ActivityCriteria criteria = new ActivityCriteria();
        //正在进行的活动
        criteria.setActivityStatus(ActivityStatus.Run);
        criteria.setPageSize(4);
        PagedResult<ActivitySimpleDto> pg = activityService.findSimplePage(criteria);
        model.addAttribute("activtings", pg.getItems());
        //往期活动
        criteria.setActivityStatus(ActivityStatus.Complete);
        PagedResult<ActivitySimpleDto> pgt = activityService.findSimplePage(criteria);
        model.addAttribute("postActives", pgt.getItems());

        Map<Integer, List<PictureDto>> picMap = pictureAllFactory.getPicByLocation("nearProduct");

        if (picMap.get(2) != null)
            model.addAttribute("p_2", picMap.get(2));

        return view(activeLayout, "active/welcome", model);
    }

    @RequestMapping("recommend")
    public ModelAndView recommend(Model model, HttpServletRequest request, HttpServletResponse response) {

        //推荐门票
        ProductCriteria criteria = new ProductCriteria();
//    /    criteria.setType(ProductType.Ticket);
        criteria.setStatus(ProductStatus.OnSale);
        criteria.setType(ProductType.Ticket);
        criteria.setKind(ProductKind.Recommended);
        criteria.setPageSize(4);
        PagedResult<ProductSimpleDto> pg = iProductService.findProductsSimple(criteria);
        if (pg != null) {
            model.addAttribute("recommedTicket", pg.getItems());
        }
        //特价
        criteria.setPageSize(5);
        criteria.setKind(ProductKind.Special);
        PagedResult<ProductSimpleDto> pgt = iProductService.findProductsSimple(criteria);
        if (pgt != null) {
            model.addAttribute("specialTicket",pgt.getItems());
        }
        //热销

        criteria.setKind(ProductKind.HotSell);
        PagedResult<ProductSimpleDto> pgr = iProductService.findProductsSimple(criteria);
        if (pgr != null) {
            model.addAttribute("hotSellTicket", pgr.getItems());
        }

        //标准

        criteria.setPageSize(4);
        PagedResult<ProductSimpleDto> pgb = iProductService.findProductsSimple(criteria);
        if (pgb != null) {
            model.addAttribute("OnSaleTicket", pgb.getItems());
        }

        Map<Integer, List<PictureDto>> picMap = pictureAllFactory.getPicByLocation("recommend");

        if (picMap.get(2) != null)
            model.addAttribute("p_2", picMap.get(2));

        return view(activeLayout, "recommend/index", model);
    }

    /**
     * 搜索结果页
     *
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("search")
    public ModelAndView search(Model model, HttpServletRequest request, HttpServletResponse response) {
        ProductCriteria criteria = new ProductCriteria();
        if (!StringUtils.isNullOrWhiteSpace(request.getParameter("pageNumber"))) {
            criteria.setPageNumber(Integer.parseInt(request.getParameter("pageNumber")));
        }
        if (!StringUtils.isNullOrWhiteSpace(request.getParameter("name"))) {
            criteria.setName(request.getParameter("name"));
        }
        PagedResult<ProductSimpleDto> pagedResult = iProductService.findProductsSimple(criteria);
        model.addAttribute("pagedResult", pagedResult);
        return view(activeLayout, "searchlist/SearchList", model);
    }

}
