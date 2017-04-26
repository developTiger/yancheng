package com.sunesoft.seera.yc.webapp.controller;

import com.sunesoft.seera.fr.exceptions.ArgumentException;
import com.sunesoft.seera.fr.results.PagedResult;
import com.sunesoft.seera.yc.core.product.application.IProductService;
import com.sunesoft.seera.yc.core.product.application.dtos.FeedBackDto;
import com.sunesoft.seera.yc.core.product.application.dtos.ProductDto;
import com.sunesoft.seera.yc.core.product.domain.criteria.FeedBackCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Created by jade on 16/8/22.
 */
@Controller
public class TicketDetailController extends BaseController{

    @Autowired
    private IProductService iProductService;
    /**
     * 详情
     *
     * @param model
     * @return
     */
    @RequestMapping(value="/ticket/{id}.html")
    public ModelAndView ticket_detail_view(Model model, @PathVariable long id){

        //商品详情;
        Optional<ProductDto> productDtoOptional = Optional.ofNullable(iProductService.get(id));
        ProductDto productDto = productDtoOptional.orElseThrow(() -> new RuntimeException("查找的商品不存在"));
        model.addAttribute("productDto", productDto);
        //评价
        if (productDto != null) {
            FeedBackCriteria fbcriteria = new FeedBackCriteria();
            fbcriteria.setProductId(productDto.getId());
            PagedResult<FeedBackDto> pgf = iProductService.getProductFeeBacks(fbcriteria);
            if (pgf != null && pgf.getItems().size() > 0)
                model.addAttribute("feedBack", pgf.getItems());
        }
        return view("ticketdetail/index",model);
    }

    /**
     * 详情-商品
     *
     * @param model
     * @return
     */
    @RequestMapping(value="/ticket/goods.html")
    public ModelAndView ticket_goods_view(Model model){

        return view("ticketdetail/goods",model);
    }

    /**
     * 详情-交通
     *
     * @param model
     * @return
     */
    @RequestMapping(value="/ticket/traffic.html")
    public ModelAndView ticket_traffic_view(Model model){

        return view("ticketdetail/traffic",model);
    }

    /**
     * 详情-预定须知
     *
     * @param model
     * @return
     */
    @RequestMapping(value="/ticket/notice.html")
    public ModelAndView ticket_notice_view(Model model){

        return view("ticketdetail/notice",model);
    }

    /**
     * 详情-用户评论
     *
     * @param model
     * @return
     */
    @RequestMapping(value="/ticket/comment.html")
    public ModelAndView ticket_comment_view(Model model, HttpServletRequest request){

        FeedBackCriteria feedBackCriteria = new FeedBackCriteria();
        feedBackCriteria.setProductId(Long.valueOf(request.getParameter("id")));
        PagedResult<FeedBackDto> pr = iProductService.getProductFeeBacks(feedBackCriteria);

        model.addAttribute("feedBacks",pr.getItems());
        return view("ticketdetail/comment",model);
    }



}
