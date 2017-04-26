package com.sunesoft.seera.yc.webapp.function;

import com.google.gson.Gson;
import com.sunesoft.seera.fr.utils.Configs;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.uAuth.application.WxTokenService;
import com.sunesoft.seera.yc.core.uAuth.domain.WxToken;
import com.sunesoft.seera.yc.webapp.model.AccessToken;
import com.sunesoft.seera.yc.webapp.model.JsApiTicket;
import com.sunesoft.seera.yc.webapp.model.WxTicketInfo;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * Created by zhouzh on 2017/1/20.
 */
@Component
public  class Wx_Access {
    @Autowired
    WxTokenService wxTokenService;
    private static WxTicketInfo wxTicketInfo = new WxTicketInfo();
    public WxTicketInfo getticket() throws IOException {

        if(wxTicketInfo==null || StringUtils.isNullOrWhiteSpace(wxTicketInfo.getAccess_token())) {
            WxToken token = wxTokenService.getTocken();
            if(token!=null){
                wxTicketInfo.setAccess_token(token.getToken());
                wxTicketInfo.setJsapi_ticket(token.getTicket());

                wxTicketInfo.setTimestamp(token.getTimeStamp());

            }
        }
        //如果accesstoken不存在，或者时间差大于7000s则重新获取
        if (wxTicketInfo == null || StringUtils.isNullOrWhiteSpace(wxTicketInfo.getAccess_token()) || (new Date().getTime() / 1000 - wxTicketInfo.getTimestamp() / 1000 > 7000)) {

            String appId = Configs.getProperty("seera.wx.oauth.appid");
            String secret = Configs.getProperty("seera.wx.oauth.secret");
            String accessTokenUrl = Configs.getProperty("seera.wx.token.url");
            String jsapiTicketUrl = Configs.getProperty("seera.wx.jsapiticket.url");

            //获取access_token
            Connection connection = Jsoup.connect(String.format(accessTokenUrl, appId, secret));
            Document document = connection.ignoreContentType(true).get();

            Gson gson = new Gson();
            AccessToken accessToken = gson.fromJson(document.text(), AccessToken.class);

            if (accessToken != null && accessToken.getErrCode() == 0) {
                wxTicketInfo.setAccess_token(accessToken.getAccessToken());

                Connection connection1 = Jsoup.connect(String.format(jsapiTicketUrl, accessToken.getAccessToken()));
                Document document1 = connection1.ignoreContentType(true).get();

                JsApiTicket jsapiTicket = gson.fromJson(document1.text(), JsApiTicket.class);

                if (jsapiTicket != null && jsapiTicket.getErrCode() == 0) {
                    wxTicketInfo.setJsapi_ticket(jsapiTicket.getTicket());
                    wxTicketInfo.setTimestamp(new Date().getTime());
                }

                WxToken token = new WxToken();
                token.setToken(wxTicketInfo.getAccess_token());
                token.setTimeStamp(wxTicketInfo.getTimestamp());
                token.setTicket(wxTicketInfo.getJsapi_ticket());

                wxTokenService.AddOrUpdateToken(token);

            }
        }

        wxTicketInfo.setExpires_in(7000 - new Date().getTime() / 1000 + wxTicketInfo.getTimestamp() / 1000);
        return wxTicketInfo;
    }
}
