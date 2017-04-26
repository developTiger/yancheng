package com.sunesoft.seera.yc.webapp.controller.order;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.JsonHelper;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.fr.utils.excel.ExpotExcel;
import com.sunesoft.seera.yc.core.order.application.IOrderService;
import com.sunesoft.seera.yc.core.order.application.criteria.OrderCriteria;
import com.sunesoft.seera.yc.core.order.application.criteria.OrderProductItemCriteria;
import com.sunesoft.seera.yc.core.order.application.dtos.OrderDownLoadDto;
import com.sunesoft.seera.yc.core.order.application.dtos.OrderDto;
import com.sunesoft.seera.yc.core.order.application.dtos.OrderProductItemDowLoadDto;
import com.sunesoft.seera.yc.core.order.application.dtos.OrderProductItemDto;
import com.sunesoft.seera.yc.core.product.application.IProductItemService;
import com.sunesoft.seera.yc.core.product.application.IProductService;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductItemSimpleDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/7/21.
 */
@Controller
public class OrderManagerController extends Layout {

    @Autowired
    IOrderService iOrderService;

    @Autowired
    IProductService iProductService;

    @Autowired
    IProductItemService iProductItemService;

    /**
     * 订单商品项页面呈现
     * @param model
     * @return
     */
    @RequestMapping(value = "sra_o_item")
    public ModelAndView sra_o_item(Model model) {

        List<ProductItemSimpleDto> list=iProductItemService.getProductItemSimpleDtos();
        model.addAttribute("listItem",list);
        return view(layout, "order/orderItemIndex", model);
    }

    /**
     * 商品项数据加载
     * @param response
     * @param orderCriteria
     */
    @RequestMapping(value = "ajax_orderProductItem_query_list")
    public void ajax_orderManager_query_list(HttpServletRequest request,HttpServletResponse response, OrderProductItemCriteria orderCriteria) {

        String productName= URI.deURI(request.getParameter("productItemName"));
        String begin_Date=request.getParameter("begin_Date");
        String end_Date=request.getParameter("end_Date");

        String productStatus=request.getParameter("product_status");

        if(!StringUtils.isNullOrWhiteSpace(productStatus)){
            orderCriteria.setProduct_status(productStatus);
        }

        if(!StringUtils.isNullOrWhiteSpace(begin_Date)){
            orderCriteria.setFromOrderTime(DateHelper.parse(begin_Date + " 00:00:00","yyyy-MM-dd HH:mm:ss"));
        }

        if(!StringUtils.isNullOrWhiteSpace(end_Date)){
            orderCriteria.setEndOrderTime(DateHelper.parse(end_Date + " 23:59:59","yyyy-MM-dd HH:mm:ss"));
        }

        orderCriteria.setProductItemName(productName.trim());
        PagedResult pagedResult = iOrderService.getOrderProductItems(orderCriteria);
        String json = JsonHelper.toJson(pagedResult);
        AjaxResponse.write(response, json);
    }


    /**
     * 订单列表页呈现
     * @param model
     * @return
     */
    @RequestMapping(value = "sra_t_o")
    public ModelAndView index(Model model) {
        Map<Integer,String> map=iProductService.getAllProvices();
        model.addAttribute("map",map);
        return view(layout, "order/index", model);
    }

    /**
     * 订单列表页呈现
     * @param model
     * @return
     */
    @RequestMapping(value = "sra_t_meal")
    public ModelAndView meal(Model model) {
        return view(layout, "order/meal", model);
    }

    /**
     * 订单列表数据加载
     * @param response
     * @param orderCriteria
     */
    @RequestMapping(value = "ajax_orderManager_query_list")
    public void queryData(HttpServletRequest request,HttpServletResponse response, OrderCriteria orderCriteria) {
        String limitAreas=request.getParameter("limitAreas");
        if(!StringUtils.isNullOrWhiteSpace(limitAreas)){
            orderCriteria.setRejectArea(limitAreas);
        }
        String productName= URI.deURI(request.getParameter("productName"));
        String begin_Date=request.getParameter("begin_Date");
        String end_Date=request.getParameter("end_Date");

        if(!StringUtils.isNullOrWhiteSpace(begin_Date)){
            orderCriteria.setFromOrderTime(DateHelper.parse(begin_Date + " 00:00:00","yyyy-MM-dd HH:mm:ss"));
        }

        if(!StringUtils.isNullOrWhiteSpace(end_Date)){
            orderCriteria.setEndOrderTime(DateHelper.parse(end_Date + " 23:59:59","yyyy-MM-dd HH:mm:ss"));
        }

        orderCriteria.setProductName(productName.trim());
        PagedResult pagedResult = iOrderService.getOrders(orderCriteria);
        String json = JsonHelper.toJson(pagedResult);
        AjaxResponse.write(response, json);
    }

