package com.sunesoft.seera.yc.webapp.model;

import com.sunesoft.seera.yc.core.coupon.application.dto.CouponDto;
import com.sunesoft.seera.yc.core.coupon.application.dto.CouponTypeDto;

/**
 * Created by kkk on 2017/1/12.
 */
public class LightResult {

    private String resultCount;
    private CouponTypeDto coupon;
    private Boolean flag;
    private String msg;

    public String getResultCount() {
        return resultCount;
    }

    public void setResultCount(String resultCount) {
        this.resultCount = resultCount;
    }

    public CouponTypeDto getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponTypeDto coupon) {
        this.coupon = coupon;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
