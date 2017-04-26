package com.sunesoft.seera.yc.webapp.controller;

import com.google.gson.Gson;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.utils.Configs;
import com.sunesoft.seera.fr.utils.DateHelper;
import com.sunesoft.seera.fr.utils.JsonHelper;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.activity.application.ShareActivityService;
import com.sunesoft.seera.yc.core.activity.application.dtos.ShareActivityDto;
import com.sunesoft.seera.yc.core.fileSys.FileService;
import com.sunesoft.seera.yc.core.fileSys.FileType;
import com.sunesoft.seera.yc.core.uAuth.application.WxTokenService;
import com.sunesoft.seera.yc.core.uAuth.domain.WxToken;
import com.sunesoft.seera.yc.webapp.function.UserSession;
import com.sunesoft.seera.yc.webapp.function.Wx_Access;
import com.sunesoft.seera.yc.webapp.model.AccessToken;
import com.sunesoft.seera.yc.webapp.model.JsApiTicket;
import com.sunesoft.seera.yc.webapp.model.WxTicketInfo;
import com.sunesoft.seera.yc.webapp.utils.AjaxResponse;
import com.sunesoft.seera.yc.webapp.utils.Helper;
import com.sunesoft.seera.yc.webapp.wx.UserInfo;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

/**
 * Created by jade on 2016/12/7.
 */

@Controller
public class ActivityController extends BaseController {

    private static WxTicketInfo wxTicketInfo = new WxTicketInfo();

    @Autowired
    UserSession userSession;

    @Autowired
    FileService fileService;

    @Autowired
    Wx_Access wx_access;
    @Autowired
    WxTokenService wxTokenService;
    @Autowired
    ShareActivityService shareActivityService;

    @RequestMapping(value = "/dd")
    public ModelAndView shareActivity_view(Model model, HttpServletRequest request) throws IOException {
        return new ModelAndView("redirect:/dd/" + userSession.getCurrentUser().getOpenId());
    }

    /**
     * 活动页面
     *
     * @param model
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/dd/{openId}")
    public ModelAndView diandeng_view(Model model, HttpServletRequest request, @PathVariable String openId) throws IOException {

        ShareActivityDto shareActivityDto = shareActivityService.getShare(openId);
        if (shareActivityDto != null) {
            return new ModelAndView("redirect:/dd/share/" + openId + ".html");
        }
        return view("activity/index", model);
    }

    /**
     * 分享页面
     *
     * @param openId
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/dd/share/{openId}.html")
    public ModelAndView dd_share_view(@PathVariable String openId, Model model, HttpServletRequest request) throws IOException {

        ShareActivityDto shareActivityDto = shareActivityService.getShare(openId);

        if (shareActivityDto == null) {
            return new ModelAndView("redirect:/dd/" + openId);
        }

        //获取jsapi_ticket
        getticket();

        String accessTokenUrl = Configs.getProperty("seera.wx.oauth.cgui.url");
        String appId = Configs.getProperty("seera.wx.oauth.appid");
        String secret = Configs.getProperty("seera.wx.oauth.secret");
        Connection connection = Jsoup.connect(String.format(accessTokenUrl, wxTicketInfo.getAccess_token(), userSession.getCurrentOpenId()));
        Document document = connection.ignoreContentType(true).get();

        Boolean isLiked = shareActivityService.isLiked(userSession.getCurrentOpenId(), openId);
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(document.text(), UserInfo.class);

        String url = request.getRequestURL().toString();
        String noncestr = Helper.getRandomString(16);
        long timestamp = new Date().getTime();
        String string1 = "jsapi_ticket=" + wxTicketInfo.getJsapi_ticket()
                + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;

        String signature = Helper.SHA1(string1);

        model.addAttribute("wxinfo", wxTicketInfo);
        model.addAttribute("noncestr", noncestr);
        model.addAttribute("timestamp", timestamp);
        model.addAttribute("isLiked", isLiked);
        model.addAttribute("signature", signature);
        model.addAttribute("appId", appId);
        model.addAttribute("openId", openId);
        model.addAttribute("url", url);
        model.addAttribute("subscribe", userInfo.getSubscribe());

        model.addAttribute("shareActivityDto", shareActivityDto);

        return view("activity/share", model);
    }


    /**
     * 点赞操作
     *
     * @param openId
     */
    @RequestMapping(value = "dd/share/{openId}")
    @ResponseBody
    public CommonResult dd_share(@PathVariable String openId) {

        CommonResult commonResult = shareActivityService.likeShare(userSession.getCurrentOpenId(),
                userSession.getCurrentUser().getName(), openId);

        return commonResult;
    }

