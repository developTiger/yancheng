package com.sunesoft.seera.yc.webapp.wxoauth;

import com.google.gson.Gson;
import org.junit.Test;

/**
 * Created by bing on 16/8/25.
 */
public class CodeResponseTest {

    @Test
    public void testCodeResponse() {
        String response = "{\"access_token\":\"ACCESS_TOKEN\"," +
                "\"expires_in\":7200," +
                "\"refresh_token\":\"REFRESH_TOKEN\"," +
                "\"openid\":\"OPENID\"," +
                "\"scope\":\"SCOPE\" }";
        Gson gson = new Gson();
        CodeResponse codeResponse = gson.fromJson(response, CodeResponse.class);
        assert codeResponse != null;
    }

    @Test
    public void testErrorResponse() {
        String response = "{\"errcode\":40029,\"errmsg\":\"invalid code\"}";
        Gson gson = new Gson();
        CodeResponse codeResponse = gson.fromJson(response, CodeResponse.class);
        assert codeResponse != null;
    }

}
