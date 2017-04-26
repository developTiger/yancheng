package com.sunesoft.seera.yc.webapi.tourist;


import com.sunesoft.seera.fr.loggers.Logger;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.fr.utils.*;
import com.sunesoft.seera.yc.core.tourist.application.ITouristService;
import com.sunesoft.seera.yc.core.tourist.application.dtos.TouristSimpleDto;
import com.sunesoft.seera.yc.webapi.RequestValidator;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.naming.ConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by zhaowy on 2016/7/25.
 */
@Controller
public class TouristService {

    @Autowired
    Logger logger;


    @Autowired
    ITouristService service;

    /*  private static final String XML_VIEW_NAME = "tourist";

        @RequestMapping(value = "/tourist/{id}",method = RequestMethod.GET)
        @ResponseBody
        public TouristSimpleDto getTourist1(@PathVariable("id") long id)
        {
            return service.getTourist(id).getT();
        }*/
    @CrossOrigin
    @RequestMapping(value = "/tourist", params = {"name", "access_token"}, method = RequestMethod.GET)
    @ResponseBody
    public TouristInfo getTourist(@RequestParam("name") String name, @RequestParam("access_token") String access_token) {
        if (!RequestValidator.isValid(access_token, name)) return null;
        UniqueResult result = service.getTourist(name);
        if (result.getIsSuccess())
            return Factory.convert(result.getT(),TouristInfo.class);
        logger.warn(result.getMsg());
        return null;
    }

    @CrossOrigin
    @RequestMapping(value = "/tourist", params = {"openid", "access_token"}, method = RequestMethod.GET)
    @ResponseBody
    public TouristInfo getTouristByOpenId(@RequestParam("openid") String openid, @RequestParam("access_token") String access_token) {
        if (!RequestValidator.isValid(access_token, openid)) return null;
        UniqueResult result = service.getTouristByOpenId(openid);

        if (result.getIsSuccess()) {
            return Factory.convert(result.getT(),TouristInfo.class);
        }
        logger.warn(result.getMsg());
        return null;
    }

    /**
     * @param arg
     * @throws ConfigurationException
     * @throws IOException
     */
    public static void main(String[] arg) throws ConfigurationException, IOException {
        String name = "489148198";
        String privateKey="LCCW90XTSW58";
        String access_token = MD5.GetMD5Code(name + privateKey).toLowerCase();
        String actionUrl = "http://localhost:8081/tourist?name="+name+"&access_token="+access_token;

        HttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(actionUrl);
        httpGet.addHeader("name", name);
        httpGet.addHeader("access_token", access_token);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        InputStream stream = httpResponse.getEntity().getContent();
        String responseStr = "";
        String line;
        BufferedReader rd = new BufferedReader(new InputStreamReader(stream));
        while ((line = rd.readLine()) != null) {
            responseStr += line;
        }
        System.out.println(responseStr);
    }
}