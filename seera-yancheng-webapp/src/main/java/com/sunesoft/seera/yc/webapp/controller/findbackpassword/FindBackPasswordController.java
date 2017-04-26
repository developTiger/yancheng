package com.sunesoft.seera.yc.webapp.controller.findbackpassword;

import com.sunesoft.seera.fr.msg.ChannelType;
import com.sunesoft.seera.fr.msg.MessageService;
import com.sunesoft.seera.fr.msg.email.EmailMessage;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.MD5;
import com.sunesoft.seera.yc.core.findbackpassword.application.FindBackPasswordService;
import com.sunesoft.seera.yc.core.tourist.application.ITouristService;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristDto;
import com.sunesoft.seera.yc.core.tourist.domain.criteria.TouristCriteria;
import com.sunesoft.seera.yc.pingplus.ChargeUtil;
import com.sunesoft.seera.yc.webapp.controller.Layout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

/**
 * Created by zhouz on 2016/6/29.
 */
@Controller
public class FindBackPasswordController extends Layout {

    @Autowired
    FindBackPasswordService findBackPasswordService;

    @Autowired
    ITouristService iTouristService;

    @Autowired
    MessageService messageService;


    @RequestMapping(value="sureZHANGHAO")
    public ModelAndView sureZHANGHAO(Model model, HttpServletRequest request, HttpServletResponse response){
        return view(findpassword,"findbackpassword/sureZHANGHAO", model);
    }

    @ResponseBody
    @RequestMapping(value="send_email_link",method = RequestMethod.POST)
    public CommonResult send_email_link( HttpServletRequest request){

        String email=request.getParameter("email");

        TouristDto touristDto=findBackPasswordService.checkAccount(email);
        String secretKey = UUID.randomUUID().toString(); // 密钥

        touristDto.setFindpasswordKey(secretKey+"|"+ DateHelper.formatDate(new Date()));
        EmailMessage msg=new EmailMessage();
        msg=ChargeUtil.emailMessage();
        msg.setReceiver(email);
        String message="<body>尊敬的用户 "+touristDto.getUserName()+" :您好!</br>请勿回复本邮件.点击下面的链接,重设" +
                "密码<br/><a href='http://localhost:8080/resetPassword?userName="+touristDto.getUserName()+"&key="+MD5.GetMD5Code(touristDto.getFindpasswordKey())+"'>" +
                "http://localhost:8080/resetPassword?userName="+ touristDto.getUserName()+"&key="+MD5.GetMD5Code(touristDto.getFindpasswordKey())+"</a></body>";
        msg.setMessage(message);
        msg.setSubject("春秋淹城乐园商城找回密码");
        messageService.sendMessage(ChannelType.Email,msg);
        iTouristService.update(touristDto);
        return new CommonResult(true,email);
    }
    @RequestMapping(value="resetPassword")
    public ModelAndView resetPassword(HttpServletRequest request, HttpServletResponse response,Model model){

        String key=request.getParameter("key");
        String token=request.getParameter("userName");
        CommonResult checkApprovedFindBack=findBackPasswordService.checkApprovedFindBack(key,token);
        if(!checkApprovedFindBack.getIsSuccess()){
            return view(findpassword,"findbackpassword/AnQuanValidate", model);
        }
        model.addAttribute("UserName",token);
        return view(findpassword,"findbackpassword/resetPassword", model);
    }

    @ResponseBody
    @RequestMapping(value="update_password_by_email",method = RequestMethod.POST)
    public CommonResult update_password_by_email(HttpServletRequest request, HttpServletResponse response){

        String password=request.getParameter("password");
        String userName=request.getParameter("userName");
        return findBackPasswordService.updatePassword(userName, password);
    }

    @ResponseBody
    @RequestMapping(value="sure_account",method = RequestMethod.POST)
    public CommonResult ajax_sure_account(HttpServletRequest request, HttpServletResponse response){

        String token=request.getParameter("token");
        TouristCriteria touristCriteria=new TouristCriteria();
        touristCriteria.setToken(token);
        return findBackPasswordService.checkAccount(touristCriteria);
    }

    @RequestMapping(value="toAnQuanValidate/{id}")
    public ModelAndView toAnQuanValidate(@PathVariable String id, Model model, HttpServletRequest request, HttpServletResponse response){

        UniqueResult<TouristDto> touristDto=iTouristService.getTourist(Long.parseLong(id));
        model.addAttribute("tourist",touristDto.getT());
        return view(findpassword,"findbackpassword/AnQuanValidate", model);
    }

    @RequestMapping(value="editPasswordSuccess")
    public ModelAndView editPasswordSuccess(Model model, HttpServletRequest request, HttpServletResponse response){
        return view(findpassword,"findbackpassword/findPasswordSuccess", model);
    }

}
