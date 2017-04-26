package com.sunesoft.seera.yc.webapp.controller;

import com.google.gson.Gson;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.fr.utils.Configs;
import com.sunesoft.seera.fr.utils.JsonHelper;
import com.sunesoft.seera.yc.core.tourist.application.ITouristService;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristDto;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristSimpleDto;
import com.sunesoft.seera.yc.core.tourist.domain.TouristGender;
import com.sunesoft.seera.yc.core.uAuth.application.dtos.UserSessionDto;
import com.sunesoft.seera.yc.webapp.function.UserSession;
import com.sunesoft.seera.yc.webapp.wx.AccessToken;
import com.sunesoft.seera.yc.webapp.wx.UserInfo;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by jade on 16/8/18.
 */
@Controller
public class LogonController extends BaseController {

    @Autowired
    private UserSession userSession;

    @Autowired
    private ITouristService iTouristService;

    /**
     * 登录.html
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/login")
    public ModelAndView logon_view(HttpServletRequest request, Model model) {
        String redirectUrl = request.getParameter("rurl");
        if (!com.sunesoft.seera.fr.utils.StringUtils.isNullOrWhiteSpace(redirectUrl)) {
            model.addAttribute("redirectUrl", redirectUrl);
        }
        return view("logon/index", model);
    }

    /**
     * 登录处理
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/logon", method = RequestMethod.POST)
    public ModelAndView logon(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String redirectUrl = request.getParameter("rurl");
        if (StringUtils.isEmpty(username))
            model.addAttribute("username_error", "用户名不能为空!");
        if (StringUtils.isEmpty(password))
            model.addAttribute("password_error", "密码不能为空!");

        UserSessionDto userSessionDto = userSession.login(request, response, username, password);
        if (userSessionDto != null) {
            if (StringUtils.isEmpty(redirectUrl))
                response.sendRedirect("/");
            else response.sendRedirect(redirectUrl);
            return null;
        } else {
            model.addAttribute("error", "用户名或密码错误!");
        }
        return view("logon/index", model);
    }

    @ResponseBody
    @RequestMapping(value = "/wx_verify")
    public String wx_verify(HttpServletRequest request) throws Exception {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        if (StringUtils.isEmpty(signature)) throw new Exception("signature is empty!");
        if (StringUtils.isEmpty(timestamp)) throw new Exception("timestamp is empty!");
        if (StringUtils.isEmpty(nonce)) throw new Exception("nonce is empty!");

        String token = Configs.getProperty("seara.m.token");

        String[] params = new String[3];
        params[0] = token;
        params[1] = timestamp;
        params[2] = nonce;
        Arrays.sort(params);
        String param = "";
        for (String p :
                params) {
            param += p;
        }
        String tmpStr = SHA1(param);
        if (tmpStr.equals(signature)) {
            return echostr;
        }
        return "";
    }

    public static String SHA1(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping(value = "/wx_login", method = RequestMethod.GET)
    public void wx_login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        String redirectUrl = request.getParameter("returnUrl");
        String appId = Configs.getProperty("seera.wx.oauth.appid");
        String secert = Configs.getProperty("seera.wx.oauth.secret");
        String accessTokenUrl = Configs.getProperty("seera.wx.oauth.at.url");
        FileWriter fileWriter = new FileWriter("e:\\1.txt");
        if (StringUtils.isNotEmpty(appId) && StringUtils.isNotEmpty(secert) && StringUtils.isNotEmpty(accessTokenUrl)) {
            fileWriter.write("0001" + String.format(accessTokenUrl, appId, secert, code) + "\\n");
            Connection connection = Jsoup.connect(String.format(accessTokenUrl, appId, secert, code));
            Document document = connection.get();
            Gson gson = new Gson();
            fileWriter.write("0002" + document.text() + "\\n");
            AccessToken accessToken = gson.fromJson(document.text(), AccessToken.class);
            if (accessToken != null && accessToken.getErrCode() == 0) {
                fileWriter.write("0003" + accessToken + "\\n");
                UniqueResult<TouristSimpleDto> touristSimpleDtoUniqueResult = iTouristService.checkOpenId(accessToken.getOpenId());
                if (touristSimpleDtoUniqueResult.getIsSuccess() && touristSimpleDtoUniqueResult.getT() != null) {
                    UserSessionDto userSessionDto = new UserSessionDto();
                    userSessionDto.setId(touristSimpleDtoUniqueResult.getT().getId());
                    userSessionDto.setName(touristSimpleDtoUniqueResult.getT().getWxName());
                    userSessionDto.setLoginName(touristSimpleDtoUniqueResult.getT().getOpenid());
                    userSessionDto.setOpenId(touristSimpleDtoUniqueResult.getT().getOpenid());
                    userSessionDto.setMobile(touristSimpleDtoUniqueResult.getT().getMobilePhone());
                    userSession.setSystemUserCookie(userSessionDto.getId().toString(), response);
                    userSession.setUserSession(request, userSessionDto);
                } else {
                    String userInfoUrl = Configs.getProperty("seera.wx.oauth.ui.url");
                    if (StringUtils.isNotEmpty(userInfoUrl)) {
                        fileWriter.write(String.format(userInfoUrl, accessToken.getAccessToken(), accessToken.getOpenId()) + "/n");
                        connection = Jsoup.connect(String.format(userInfoUrl, accessToken.getAccessToken(), accessToken.getOpenId()));
                        document = connection.get();
                        fileWriter.write("0004" + document.text() + "\\n");
                        UserInfo userInfo = gson.fromJson(document.text(), UserInfo.class);
                        if (userInfo != null && userInfo.getOpenId() != null) {
                            fileWriter.write("0005" + JsonHelper.toJson(userInfo));
                            String password = UUID.randomUUID().toString();
                            TouristDto touristDto = new TouristDto();
                            touristDto.setOpenid(userInfo.getOpenId());
                            touristDto.setUserName("");
                            touristDto.setRealName(userInfo.getNickName());
                            touristDto.setWxName(userInfo.getNickName());
                            touristDto.setCity(userInfo.getCity());
                            touristDto.setProvince(userInfo.getProvince());
                            touristDto.setCountry(userInfo.getCountry());
                            if (!com.sunesoft.seera.fr.utils.StringUtils.isNullOrWhiteSpace(userInfo.getSex())) {
                                if (userInfo.getSex() == "0")
                                    touristDto.setGender(TouristGender.Unknown);
                                if (userInfo.getSex() == "1")
                                    touristDto.setGender(TouristGender.Male);
                                if (userInfo.getSex() == "2")
                                    touristDto.setGender(TouristGender.Female);
                            }
                            touristDto.setHeadimgurl(userInfo.getHeadImgUrl());
                            touristDto.setUnionid(userInfo.getUnionid());
                            String result = "";


//                            for (String ple :
//                                    userInfo.getPrivilege()) {
//                                result += "".equals(result) ? ple : ";" + ple;
//                            }
//                            touristDto.setPrivilege(result);
                            CommonResult result1 = iTouristService.create(touristDto);

                            iTouristService.restPassword(result1.getId(), password);
                            userSession.login(request, response, userInfo.getOpenId(), password);
                        }

                    }
                }
            }
        }

        fileWriter.flush();
        fileWriter.close();
        if (!com.sunesoft.seera.fr.utils.StringUtils.isNullOrWhiteSpace(redirectUrl))
            response.sendRedirect(redirectUrl);
        else
            response.sendRedirect("/");

    }

    /**
     * 退出
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userSession.removeUserCookie(request, response);
        response.sendRedirect("/login");
    }
}
