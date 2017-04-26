package com.sunesoft.seera.yc.core.tourist.application.dtos;

import com.sunesoft.seera.yc.core.coupon.application.dto.CouponDto;
import com.sunesoft.seera.yc.core.tourist.domain.TouristGender;

import java.util.Date;
import java.util.List;

/**
 * Created by zhaowy on 2016/7/12.
 */
public class TouristDto extends TouristSimpleDto {

    //region Filed
    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别
     */
    private TouristGender gender;
    /**
     * 身份证号
     */
    private String idCardNo;


    /**
     * 常用取件人信息
     */

    private List<FetcherDto> fetcherDtos;
    /**
     * 绑定优惠券
     */
    private List<CouponDto> bindCouponDtos;

    //region wx field

    /**
     * 微信号省份
     * 用户个人资料填写的省份
     */
    private String province;

    /**
     * 微信号城市
     * 普通用户个人资料填写的城市
     */
    private String city;

    /**
     * 微信号国家
     * 如中国为CN
     */
    private String country;


    /**
     * 用户特权信息
     * 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
     */
    private String privilege;

    /**
     * 微信号UnionId
     * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
     */
    private String unionid;


    private String findpasswordKey;



    public String getFindpasswordKey() {
        return findpasswordKey;
    }

    public void setFindpasswordKey(String findpasswordKey) {
        this.findpasswordKey = findpasswordKey;
    }


    //endregion

    //endregion

    //region Property Get&Set

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public TouristGender getGender() {
        return gender;
    }

    public void setGender(TouristGender gender) {
        this.gender = gender;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }


    public List<CouponDto> getBindCouponDtos() {
        return bindCouponDtos;
    }

    public void setBindCouponDtos(List<CouponDto> bindCouponDtos) {
        this.bindCouponDtos = bindCouponDtos;
    }

    public List<FetcherDto> getFetcherDtos() {
        return fetcherDtos;
    }

    public void setFetcherDtos(List<FetcherDto> fetcherDtos) {
        this.fetcherDtos = fetcherDtos;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }



    //endregion

    //region Method

    /**
     * 获取游客默认取件人
     *
     * @return 已设置默认取件人则返回，否则返回NULL
     */
    public FetcherDto getDefaultFetcher() {
        if (this.fetcherDtos == null) return null;
        return this.fetcherDtos.stream().filter(i -> i.getIsDefault()).findFirst().orElse(null);
    }
    //endregion

}