    /**
     * 上传
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "upload")
    public String upload_view(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String openId = request.getParameter("openId");

        String fileName = "";
        String subPath = "";

        //设置响应给前台内容的PrintWriter对象
        PrintWriter out = response.getWriter();
        String originalFilename = null;
        List<MultipartFile> images = ((DefaultMultipartHttpServletRequest) request).getFiles("file");
        for (MultipartFile myfile : images) {
            if (myfile.isEmpty()) {
                CommonResult res = new CommonResult(false, "请选择要上传的文件！");
                AjaxResponse.write(response, JsonHelper.toJson(res));
                return null;
            } else {
                try {
                    String originalFilaName = myfile.getOriginalFilename();
                    String extension = originalFilaName.substring(originalFilaName.lastIndexOf(".") + 1);
                    fileName = DateHelper.formatDate(new Date(), "yyyyMMddhhmmSSsss");
                    fileName = fileName + "." + extension;
                    CommonResult result = fileService.upload(fileName, subPath, myfile.getInputStream(), FileType.WxUpload);
                    if (!result.getIsSuccess())
                        return null;
                    AjaxResponse.write(response, "{\"fileName\":\"" + fileName + "\"}");

                    ShareActivityDto shareActivityDto = new ShareActivityDto();

                    shareActivityDto.setOpenId(openId);
                    shareActivityDto.setWxName(userSession.getCurrentUser().getName());
                    shareActivityDto.setTitle(title);
                    shareActivityDto.setContent(content);
                    shareActivityDto.setActivityName("点灯活动");
                    shareActivityDto.setFilePath(fileName);
                    shareActivityService.addShare(shareActivityDto);

                    return "redirect:/dd/share/" + openId + ".html";

                } catch (IOException e) {
                    System.out.println("文件[" + originalFilename + "]上传失败,堆栈轨迹如下");
                    e.printStackTrace();
                    out.print("1`文件上传失败，请重试！！");
                    out.flush();
                    CommonResult res = new CommonResult(false, "文件上传失败，请重试！！");
                    AjaxResponse.write(response, JsonHelper.toJson(res));
                    return null;
                }
            }
        }

        return "redirect:/";
    }

    /**
     * 获取jsapi
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "getticket")
    @ResponseBody
    public WxTicketInfo getticket() throws IOException {
//
//        if(wxTicketInfo==null || StringUtils.isNullOrWhiteSpace(wxTicketInfo.getAccess_token())) {
//            WxToken token = wxTokenService.getTocken();
//            if(token!=null){
//                wxTicketInfo.setAccess_token(token.getToken());
//                wxTicketInfo.setJsapi_ticket(token.getTicket());
//
//                wxTicketInfo.setTimestamp(token.getTimeStamp());
//
//            }
//        }
//        //如果accesstoken不存在，或者时间差大于7000s则重新获取
//        if (wxTicketInfo == null || StringUtils.isNullOrWhiteSpace(wxTicketInfo.getAccess_token()) || (new Date().getTime() / 1000 - wxTicketInfo.getTimestamp() / 1000 > 7000)) {
//
//            String appId = Configs.getProperty("seera.wx.oauth.appid");
//            String secret = Configs.getProperty("seera.wx.oauth.secret");
//            String accessTokenUrl = Configs.getProperty("seera.wx.token.url");
//            String jsapiTicketUrl = Configs.getProperty("seera.wx.jsapiticket.url");
//
//            //获取access_token
//            Connection connection = Jsoup.connect(String.format(accessTokenUrl, appId, secret));
//            Document document = connection.ignoreContentType(true).get();
//
//            Gson gson = new Gson();
//            AccessToken accessToken = gson.fromJson(document.text(), AccessToken.class);
//
//            if (accessToken != null && accessToken.getErrCode() == 0) {
//                wxTicketInfo.setAccess_token(accessToken.getAccessToken());
//
//                Connection connection1 = Jsoup.connect(String.format(jsapiTicketUrl, accessToken.getAccessToken()));
//                Document document1 = connection1.ignoreContentType(true).get();
//
//                JsApiTicket jsapiTicket = gson.fromJson(document1.text(), JsApiTicket.class);
//
//                if (jsapiTicket != null && jsapiTicket.getErrCode() == 0) {
//                    wxTicketInfo.setJsapi_ticket(jsapiTicket.getTicket());
//                    wxTicketInfo.setTimestamp(new Date().getTime());
//                }
//
//                WxToken token = new WxToken();
//                token.setToken(wxTicketInfo.getAccess_token());
//                token.setTimeStamp(wxTicketInfo.getTimestamp());
//                token.setTicket(wxTicketInfo.getJsapi_ticket());
//
//                wxTokenService.AddOrUpdateToken(token);
//
//            }
//        }
//
//        wxTicketInfo.setExpires_in(7000 - new Date().getTime() / 1000 + wxTicketInfo.getTimestamp() / 1000);
        wxTicketInfo = wx_access.getticket();
        return wxTicketInfo;
    }

    /**
     * @return
     */
    @RequestMapping(value = "dd/gather")
    public ModelAndView gather() {
        return null;
    }

}