    /**
     * 订单商品项列表数据加载
     * @param response
     * @param orderCriteria
     */
    @RequestMapping(value = "sra_downloadData2")
    public void sra_downloadData2(HttpServletRequest request,HttpServletResponse response, OrderProductItemCriteria orderCriteria) {

        String productName= URI.deURI(request.getParameter("productItemName"));
        String begin_Date=request.getParameter("begin_Date");
        String end_Date=request.getParameter("end_Date");

        if(!StringUtils.isNullOrWhiteSpace(begin_Date)){
            orderCriteria.setFromOrderTime(DateHelper.parse(begin_Date + " 00:00:00","yyyy-MM-dd HH:mm:ss"));
        }

        if(!StringUtils.isNullOrWhiteSpace(end_Date)){
            orderCriteria.setEndOrderTime(DateHelper.parse(end_Date + " 23:59:59","yyyy-MM-dd HH:mm:ss"));
        }
        orderCriteria.setProductItemName(productName);

        orderCriteria.setPageSize(60000);
        PagedResult pagedResult = iOrderService.getOrderProductDownloadItems(orderCriteria);
        List<OrderProductItemDowLoadDto> result = pagedResult.getItems();
        ExpotExcel<OrderProductItemDowLoadDto> expotExcel = new ExpotExcel<>();
        String[] header = new String[]{"订单编号", "销售时间", "商品项名称", "商品项编号","商品项类型", "商品项单价","商品项数量","使用状态","商品项使用时间"};
        expotExcel.doExportExcel("商品项信息", header, result, "yyyy-MM-dd hh:mm:ss", response);

    }


    /**
     * 订单列表数据加载
     * @param response
     * @param orderCriteria
     */
    @RequestMapping(value = "sra_downloadData")
    public void downloadDatas(HttpServletRequest request,HttpServletResponse response, OrderCriteria orderCriteria) {

        String productName= URI.deURI(request.getParameter("productName"));
        String begin_Date=request.getParameter("begin_Date");
        String end_Date=request.getParameter("end_Date");

        if(!StringUtils.isNullOrWhiteSpace(begin_Date)){
            orderCriteria.setFromOrderTime(DateHelper.parse(begin_Date + " 00:00:00","yyyy-MM-dd HH:mm:ss"));
        }

        if(!StringUtils.isNullOrWhiteSpace(end_Date)){
            orderCriteria.setEndOrderTime(DateHelper.parse(end_Date + " 23:59:59","yyyy-MM-dd HH:mm:ss"));
        }
        orderCriteria.setProductName(productName.trim());
        orderCriteria.setPageSize(60000);
        List<OrderDownLoadDto> result = iOrderService.getOrdersDownload(orderCriteria);
        ExpotExcel<OrderDownLoadDto> expotExcel = new ExpotExcel<>();
        String[] header = new String[]{"订单编号", "用户名", "订单商品", "状态","总价", "下单时间","支付方式","优惠券信息","取票人","取票人电话"};
        expotExcel.doExportExcel("订单信息", header, result, "yyyy-MM-dd hh:mm:ss", response);

    }

    @RequestMapping(value="ajax_orderMeal_query_list")
    public void ajax_orderMeal_query_list(HttpServletRequest request,HttpServletResponse response,OrderCriteria orderCriteria){

        String productName= URI.deURI(request.getParameter("productName"));
        String begin_Date=request.getParameter("begin_Date");
        String end_Date=request.getParameter("end_Date");

        if(!StringUtils.isNullOrWhiteSpace(begin_Date)){
            orderCriteria.setFromOrderTime(DateHelper.parse(begin_Date + " 00:00:00","yyyy-MM-dd HH:mm:ss"));
        }

        if(!StringUtils.isNullOrWhiteSpace(end_Date)){
            orderCriteria.setEndOrderTime(DateHelper.parse(end_Date + " 23:59:59","yyyy-MM-dd HH:mm:ss"));
        }

        orderCriteria.setProductName(productName.trim());

        PagedResult pagedResult=iOrderService.getMealOrdersProduct(orderCriteria);
        String json = JsonHelper.toJson(pagedResult);
        AjaxResponse.write(response, json);
    }

