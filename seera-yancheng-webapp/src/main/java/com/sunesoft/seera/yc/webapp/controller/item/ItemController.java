package com.sunesoft.seera.yc.webapp.controller.item;

import com.sunesoft.seera.fr.exceptions.ArgumentException;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.order.application.IOrderService;
import com.sunesoft.seera.yc.core.product.application.IProductService;
import com.sunesoft.seera.yc.core.product.application.dtos.FeedBackDto;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductDto;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductSimpleDto;
import com.sunesoft.seera.yc.core.product.domain.ProductCT;
import com.sunesoft.seera.yc.core.product.domain.ProductStatus;
import com.sunesoft.seera.yc.core.product.domain.ProductType;
import com.sunesoft.seera.yc.core.product.domain.criteria.FeedBackCriteria;
import com.sunesoft.seera.yc.core.product.domain.criteria.ProductCriteria;
import com.sunesoft.seera.yc.core.shoppingCar.application.dto.ShoppingItemDto;
import com.sunesoft.seera.yc.webapp.controller.Layout;
import com.sunesoft.seera.yc.webapp.function.UserSession;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class ItemController extends Layout {

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private UserSession userSession;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @RequestMapping(value = "item/{pid}")
    public ModelAndView productdetails(Model model, @PathVariable Long pid) {
        //商品详情
        if (pid == null)
            throw new ArgumentException("查找的商品不存在");
        ProductDto productDto2=iProductService.get(pid);



        Optional<ProductDto> productDtoOptional = Optional.ofNullable(iProductService.get(pid));
        ProductDto productDto = productDtoOptional.orElseThrow(() -> new RuntimeException("查找的商品不存在"));

        if(productDto.getCtEndDate()!=null) {
            if(!productDto.getProductCt().equals(ProductCT.Today)) {
                if (productDto.getCtEndDate().getTime() < new Date().getTime()) {
                    model.addAttribute("disabledButton", true);
                }
            }
        }
        if(productDto.getProductCt()!=null&&productDto.getProductCt().equals(ProductCT.Today)){
            productDto.setCtBeginDate(new Date());
        }
        model.addAttribute("productDto", productDto);
        //推荐门票

        ProductCriteria criteria = new ProductCriteria();
        criteria.setStatus(ProductStatus.OnSale);
        criteria.setType(ProductType.Ticket);
        PagedResult<ProductDto> pg = iProductService.findProducts(criteria);
        if (pg != null && pg.getItems().size() > 0) {
            model.addAttribute("recommend", pg.getItems());
        }

        //评价

        if (productDto != null) {
            FeedBackCriteria fbcriteria = new FeedBackCriteria();
            fbcriteria.setProductId(productDto.getId());
            PagedResult<FeedBackDto> pgf = iProductService.getProductFeeBacks(fbcriteria);
            if (pgf != null && pgf.getItems().size() > 0)
                model.addAttribute("feedBack", pgf.getItems());
        }

        return view(activeLayout, "item/item", model);
    }

    @RequestMapping(value="toItem")
    public String itemTest(HttpServletRequest request){
        String productNum=request.getParameter("productNum");
        ProductDto productDto=iProductService.get(productNum);
        return "redirect:item/"+productDto.getId();
    }

    @ResponseBody
    @RequestMapping(value = "ajax_reservation", method = RequestMethod.POST)
    public CommonResult reservation(HttpServletRequest request) throws ParseException {
        String productId = request.getParameter("pid");
        if (StringUtils.isEmpty(productId) || !StringUtils.isNumeric(productId))
            return new CommonResult(false, "商品Id不能为空!");
        String num = request.getParameter("num");
        if (StringUtils.isEmpty(num) || !StringUtils.isNumeric(num)) return new CommonResult(false, "商品数量不能为空!");

        String tourScheduleDate = request.getParameter("tsd");
        String hotelScheduleDate = request.getParameter("hsd");

          /*
         *新增 入园时间判断
         */
        if(com.sunesoft.seera.fr.utils.StringUtils.isNullOrWhiteSpace(tourScheduleDate)){
            if(com.sunesoft.seera.fr.utils.StringUtils.isNullOrWhiteSpace(hotelScheduleDate)){
                return new CommonResult(false,"请选择时间");
            }
        }
        if(com.sunesoft.seera.fr.utils.StringUtils.isNullOrWhiteSpace(hotelScheduleDate)){
            if(com.sunesoft.seera.fr.utils.StringUtils.isNullOrWhiteSpace(tourScheduleDate)){
                return new CommonResult(false,"请选择时间");
            }
        }

        long pid = Long.parseLong(productId);
        ProductSimpleDto productSimpleDto = iProductService.getSimple(pid);
        if (productSimpleDto != null) {
            ShoppingItemDto shoppingItemDto = new ShoppingItemDto();
            shoppingItemDto.setProductName(productSimpleDto.getName());
            shoppingItemDto.setPrice(productSimpleDto.getDiscountPrice());
            shoppingItemDto.setSpecDescription(productSimpleDto.getSpecDescription());
            shoppingItemDto.setStock(productSimpleDto.getStock());
            shoppingItemDto.setTouristId(userSession.getCurrentUserId());
            shoppingItemDto.setTouristName(userSession.getCurrentUser().getName());
            shoppingItemDto.setProductId(Long.parseLong(productId));
            shoppingItemDto.setCount(Integer.parseInt(num));
            if (StringUtils.isNotEmpty(tourScheduleDate)) {
                shoppingItemDto.setTourScheduleDate(simpleDateFormat.parse(tourScheduleDate));
            }
            if (StringUtils.isNotEmpty(hotelScheduleDate)) {
                shoppingItemDto.setHotelScheduleDate(simpleDateFormat.parse(hotelScheduleDate));
            }
            List<ShoppingItemDto> shoppingItemDtos = new ArrayList<>();
            shoppingItemDtos.add(shoppingItemDto);
            userSession.getCurrentSession().setAttribute("shoppingItemDtos", shoppingItemDtos);
        }
        return new CommonResult(true, "添加成功!");
    }

}
