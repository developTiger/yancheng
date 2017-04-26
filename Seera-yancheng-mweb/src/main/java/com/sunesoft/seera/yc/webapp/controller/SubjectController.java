package com.sunesoft.seera.yc.webapp.controller;

import com.google.gson.Gson;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.fr.utils.Configs;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.coupon.application.ICouponService;
import com.sunesoft.seera.yc.core.coupon.application.ICouponTypeService;
import com.sunesoft.seera.yc.core.coupon.application.dto.CouponDto;
import com.sunesoft.seera.yc.core.coupon.application.dto.CouponTypeDto;
import com.sunesoft.seera.yc.core.fileSys.FileService;
import com.sunesoft.seera.yc.core.lottery.application.SubjectService;
import com.sunesoft.seera.yc.core.lottery.application.dtos.SubjectDto;
import com.sunesoft.seera.yc.core.order.application.IOrderService;
import com.sunesoft.seera.yc.core.order.application.criteria.CreateOrder;
import com.sunesoft.seera.yc.core.order.application.criteria.CreateOrderProduct;
import com.sunesoft.seera.yc.core.order.domain.OrderInfo;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.UserSessionDto;
import com.sunesoft.seera.yc.webapp.function.Wx_Access;
import com.sunesoft.seera.yc.webapp.model.Light;
import com.sunesoft.seera.yc.webapp.model.LightResult;
import com.sunesoft.seera.yc.webapp.model.SubjectAllFactory;
import com.sunesoft.seera.yc.webapp.model.WxTicketInfo;
import com.sunesoft.seera.yc.webapp.wx.UserInfo;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zhouz on 2016/8/23.
 */
@Controller
public class SubjectController extends BaseController {
    @Autowired
    Wx_Access wx_access;
    @Autowired
    SubjectService subjectService;

    @Autowired
    ICouponTypeService iCouponTypeService;

    @Autowired
    ICouponService iCouponService;

    @Autowired
    SubjectAllFactory subjectAllFactory;

    @Autowired
    IOrderService iOrderService;

    @RequestMapping(value = "/light/{number}")
    public ModelAndView lotteryindex(Model model, HttpServletRequest request, @PathVariable String number) {

        if(us.getCurrentUser()==null){
           return new ModelAndView("redirect:"+us.authur(request.getRequestURI()));
        }

        Light light = subjectAllFactory.getLight(Integer.parseInt(number));
        if (!light.getSubjectDto().getStatus()) {
            return view("/lottery/showed", model);
        }
        model.addAttribute("number", number);
        return view("/lottery/lotteryindex", model);
    }

    @RequestMapping(value = "/lottery/{number}")
    public ModelAndView lottery(Model model, HttpServletRequest request, @PathVariable String number) throws IOException {
        if(us.getCurrentUser()==null){
            return new ModelAndView("redirect:"+us.authur(request.getRequestURI()));
        }
        UserInfo userInfo =this.getUI();
        if("0".equals(userInfo.getSubscribe())){
            return new ModelAndView("redirect:/erweima");
        }

        Light light = subjectAllFactory.getLight(Integer.parseInt(number));
        if (!light.getSubjectDto().getStatus()) {
            return view("/lottery/showed", model);
        }
        List<SubjectDto> list = subjectAllFactory.getMapSub1(Integer.parseInt(number));
        light.setList(list);
        model.addAttribute("subject0", light.getSubjectDto());
        model.addAttribute("subject", light.getList());
        model.addAttribute("num", number);
        return view("/lottery/subject", model);
    }

    @RequestMapping(value = "erweima")
    public ModelAndView erweima(Model model,HttpServletRequest request){

        return view("/lottery/erweima",model);
    }

    private UserInfo getUI() throws IOException {
        WxTicketInfo   wxTicketInfo=wx_access.getticket();
        String accessTokenUrl = Configs.getProperty("seera.wx.oauth.cgui.url");
        String appId = Configs.getProperty("seera.wx.oauth.appid");
        String secret = Configs.getProperty("seera.wx.oauth.secret");
        Connection connection = Jsoup.connect(String.format(accessTokenUrl, wxTicketInfo.getAccess_token(), us.getCurrentOpenId()));
        Document document = connection.ignoreContentType(true).get();

        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(document.text(), UserInfo.class);
        return userInfo;
    }

    //
//    @RequestMapping(value="/ajax_subject_result",method = RequestMethod.POST)
//    @ResponseBody
    private CouponTypeDto getCouponId(Integer count) {
//        String typeId=request.getParameter("typeId");
//        String count=request.getParameter("count");
        //Boolean checked=subjectService.checkStatus(Long.parseLong(typeId));
//        LightResult lg=new LightResult();
//        if(checked && count.equals("5")) {
//            CommonResult result = subjectService.updateStatus(Long.parseLong(typeId));
//            subjectAllFactory.clear();
//            if(result.getIsSuccess()){
//                lg.setResultCount(count);
//            }
//        }else{
//            if("5".equals(count)){
//                lg.setFlag(false);
//                lg.setMsg("该题目已被抢答啦，不能领取门票了.");
//                lg.setResultCount("4");
//            }else{
//                lg.setResultCount(count);
//            }
//        }
        Properties properties = new Properties();
        InputStream in = RegController.class.getClassLoader().getResourceAsStream("env.properties");
        String ids = null;
        try {
            properties.load(in);
            ids = (String) properties.get("couponId");
            System.out.println(ids);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] id = ids.split(",");
//        int num=Integer.parseInt(lg.getResultCount());
        CouponTypeDto couponTypeDto = iCouponTypeService.getById(Long.parseLong(id[count - 2]));
//        lg.setCoupon(couponTypeDto);
        return couponTypeDto;
        //CommonResult result= iCouponTypeService.draw();
        /**
         *  （3）5条全对，获得100元门票优惠券1张。每条关卡上仅限一张，先到先得。
         *  （4）答对4条，获得40元门票优惠券1张。
         *  （5）答对3条，获得30元门票优惠券1张。
         *  （6）答对2条，获得20元门票优惠券1张。
         *  （7）答对1条，获得纪念品1份。
         *  （8）第1条必答，如答错，闯关结束。
         */
    }