    /**
     * 订单详情查看页
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "orderInfo")
    public ModelAndView orderListDetail(Model model,HttpServletRequest request) {
        String id = request.getParameter("id");
        if(!StringUtils.isNullOrWhiteSpace(id)){
            OrderDto dto = iOrderService.getOrder(Long.parseLong(id));
            model.addAttribute("order", dto);

        }
        else{
            String orderNum = request.getParameter("num");
            OrderDto dto = iOrderService.getOrder(orderNum);
            model.addAttribute("order", dto);
        }
        return view(layout, "order/orderInfo", model);
    }

    @RequestMapping(value = "mealOrderInfo")
    public ModelAndView mealOrderInfo(Model model,HttpServletRequest request) {
        String id = request.getParameter("id");
        OrderDto dto = iOrderService.getOrder(id);
        model.addAttribute("order", dto);
        return view(layout, "order/mealOrderInfo", model);
    }

    @RequestMapping(value = "_orderMealAuditing")
    public ModelAndView orderMealAuditingForm(Model model,HttpServletRequest request) {
        String orderNum =request.getParameter("orderNum");
        String orderProductId = request.getParameter("opd");
        if (!StringUtils.isNullOrWhiteSpace(orderProductId)) {
            UniqueResult result = iOrderService.getOrderProduct(Long.parseLong(orderProductId));
            model.addAttribute("bean", result.getT());
            model.addAttribute("orderNum",orderNum);
        }
        return view("order/_orderMealAuditing", model);
    }

    @RequestMapping(value = "ajax_submit_meal",method=RequestMethod.POST)
    @ResponseBody
    public CommonResult ajax_submit_meal(Model model,HttpServletRequest request) {

        String orderNum = request.getParameter("orderNum");
        String orderProductId = request.getParameter("id");
        if (!StringUtils.isNullOrWhiteSpace(orderProductId)) {
           return iOrderService.mealCheck(orderNum,Long.parseLong(orderProductId),true);
        }
        return new CommonResult(false,"商品编号不存在");
    }

    @RequestMapping(value = "ajax_rejects_meal",method=RequestMethod.POST)
    @ResponseBody
    public CommonResult ajax_rejects_meal(Model model,HttpServletRequest request) {
        String orderNum = request.getParameter("orderNum");
        String orderProductId = request.getParameter("id");
        if (!StringUtils.isNullOrWhiteSpace(orderProductId)) {
            return iOrderService.mealCheck(orderNum,Long.parseLong(orderProductId),false);
        }
        return new CommonResult(false,"商品编号不存在");
    }

    /**
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "ajax_itemMealCheck", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult MealCheck(HttpServletRequest request) throws Exception {
        String orderNum = request.getParameter("orderNum");
        String orderProductId = request.getParameter("opd");
        String isCheck = request.getParameter("check");
        if (StringUtils.isNullOrWhiteSpace(orderProductId) || StringUtils.isNullOrWhiteSpace(isCheck))
            return ResultFactory.commonError("改签参数错误");
        return iOrderService.mealCheck(orderNum,Long.parseLong(orderProductId), Boolean.parseBoolean(isCheck));
    }

//    /**
//     * @param request
//     * @throws Exception
//     */
//    @RequestMapping(value = "ajax_orderReturn", method = RequestMethod.POST)
//    @ResponseBody
//    public CommonResult itemReturn(HttpServletRequest request) throws Exception {
//        String orderProductId = request.getParameter("opd");
//        if (StringUtils.isNullOrWhiteSpace(orderProductId))
//            return ResultFactory.commonError("改签参数错误");
//        return iOrderService.returnApprove(Long.parseLong(orderProductId));
//    }


    @RequestMapping(value = "order/mealRedo")
    @ResponseBody
    public CommonResult mealRedo(HttpServletRequest request, HttpServletResponse response) {

        String zybCode = request.getParameter("zybCode");

        CommonResult result = iOrderService.ticketZybMeal(zybCode);


        return result;
    }


    @RequestMapping(value = "ajax_getImg")
    @ResponseBody
    public CommonResult getImg(HttpServletRequest request, HttpServletResponse response) {
        String orderNum = request.getParameter("orderNum");
        String productId = request.getParameter("productId");
        CommonResult result = iOrderService.reGetZybImg(orderNum, Long.parseLong(productId));
        return result;
    }


    @RequestMapping(value = "ajax_Retry_zyb_ticket")
    @ResponseBody
    public CommonResult ajax_Retry_zyb_ticket(HttpServletRequest request, HttpServletResponse response) {
        String orderNum = request.getParameter("orderNum");
        String productId = request.getParameter("productId");
        CommonResult result = iOrderService.reCreateTicketByProduct(orderNum, Long.parseLong(productId));
        return result;
    }

    @RequestMapping(value = "ajax_orderReturn")
    @ResponseBody
    public CommonResult productReturn(HttpServletRequest request, HttpServletResponse response) {

        String orderNum = request.getParameter("orderNum");
        String productIds = request.getParameter("productIds");

        List<Long> idList = new ArrayList<>();
        String[] ids = productIds.split(",");
        for (String id : ids) {
            idList.add(Long.parseLong(id));
        }
        CommonResult result = iOrderService.ticketChargeBack(orderNum, idList);
        return result;
    }

    @RequestMapping(value = "ajax_cancelOrder")
    @ResponseBody
    public CommonResult cancelOrder(HttpServletRequest request, HttpServletResponse response) {

        String orderId = request.getParameter("orderId");
        if (!StringUtils.isNullOrWhiteSpace(orderId)) {
            CommonResult result = iOrderService.cancelOrder(Long.parseLong(orderId));
            return result;
        }
        return ResultFactory.commonError("订单异常取消失败");
    }
}
