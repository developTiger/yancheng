package com.sunesoft.seera.yc.core.uAuth.domain;

/**
 * Created by zhouz on 2016/5/12.
 */
public interface WxTokenRepository {


    Long save( WxToken token);

    void delete(Long id);

    WxToken get(Long tokenId);

}
