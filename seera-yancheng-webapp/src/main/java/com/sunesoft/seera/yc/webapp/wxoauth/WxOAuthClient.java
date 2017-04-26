package com.sunesoft.seera.yc.webapp.wxoauth;

import com.google.gson.Gson;
import com.google.zxing.qrcode.encoder.QRCode;
import com.sunesoft.seera.fr.utils.QRCodeGenerator;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by bing on 16/8/25.
 */
@Component
public class WxOAuthClient {

    private String appId = "wx43ac85dc751408e1";

    private String secret;

    private Gson gson = new Gson();

    public CodeResponse authorizationCode(String code) throws IOException {
        Connection connection = Jsoup.connect("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" +
                appId + "&secret=" + secret + "&code=" + code + "&grant_type=authorization_code");
        Document document = connection.post();
        String responseResult = document.data();
        return gson.fromJson(responseResult, CodeResponse.class);
    }

    public CodeResponse refreshToken(String refreshToken) throws IOException {
        Connection connection = Jsoup.connect("https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" +
                appId + "&grant_type=refresh_token&refresh_token=" + refreshToken);
        Document document = connection.post();
        String responseResult = document.data();
        return gson.fromJson(responseResult, CodeResponse.class);
    }

    public UserInfoResponse getUserInfo(String accessToken, String openId) throws IOException {
        Connection connection = Jsoup.connect("https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN");
        Document document = connection.get();
        String responseResult = document.data();
        return gson.fromJson(responseResult, UserInfoResponse.class);
    }

    public synchronized QrCode wxQr() {
        String uuid = UUID.randomUUID().toString();
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId +
                "&redirect_uri=http%3a%2f%2fwww.sunesoft.com%2flogin_wx_oauth" +
                "&response_type=code&scope=snsapi_userinfo" +
                "&state=" + uuid;
        String code = QRCodeGenerator.generalQRCode(url, 200, 200);
        QrCode qrCode = new QrCode();
        qrCode.setUuid(uuid);
        qrCode.setUrl(url);
        qrCode.setCode(code);
        return qrCode;
    }
}