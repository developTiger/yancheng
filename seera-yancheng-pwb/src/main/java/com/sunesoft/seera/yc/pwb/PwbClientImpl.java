package com.sunesoft.seera.yc.pwb;

import com.sunesoft.seera.fr.utils.Configs;
import com.sunesoft.seera.yc.Client;
import com.sunesoft.seera.yc.Request;
import com.sunesoft.seera.yc.Response;
import com.sunesoft.seera.yc.utils.MD5Util;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bing on 16/7/27.
 */
@Service
public class PwbClientImpl implements Client {

    private final String sign = Configs.getProperty("seera.yancheng.pwb.sign");
    private final String url = Configs.getProperty("seera.yancheng.pwb.url");

    private Logger logger = LoggerFactory.getLogger(PwbClientImpl.class);

    public <T extends Response> T execute(Request<T> request) throws IOException, JAXBException {
        String xml = request.toXml();
        String signString = MD5Util.MD5("xmlMsg=" + xml+ sign).toLowerCase();
        System.out.print(xml);
        if (logger.isDebugEnabled()) {
            logger.debug("url:" + url);
            logger.debug("sign:" + sign);
            logger.debug("request:" + xml);
            logger.debug("signString:" + signString);
        }

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> datas = new ArrayList<>();
        datas.add(new BasicNameValuePair("xmlMsg", xml));
        datas.add(new BasicNameValuePair("sign", signString));
        httpPost.setEntity(new UrlEncodedFormEntity(datas,"UTF-8"));
        HttpResponse httpResponse = httpClient.execute(httpPost);

        if (httpResponse != null) {
            if (logger.isDebugEnabled()) {
                logger.debug(httpResponse.getStatusLine().toString());
                logger.debug(EntityUtils.toString(httpResponse.getEntity()));
            }
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                return Request.toObject(EntityUtils.toString(httpResponse.getEntity()), request.getResponseClass());
            }
        }

        return null;

//        Connection connection = Jsoup.connect(url);
//        connection.ignoreContentType(true);
//        connection.data("xmlMsg", xml);
//        connection.data("sign", signString);
//
//        Document document = connection.post();
//        String responseXml = document.body().html();
//        if (logger.isDebugEnabled())
//            logger.debug(responseXml);
//
//        return Request.toObject(responseXml, request.getResponseClass());
    }

}
