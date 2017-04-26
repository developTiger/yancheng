package com.sunesoft.seera.yc.pwb.common;

import java.io.Serializable;

/**
 * Created by bing on 16/7/27.
 */
public class IdentityInfo implements Serializable {

    private String corpCode;

    private String userName;

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
