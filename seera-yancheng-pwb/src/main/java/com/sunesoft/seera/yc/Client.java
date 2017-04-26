package com.sunesoft.seera.yc;

/**
 * Created by bing on 16/7/27.
 */
public interface Client {

    <T extends Response> T execute(Request<T> request) throws Exception;

}