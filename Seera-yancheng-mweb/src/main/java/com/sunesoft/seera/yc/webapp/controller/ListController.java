package com.sunesoft.seera.yc.webapp.controller;

import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.activity.application.IActivityService;
import com.sunesoft.seera.yc.core.activity.application.dtos.ActivityDto;
import com.sunesoft.seera.yc.core.activity.application.dtos.ActivitySimpleDto;
import com.sunesoft.seera.yc.core.activity.domain.ActivityStatus;
import com.sunesoft.seera.yc.core.activity.domain.creteria.ActivityCriteria;
import com.sunesoft.seera.yc.core.product.application.IProductService;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductSimpleDto;
import com.sunesoft.seera.yc.core.product.domain.ProductStatus;
import com.sunesoft.seera.yc.core.product.domain.ProductType;
import com.sunesoft.seera.yc.core.product.domain.criteria.ProductCriteria;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by jade on 16/8/18.
 */
@Controller
public class ListController extends BaseController {


    @Autowired
    private IProductService iProductService;
    @Autowired
    private IActivityService activityService;

    /**
     * 门票列表
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list/ticket/{type}.html")
    public ModelAndView list_ticket_view(@PathVariable String type, Model model) {

        //推荐门票
        ProductCriteria criteria = new ProductCriteria();
        criteria.setType(ProductType.Ticket);
        criteria.setStatus(ProductStatus.OnSale);
        PagedResult<ProductSimpleDto> pg = iProductService.findProductsSimple(criteria);
        if (pg != null) {
            model.addAttribute("recommedTicket", pg.getItems());
        }
        return view("list/ticket", model);
    }

    /**
     * 周边列表
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list/periphery.html")
    public ModelAndView list_periphery_view(Model model, HttpServletRequest request) {

        ProductCriteria criteria = new ProductCriteria();
        criteria.setStatus(ProductStatus.OnSale);
        criteria.setType(ProductType.Hotel);
        criteria.setPageSize(100);
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
        return view("list/periphery", model);
    }


    /**
     * 活动(抢购)
     *
     * @param type
     * @param model
     * @return
     */
    @RequestMapping(value = "/list/activity/{type}.html")
    public ModelAndView list_activity_view(@PathVariable String type, Model model) {
        ActivityCriteria activityCriteria = new ActivityCriteria();
        if (StringUtils.equals("1", type)) {
            activityCriteria.setActivityStatus(ActivityStatus.Run);
        } else {
            activityCriteria.setActivityStatus(ActivityStatus.Complete);
        }

        PagedResult<ActivitySimpleDto> pr = activityService.findSimplePage(activityCriteria);
        if (pr != null) {
            model.addAttribute("list", pr.getItems());
        }

        model.addAttribute("type", type);
        return view("list/activity", model);
    }

    /**
     * 组合商品
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list/groupgoods.html")
    public ModelAndView groupGoods(Model model) {
        ProductCriteria criteria = new ProductCriteria();
        criteria.setStatus(ProductStatus.OnSale);
        criteria.setType(ProductType.GroupProduct);
        criteria.setPageSize(100);
        PagedResult<ProductSimpleDto> pg = iProductService.findProductsSimple(criteria);
        if (pg != null) {
            model.addAttribute("groupgoods", pg.getItems());
        }
        return view("list/groupgoods", model);
    }

    /**
     * 组合商品
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list/nomalticket.html")
    public ModelAndView nomalTicket(Model model) {
        ProductCriteria criteria = new ProductCriteria();
        criteria.setStatus(ProductStatus.OnSale);
        criteria.setType(ProductType.Ticket);
        criteria.setPageSize(100);
        PagedResult<ProductSimpleDto> pg = iProductService.findProductsSimple(criteria);
        if (pg != null) {
            model.addAttribute("tickets", pg.getItems());
        }
        return view("list/ticket", model);
    }
}
