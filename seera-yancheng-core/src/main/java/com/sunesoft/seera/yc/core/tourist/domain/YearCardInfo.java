package com.sunesoft.seera.yc.core.tourist.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by zwork on 2016/11/4.
 */
@Entity
public class YearCardInfo extends BaseEntity {

    private String phoneNo;

    /**
     * 年卡类型
     */
    @Column(name = "year_card_info")
    private String yearCardType;

    /**
     * 年卡过期时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "year_card_expire_date")
    private Date yearCardExpireDate;


    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getYearCardType() {
        return yearCardType;
    }

    public void setYearCardType(String yearCardType) {
        this.yearCardType = yearCardType;
    }

    public Date getYearCardExpireDate() {
        return yearCardExpireDate;
    }

    public void setYearCardExpireDate(Date yearCardExpireDate) {
        this.yearCardExpireDate = yearCardExpireDate;
    }
}