    @RequestMapping(value = "coupon")
    public ModelAndView coupon(Model model, HttpServletRequest request) {
        String number = request.getParameter("lnum");
        String count = request.getParameter("lcount");
        String couponId = request.getParameter("lcouponId");
        if (StringUtils.isNullOrWhiteSpace(count))
            return new ModelAndView("redirect:/light/" + number);

        Integer ct = Integer.parseInt(count);

        if (ct.equals(5)) {

            Boolean checked=subjectService.checkStatus(Long.parseLong(number));

            if(checked) {
               CommonResult result = subjectService.updateStatus(Long.parseLong(number),us.getCurrentUserId());
               subjectAllFactory.clear();
               return new ModelAndView("redirect:/receiving?typeId=" + number);
            }
            else
                ct=4;

        }
        model.addAttribute("count", count);
        model.addAttribute("num", number);
        if (ct < 5) {
            if(ct>1) {
                CouponTypeDto couponDto = this.getCouponId(ct);
                couponDto.setQuota(new BigDecimal(couponDto.getQuota().intValue()));
                model.addAttribute("coupon", couponDto);
            }
            return view("/lottery/coupon", model);
        }


        return new ModelAndView("redirect:/light/" + number);
    }

    @RequestMapping(value = "receiving")
    public ModelAndView receiving(Model model, HttpServletRequest request) {
        if(us.getCurrentUser()==null){
            return new ModelAndView("redirect:"+us.authur(request.getRequestURI()));
        }
        String typeId = request.getParameter("typeId");
        model.addAttribute("typeId", typeId);
        return view("/lottery/receiving", model);
    }

    @RequestMapping(value = "ajax_bangding", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult ajax_bangding(HttpServletRequest request) {
        String couponId = request.getParameter("couponId");
        UserSessionDto userSession = us.getCurrentUser();
        CommonResult commonResult = iCouponTypeService.draw(userSession.getId(), Long.parseLong(couponId));
        return commonResult;

    }

    @RequestMapping(value = "ajax_received_ticket", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult ajax_received_ticket(HttpServletRequest request) {
        String typeId = request.getParameter("typeId");
        SubjectDto subject = subjectService.getById(Long.parseLong(typeId));
        if (!subject.getReceive()) {
            return new CommonResult(false, "奖项已被领取完");
        }
        UserSessionDto userSession = us.getCurrentUser();
        if (userSession == null) {
            return new CommonResult(false, "用户信息不存在");
        }
        if(subject.getAnswerUserId()==null||!subject.getAnswerUserId().equals(us.getCurrentUserId()))
            return  new CommonResult(false, "您手速慢了，奖励被他人提前领走！");
        CreateOrder createOrder = new CreateOrder();
        createOrder.setCheckFetcher(false);
        createOrder.setRemark("灯谜活动门票:通道 "+typeId);
        createOrder.setTouristId(userSession.getId());
        CreateOrderProduct createOrderProduct = new CreateOrderProduct();

        Properties properties = new Properties();
        InputStream in = RegController.class.getClassLoader().getResourceAsStream("env.properties");
        String ids = null;
        try {
            properties.load(in);
            ids = (String) properties.get("nightPark");
            System.out.println(ids);
        } catch (IOException e) {

            e.printStackTrace();
        }

        createOrderProduct.setProductId(Long.parseLong(ids));
        createOrderProduct.setTourScheduleDate(DateHelper.parse("2017-02-11","yyyy-MM-dd"));
        createOrderProduct.setCount(1);
        List<CreateOrderProduct> list = new ArrayList<>();
        list.add(createOrderProduct);
        createOrder.setOrderProducts(list);
        try {
            CommonResult commonResult = iOrderService.LightNightPark(createOrder, Long.parseLong(typeId));
//            if(commonResult.getIsSuccess()){
//                subjectService.updateReceive(Long.parseLong(typeId));
//            }
            return commonResult;
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(false, "门票领取失败");
        }
    }

    @RequestMapping(value = "received")
    public ModelAndView received(Model model,HttpServletRequest request) {
        if(us.getCurrentUser()==null){
            return new ModelAndView("redirect:"+us.authur(request.getRequestURI()));
        }
        return view("/lottery/received", model);
    }

    @RequestMapping(value = "/result/{num}")
    public ModelAndView result(Model model, HttpServletRequest request, @PathVariable String num) {
        if(us.getCurrentUser()==null){
            return new ModelAndView("redirect:"+us.authur(request.getRequestURI()));
        }
        model.addAttribute("num", num);
        return view("/lottery/result", model);
    }


    @RequestMapping(value = "clearSubject",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult ajax_clear(HttpServletRequest request,Model model){
        try{
            subjectAllFactory.clear();
            return new CommonResult(true);
        }catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(false);
        }

    }

}
