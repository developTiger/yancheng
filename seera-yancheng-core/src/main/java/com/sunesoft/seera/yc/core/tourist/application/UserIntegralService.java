package com.sunesoft.seera.yc.core.tourist.application;

import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.utils.*;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.ConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhaowy on 2016/8/14.
 */
public class UserIntegralService {

    private static Logger logger = LoggerFactory.getLogger(UserIntegralService.class);

    /**
     * 获取用户实时积分
     *
     * @param openid 用户唯一ID（手机号） 32位
     * @return success:true 成功 obj 为用户积分
     * success:false 失败
     * msg:错误信息
     */
    public static CommonResult getUserIntegrals(String openid) throws ConfigurationException, IOException {
        String channelId = Configs.getProperty("qxchannelId");
        if (StringUtils.isNullOrWhiteSpace(channelId)) throw new ConfigurationException("渠道ID获取错误");
        String timestamp = DateHelper.formatDate(new Date(), "yyyymmddhhmmssm");
        String key = Configs.getProperty("qxkey");
        if (StringUtils.isNullOrWhiteSpace(key)) throw new ConfigurationException("渠道Key获取错误");
        String token = MD5.GetMD5Code(key + timestamp).toLowerCase();
        //TODO 获取用户积分的请求结果
        String actionUrl = Configs.getProperty("getUserIntegrals");
        if (StringUtils.isNullOrWhiteSpace(actionUrl)) throw new ConfigurationException("积分读取地址错误");

        List<NameValuePair> datas = new ArrayList<>();
//        httpPost.setHeader("userId", userId);
//        httpPost.setHeader("channelId", channelId);
//        httpPost.setHeader("timestamp", timestamp);
//        httpPost.setHeader("token", token);

        datas.add(new BasicNameValuePair("openid", openid));
        datas.add(new BasicNameValuePair("channelId", channelId));
        datas.add(new BasicNameValuePair("timestamp", timestamp));
        datas.add(new BasicNameValuePair("token", token));
        return postAction(actionUrl,datas);
    }

    /**
     * 新增用户积分
     *
     * @param openid    微信用户唯一ID（微信openid）
     * @param integrals 增加的用户积分
     * @return success:true 成功 obj.integrals 为用户积分
     * success:false 失败
     * msg:错误信息
     */
    public static CommonResult increaseUserIntegrals(String openid, Integer integrals) throws ConfigurationException, IOException {
        String channelId = Configs.getProperty("qxchannelId");
        if (StringUtils.isNullOrWhiteSpace(channelId)) throw new ConfigurationException("渠道ID获取错误");
        String timestamp = DateHelper.formatDate(new Date(), "yyyymmddhhmmssm");
        String key = Configs.getProperty("qxkey");
        if (StringUtils.isNullOrWhiteSpace(key)) throw new ConfigurationException("渠道Key获取错误");
        String token = MD5.GetMD5Code(key + timestamp).toLowerCase();
        //TODO 增加用户积分的请求结果
        String actionUrl = Configs.getProperty("increaseUserIntegrals");
        if (StringUtils.isNullOrWhiteSpace(actionUrl)) throw new ConfigurationException("累加积分地址错误");

        List<NameValuePair> datas = new ArrayList<>();
        datas.add(new BasicNameValuePair("openid", openid));
        datas.add(new BasicNameValuePair("channelId", channelId));
        datas.add(new BasicNameValuePair("integrals", integrals.toString()));
        datas.add(new BasicNameValuePair("timestamp", timestamp));
        datas.add(new BasicNameValuePair("token", token));
        return postAction(actionUrl,datas);

    }

    private static CommonResult postAction(String actionUrl,List<NameValuePair> datas) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(actionUrl);
        httpPost.setEntity(new UrlEncodedFormEntity(datas, "UTF-8"));

        HttpResponse httpResponse = httpClient.execute(httpPost);
        InputStream stream = httpResponse.getEntity().getContent();
        String responseStr = "";
        String line;
        BufferedReader rd = new BufferedReader(new InputStreamReader(stream));
        while ((line = rd.readLine()) != null) {
            responseStr += line;
        }

        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            IntegralsResponse response = (IntegralsResponse) JsonHelper.toObject(responseStr, IntegralsResponse.class);
            if (response.getSuccess()) {
                System.out.println(response.getObj().getIntegral());
                return new CommonResult(true,response.getObj().getIntegral());
            }
            System.out.println(response.getErrMsgCode() + response.getMsg());
            logger.warn(response.getErrMsgCode() +response.getMsg(),response);
        }
        return ResultFactory.commonError("积分接口调用异常！");
    }

}
