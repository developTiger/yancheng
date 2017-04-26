package com.sunesoft.seera.yc.webapp.controller.product;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.utils.JsonHelper;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.manager.application.IManagerService;
import com.sunesoft.seera.yc.core.manager.application.criteria.ManagerCriteria;
import com.sunesoft.seera.yc.core.product.application.IProductItemService;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductItemDto;
import com.sunesoft.seera.yc.core.product.domain.ProductItemType;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/8/5.
 */
@Controller
public class GoodsItemManagerController extends Layout {

    @Autowired
    IProductItemService iProductItemService;
    @Autowired
    IManagerService managerService;


    /**
     * 商品首页
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "sra_t_productItem")
    public ModelAndView index(Model model) {
        return view(layout, "product/productItem", model);
    }


    /**
     * 商品项页面数据展示 页面数据查询
     *
     * @param response
     * @param productItemCriteria
     * @param request
     */
    @RequestMapping(value = "ajax_goodsItem_query")
    public void queryGoodsListData(HttpServletResponse response, ProductItemCriteria productItemCriteria, HttpServletRequest request) {
        String name = request.getParameter("goodsItemName");
        String type = request.getParameter("typeList");
        if (!StringUtils.isNullOrWhiteSpace(type)) {

            if (type.equals("3")) {
                productItemCriteria.setType(ProductItemType.Ticket);
            }
            if (type.equals("4")) {
                productItemCriteria.setType(ProductItemType.Souvenirs);
            }
            if (type.equals("5")) {
                productItemCriteria.setType(ProductItemType.Hotel);
            }
            if (type.equals("6")) {
                productItemCriteria.setType(ProductItemType.Other);
            }
        }
        if (!StringUtils.isNullOrWhiteSpace(name)) {
            productItemCriteria.setName(URI.deURI(name));

        }

        PagedResult pagedResult = iProductItemService.findProductItems(productItemCriteria);
        String json = JsonHelper.toJson(pagedResult);
        AjaxResponse.write(response, json);
    }

    /**
     * 商品项 新增弹窗
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "_addGoodsItemForm")
    public ModelAndView addGoodsListManagerForm(Model model) {
        ManagerCriteria criteria = new ManagerCriteria();
        criteria.setStatus(true);
        criteria.setPageSize(Integer.MAX_VALUE);
        model.addAttribute("manager", managerService.findUser(criteria).getItems());
        return view("product/_addGoodsItemForm", model);
    }

    /**
     * 商品项 编辑弹窗
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "_editGoodsItemForm")
    public ModelAndView editGoodsListManagerForm(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (!StringUtils.isNullOrWhiteSpace(id)) {
            ProductItemDto productItemDto = iProductItemService.get(Long.parseLong(id));
//            ProductItemType productItemType = productItemDto.getProductItemType();
//            if (productItemType.name().equals("Ticket")) {
//                model.addAttribute("type", "3");
//            }
//            if (productItemType.name().equals("Souvenirs")) {
//                model.addAttribute("type", "4");
//            }
//            if (productItemType.name().equals("Hotel")) {
//                model.addAttribute("type", "5");
//            }
//            if (productItemType.name().equals("Other")) {
//                model.addAttribute("type", "6");
//            }
//            if (productItemType.name().equals("Catering")) {
//                model.addAttribute("type", "7");
//            }
            model.addAttribute("beans", productItemDto);

            ManagerCriteria criteria = new ManagerCriteria();
            criteria.setStatus(true);
            criteria.setPageSize(Integer.MAX_VALUE);
            PagedResult managers = managerService.findUser(criteria);
            if (null != managers)
                model.addAttribute("manager", managers.getItems());
        }
        return view("product/_editGoodsItemForm", model);
    }

    /**
     * 新增 提交
     *
     * @param request
     * @param productItemDto
     * @return
     */
    @RequestMapping(value = "ajax_add_update_goodsItem", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addOrUpdateGoodsItem(HttpServletRequest request, ProductItemDto productItemDto) {
        String id = request.getParameter("id");
        String innerManageId = request.getParameter("innerManageId");
        productItemDto.setInnerManageId(Long.parseLong(innerManageId));
        String type = StringUtils.isNullOrWhiteSpace(request.getParameter("hidType")) ?
                request.getParameter("itemType") ://新增 商品类目
                request.getParameter("hidType");//修改 商品类目

        productItemDto.setProductItemType(ProductItemType.valueOf(type));
        CommonResult commonResult = null;

        String price = request.getParameter("prices");
        productItemDto.setPrice(BigDecimal.valueOf(Double.parseDouble(price)));

        //id存在，修改；id不存在，新增
        if (!StringUtils.isNullOrWhiteSpace(id)) {
            productItemDto.setId(Long.parseLong(id));
//            if (hidType.equals(ProductItemType.Ticket.toString())) {
//                productItemDto.setProductItemType(ProductItemType.Ticket);
//            }
//            if (hidType.equals("4")) {
//                productItemDto.setProductItemType(ProductItemType.Souvenirs);
//            }
//            if (hidType.equals("5")) {
//                productItemDto.setProductItemType(ProductItemType.Hotel);
//            }
//            if (hidType.equals("6")) {
//                productItemDto.setProductItemType(ProductItemType.Other);
//            }
//            if (hidType.equals("7")) {
//                productItemDto.setProductItemType(ProductItemType.Catering);
//            }
            commonResult = iProductItemService.edit(productItemDto);
        } else {
//            if (type.equals("3")) {
//                productItemDto.setProductItemType(ProductItemType.Ticket);
//            }
//            if (type.equals("4")) {
//                productItemDto.setProductItemType(ProductItemType.Souvenirs);
//            }
//            if (type.equals("5")) {
//                productItemDto.setProductItemType(ProductItemType.Hotel);
//            }
//            if (type.equals("6")) {
//                productItemDto.setProductItemType(ProductItemType.Other);
//            }
//            if (type.equals("7")) {
//                productItemDto.setProductItemType(ProductItemType.Catering);
//            }
            commonResult = iProductItemService.create(productItemDto);
        }

        return commonResult;
    }

    /**
     * 商品项 页面 单个删除
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "ajax_delete_goodsItem")
    @ResponseBody
    public CommonResult deleteGoodsItem(HttpServletRequest request) {
        String id = request.getParameter("id");

        String[] ids = id.split(",");
        List<Long> list = new ArrayList<>();
        CommonResult commonResult;
        for (int i = 0; i < ids.length; i++) {
            list.add(Long.parseLong(ids[i]));
        }

        commonResult = iProductItemService.remove(list);
        return commonResult;
    }

    /**
     * 商品项 页面 批量删除
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "ajax_deleteGoodsItemsList")
    @ResponseBody
    public CommonResult deleteGoodsItemsList(HttpServletRequest request) {
        String id = request.getParameter("ids");
        String[] ids = id.split(",");
        List<Long> list = new ArrayList<>();
        for (String idGoodsItem : ids) {
            list.add(Long.parseLong(idGoodsItem));
        }
        CommonResult commonResult = iProductItemService.remove(list);
        return commonResult;
    }

}
