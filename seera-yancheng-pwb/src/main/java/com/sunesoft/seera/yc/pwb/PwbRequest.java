package com.sunesoft.seera.yc.pwb;

import com.sunesoft.seera.fr.utils.Configs;
import com.sunesoft.seera.yc.Request;
import com.sunesoft.seera.yc.pwb.common.Header;
import com.sunesoft.seera.yc.pwb.common.IdentityInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 票务宝公共请求对象接口.
 * <p>
 * Created by bing on 16/7/27.
 */
public class PwbRequest<T extends PwbResponse> extends Request<T> {

    private final String username = Configs.getProperty("seera.yancheng.pwb.username");
    private final String corpcode = Configs.getProperty("seera.yancheng.pwb.corpcode");

    private final Logger logger = LoggerFactory.getLogger(PwbRequest.class);

    public PwbRequest(Class<?> objectClass) {
        super(objectClass);
        this.header = new Header();
        this.header.setApplication("SendCode");
        this.header.setRequestTime(SIMPLE_DATE_FORMAT.format(new Date()));

        this.identityInfo = new IdentityInfo();
        this.identityInfo.setUserName(username);
        this.identityInfo.setCorpCode(corpcode);

        if (logger.isDebugEnabled()) {
            logger.debug("username:" + username);
            logger.debug("corpcode:" + corpcode);
        }
    }

    protected String transactionName;

    protected Header header;

    protected IdentityInfo identityInfo;

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public IdentityInfo getIdentityInfo() {
        return identityInfo;
    }

    public void setIdentityInfo(IdentityInfo identityInfo) {
        this.identityInfo = identityInfo;
    }
}
