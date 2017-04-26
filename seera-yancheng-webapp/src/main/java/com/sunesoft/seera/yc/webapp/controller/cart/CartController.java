package com.sunesoft.seera.yc.webapp.controller.cart;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.product.application.IProductService;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductSimpleDto;
import com.sunesoft.seera.yc.core.shoppingCar.application.IShoppingCarService;
import com.sunesoft.seera.yc.core.shoppingCar.application.dto.ShoppingItemDto;
import com.sunesoft.seera.yc.webapp.controller.Layout;
import com.sunesoft.seera.yc.webapp.function.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bing on 16/8/18.
 */
@Controller
public class CartController extends Layout {

    @Autowired
    private IShoppingCarService shoppingCarService;

    @Autowired
    private UserSession userSession;

    @Autowired
    private IProductService iProductService;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 获取购物车商品项列表
     */
    @RequestMapping(value = "cart")
    public ModelAndView shoppingCart(Model model) {
        List<ShoppingItemDto> items = shoppingCarService.getTouristShoppingItems(userSession.getCurrentUserId());
        if (items != null)
            model.addAttribute("shoppingItems", items);
        else
            model.addAttribute("itemsError", "购物车里暂没商品，赶紧去添加");
        return view(activeLayout, "cart/cart", model);
    }

    /**
     * 增加购物车商品项
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "ajax_cart_addItem", method = RequestMethod.POST)
    public CommonResult addItem(HttpServletRequest request) throws Exception {
        String productId = request.getParameter("pid");
        if (org.apache.commons.lang.StringUtils.isEmpty(productId) || !org.apache.commons.lang.StringUtils.isNumeric(productId))
            return new CommonResult(false, "商品Id不能为空!");
        String num = request.getParameter("num");
        if (org.apache.commons.lang.StringUtils.isEmpty(num) || !org.apache.commons.lang.StringUtils.isNumeric(num))
            return new CommonResult(false, "商品数量不能为空!");
        String tourScheduleDate = request.getParameter("tsd");
        String hotelScheduleDate = request.getParameter("hsd");

        /*
         *新增 入园时间判断
         */
        if(StringUtils.isNullOrWhiteSpace(tourScheduleDate)){
            if(tourScheduleDate!=null) {
                if (StringUtils.isNullOrWhiteSpace(hotelScheduleDate)) {
                    return new CommonResult(false, "请选择时间");
                } else {
                    return new CommonResult(false, "请选择时间");
                }
            }
        }
        if(StringUtils.isNullOrWhiteSpace(hotelScheduleDate)){
            if(hotelScheduleDate!=null) {
                if(StringUtils.isNullOrWhiteSpace(tourScheduleDate)){
                    return new CommonResult(false,"请选择时间");
                }else{
                    return new CommonResult(false,"请选择时间");
                }
            }

        }

        Long pid = Long.parseLong(productId);
        Integer count = Integer.parseInt(num);
        ProductSimpleDto productSimpleDto = iProductService.getSimple(pid);
        if (productSimpleDto == null)
            return new CommonResult(false, "商品不存在!");
        if (count < 1)
            count = 1;
        ShoppingItemDto dto = new ShoppingItemDto();
        dto.setCount(count);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(tourScheduleDate))
            dto.setTourScheduleDate(simpleDateFormat.parse(tourScheduleDate));
        if (org.apache.commons.lang.StringUtils.isNotEmpty(hotelScheduleDate))
            dto.setHotelScheduleDate(simpleDateFormat.parse(hotelScheduleDate));

        dto.setTouristId(userSession.getCurrentUserId());
        dto.setProductId(pid);
        return shoppingCarService.addItem(dto);
    }

    /**
     * 单个移除购物车商品项数量
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "ajax_cart_singleRemoveCount/{id}")
    public CommonResult singleRemoveCount(@PathVariable Long id) throws Exception {
        if (id == null) return new CommonResult(false, "输入的ids有误");
        return shoppingCarService.removeItem(id);
    }

    /**
     * 批量移除购物车商品项数量
     *
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "ajax_sc_removeItemCount")
    public CommonResult removeCount(HttpServletRequest httpServletRequest) throws Exception {
        String id = httpServletRequest.getParameter("ids");//保证ids是一个字符串
        if (StringUtils.isNullOrWhiteSpace(id))
            return new CommonResult(false, "未选中购物车商品");
        String[] ids = id.split(",");
        if (ids == null || ids.length < 1) return new CommonResult(false, "未选中购物车商品");
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            if (!StringUtils.isNullOrWhiteSpace(ids[i]))
                list.add(Long.parseLong(ids[i]));
        }
        return shoppingCarService.removeItems(list);
    }

    /**
     * 清空购物车
     *
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "ajax_cart_clearShoppingCar")
    public CommonResult clearShoppingCar(HttpServletRequest httpServletRequest) throws Exception {
        Long touristId = userSession.getCurrentUser(httpServletRequest).getId();
        if (touristId == null) return new CommonResult(false, "输入的touristId有误");
        //页面无清空功能操作
        return shoppingCarService.clearShoppingCar(touristId);
    }

    //region 不用的代码
    //    /**
//     * 增加购物车商品项数量
//     *
//     * @return
//     * @throws Exception
//     */
//    @ResponseBody
//    @RequestMapping(value = "ajax_cart_plusItemCount/{pid}/{count}")
//    @Deprecated
//    public CommonResult increaseCount(@PathVariable Long pid, @PathVariable Integer count) throws Exception {
//        ProductSimpleDto productSimpleDto = iProductService.getSimple(pid);
//        if (productSimpleDto == null)
//            return new CommonResult(false, "商品不存在!");
//        if (count < 1)
//            count = 1;
//        return shoppingCarService.increaseItemCount(pid, count);
//    }


//    /**
//     * 减少购物车商品项数量
//     *
//     * @return
//     * @throws Exception
//     */
//    @ResponseBody
//    @RequestMapping(value = "ajax_cart_reduceItemCount/{pid}/{count}")
//    @Deprecated
//    public CommonResult reduceCount(@PathVariable Long pid, @PathVariable Integer count) throws Exception {
//        ProductSimpleDto productSimpleDto = iProductService.getSimple(pid);
//        if (productSimpleDto == null)
//            return new CommonResult(false, "商品不存在!");
//        if (count < 1)
//            count = 1;
//        return shoppingCarService.increaseItemCount(pid, count);
//    }
    //endregion

    /**
     * 设置购物车商品项数量
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "ajax_cart_setItemCount/{pid}/{count}")
    public CommonResult setCount(@PathVariable Long pid, @PathVariable Integer count) throws Exception {
        if (pid == null) return new CommonResult(false, "未指明商品");
        if (count == null || count < 1) return new CommonResult(false, "商品数量有误");
        return shoppingCarService.setItemCount(pid, count);
    }

}
