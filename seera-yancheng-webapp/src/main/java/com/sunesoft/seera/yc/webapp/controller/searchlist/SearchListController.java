package com.sunesoft.seera.yc.webapp.controller.searchlist;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.product.application.IProductItemService;
import com.sunesoft.seera.yc.core.product.application.IProductService;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductDto;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductSimpleDto;
import com.sunesoft.seera.yc.core.product.domain.ProductKind;
import com.sunesoft.seera.yc.core.product.domain.ProductStatus;
import com.sunesoft.seera.yc.core.product.domain.ProductType;
import com.sunesoft.seera.yc.core.product.domain.criteria.ProductCriteria;
import com.sunesoft.seera.yc.webapp.controller.Layout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;


@Controller
public class SearchListController extends Layout {

    @Autowired
    IProductItemService iProductItemService;

    @Autowired
    IProductService iProductService;

    @RequestMapping(value="searchlist")
    public ModelAndView searchlist(Model model,HttpServletRequest request,HttpServletResponse response){
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String searchType=request.getParameter("searchType");

        String searchContent= request.getParameter("searchContent");
        if(!StringUtils.isNullOrWhiteSpace(searchContent))
            try {
                searchContent=new String(searchContent.getBytes("ISO8859_1"),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        String toPage=request.getParameter("toPage");
        ProductCriteria criteria=new ProductCriteria();
        if(!StringUtils.isNullOrWhiteSpace(searchType)) {
            criteria.setType(ProductType.valueOf(searchType));
        }
        criteria.setName(searchContent);
        if(StringUtils.isNullOrWhiteSpace(toPage)){
            criteria.setPageNumber(1);
        }else{
            criteria.setPageNumber(Integer.parseInt(toPage));
        }
        String kind=request.getParameter("kind");
        if(!StringUtils.isNullOrWhiteSpace(kind)){
            criteria.setKind(ProductKind.valueOf(kind));
            model.addAttribute("kind",kind);
        }
        criteria.setStatus(ProductStatus.OnSale);
       // PagedResult<ProductSimpleDto> pagedResult=  iProductService.findProductsSimple(criteria);
        PagedResult<ProductDto> pagedResult=  iProductService.findProducts(criteria);
        model.addAttribute("pagedResult",pagedResult);


        ProductCriteria productCriteria=new ProductCriteria();
        productCriteria.setStatus(ProductStatus.OnSale);
        criteria.setKind(ProductKind.Recommended);
        productCriteria.setPageSize(6);
        productCriteria.setOrderByProperty("onSaleTime");
        productCriteria.setAscOrDesc(false);
        //推荐商品
        PagedResult<ProductSimpleDto> productSimpleDtoPagedResult = iProductService.findProductsSimple(productCriteria);
        if (productSimpleDtoPagedResult != null) {
            model.addAttribute("recommendedProducts", productSimpleDtoPagedResult.getItems());
        }
        if(searchType==null){
            model.addAttribute("searchType","门票");
            return view(activeLayout,"searchlist/SearchList",model);
        }
        switch (searchType){
            case "Ticket":
                model.addAttribute("searchType","门票");
                break;
            case "Catering":
                model.addAttribute("searchType","餐饮");
                break;
            case "Souvenirs":
                model.addAttribute("searchType","纪念品");
                break;
            case "Hotel":
                model.addAttribute("searchType","酒店");
                break;
            case "Other":
                model.addAttribute("searchType","其他");
                break;
        }
        return view(activeLayout,"searchlist/SearchList",model);
    }




}
