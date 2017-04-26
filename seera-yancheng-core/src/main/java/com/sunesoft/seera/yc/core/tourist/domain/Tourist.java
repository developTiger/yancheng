package com.sunesoft.seera.yc.core.tourist.domain;

import com.sunesoft.seera.fr.ddd.BaseEntity;
import com.sunesoft.seera.fr.results.CommonResult;
import com.sunesoft.seera.fr.results.ResultFactory;
import com.sunesoft.seera.fr.utils.MD5;
import com.sunesoft.seera.fr.utils.StringUtils;
import com.sunesoft.seera.yc.core.coupon.domain.Coupon;
import com.sunesoft.seera.yc.core.coupon.domain.CouponStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 游客实体聚合根
 * Created by zhaowy on 2016/7/11.
 */
@Entity
public class Tourist extends BaseEntity {

    //region Construct

    /**
     * new empty tourist
     */
    public Tourist() {
//        this.setIsActive(true);
//        this.setCreateDateTime(new Date());
//        this.setLastUpdateTime(new Date());
        this.status = TouristStatus.Normal;
        this.integrals = 0;
        //setPassword("123456");
    }

    /**
     * @param userName 用户名
     * @param pwd      密码
     */
    public Tourist(String userName, String pwd) {
        this.userName = userName;
        this.password = MD5.GetMD5Code(pwd);
        this.status = TouristStatus.Normal;
        this.integrals = 0;
        //setPassword("123456");
//        this.setIsActive(true);
//        this.setCreateDateTime(new Date());
//        this.setLastUpdateTime(new Date());
    }

    //endregion

    // region Filed
    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 密码
     */
    @Column(name = "user_password")
    private String password;

    /**
     * 手机号
     */
    @Column(name = "mobile_phone")
    private String mobilePhone;

    /**
     * 微信昵称
     */
    @Column(name = "wx_name")
    private String wxName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 真实姓名
     */
    @Column(name = "real_name")
    private String realName;

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
    @Column(name = "id_card_no")
    private String idCardNo;

    /**
     * 年卡信息
     */
    @Column(name = "year_card_info")
    private String yearCardInfo;

    /**
     * 年卡过期时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "year_card_expire_date")
    private Date yearCardExpireDate;

    /**
     * 游客状态
     */
    @Column(name = "user_status")
    private TouristStatus status;

    /**
     * 积分
     */
    private Integer integrals;

    /**
     * 绑定优惠券
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL, CascadeType.PERSIST})
    @JoinColumn(name = "tourist_id")
    private List<Coupon> bindCoupons;

    /**
     * 常用取件人信息
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL, CascadeType.ALL})
    @JoinColumn(name = "tourist_id")
    private List<Fetcher> fetchers;

    //region wx field

    /**
     * 微信openId
     */
    private String openid;//用户的唯一标识

//    /**
//     * 微信号昵称
//     */
//    private String nickname;//用户昵称

    //private String sex;//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知

    /**
     * 微信号省份
     */
    private String province;//用户个人资料填写的省份

    /**
     * 微信号城市
     */
    private String city;//普通用户个人资料填写的城市

    /**
     * 微信号国家
     */
    private String country;//国家，如中国为CN

    /**
     * 微信号用户头像
     */
    private String headimgurl;//用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。

    /**
     * 用户特权信息
     */
    private String privilege;//用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）

    /**
     * 微信号省份
     */
    private String unionid;//只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。

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
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    //无数据时显示的优先顺序
    public String getRealName() {
        if (!StringUtils.isNullOrWhiteSpace(this.realName))
            return realName;
        if (!StringUtils.isNullOrWhiteSpace(this.userName))
            return userName;
        if (!StringUtils.isNullOrWhiteSpace(this.wxName))
            return wxName;
        if (!StringUtils.isNullOrWhiteSpace(this.mobilePhone))
            return mobilePhone;
        return this.email;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getYearCardInfo() {
        return yearCardInfo;
    }

    public void setYearCardInfo(String yearCardInfo) {
        this.yearCardInfo = yearCardInfo;
    }

    public Date getYearCardExpireDate() {
        return yearCardExpireDate;
    }

    public void setYearCardExpireDate(Date yearCardExpireDate) {
        this.yearCardExpireDate = yearCardExpireDate;
    }

    public int getIntegrals() {
        return integrals;
    }

    public void setIntegrals(int integrals) {
        this.integrals = integrals;
    }

    public void increaseIntergrals(int integrals) {
        this.integrals += integrals;
    }

    public Boolean reduceIntergrals(int integrals) {
        if (integrals <= this.integrals) {
            this.integrals -= integrals;
            return true;
        } else return false;
    }

    public List<Fetcher> getFetchers() {
        return fetchers;
    }

    public List<Coupon> getBindCoupons() {
        return bindCoupons;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
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

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
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

    //region Entity Method

    public void bindCoupons(Coupon coupon) {
        if (coupon.getCouponStatus() == CouponStatus.Valid && coupon.getIsActive()) {
            //判断bindCoupons是否存在
            if (this.bindCoupons == null) {
                this.bindCoupons = new ArrayList<>();
            }
            coupon.setCouponStatus(CouponStatus.bind);
            this.bindCoupons.add(coupon);
        }

        //TODO coupon.setCouponStatus(CouponStatus.Used);
    }

    /**
     * 移除指定的优惠券
     *
     * @param coupon
     */
    public void removeCoupons(Coupon coupon) {
        this.bindCoupons.remove(coupon);
    }

    public Coupon getByCouponId(Long CouponId) {
        if (this.bindCoupons == null || this.bindCoupons.isEmpty()) {
            return null;
        }
        for (Coupon coupon : this.bindCoupons) {
            if (coupon.getId().equals(CouponId))
                return coupon;
        }
        return null;
    }

    /**
     * @param token 用户令牌，支持 userName|wxName|mobilePhone|email
     * @param pwd   明文密码
     * @return
     */
    public Boolean checkPassword(String token, String pwd) {
        if (!StringUtils.isNullOrWhiteSpace(token) && !StringUtils.isNullOrWhiteSpace(pwd))
            return (this.userName.equals(token) || this.wxName.equals(token)
                    || this.mobilePhone.equals(token) || this.email.equals(token))
                    && this.password.equals(MD5.GetMD5Code(pwd));
        return false;
    }

//    public String getPassword() {
//        return this.password;
//    }

    public void setPassword(String password) {
        if (!StringUtils.isNullOrWhiteSpace(password))
            this.password = MD5.GetMD5Code(password);
    }

    public TouristStatus getStatus() {
        return status;
    }

    public void setStatus(TouristStatus status) {
        this.status = status;
    }

    /**
     * 新增取件人
     *
     * @param fetcher 取件人
     */
    public CommonResult addFetchers(Fetcher fetcher) {
        if (this.fetchers == null || this.fetchers.isEmpty()) {
            this.fetchers = new ArrayList<>();
            fetcher.setIsDefault(true);
            this.fetchers.add(fetcher);
            return new CommonResult(true);
        } else {

            if (this.fetchers.stream().anyMatch(i -> i.getIdCardNo().equals(fetcher.getIdCardNo()))) {
                if (fetcher.getIsDefault()) {
                    this.fetchers.stream().forEach(i -> {
                        if (i.getIsDefault()) i.setIsDefault(false);
                    });
                }
                Fetcher f = this.fetchers.stream().filter(i -> i.getIdCardNo().equals(fetcher.getIdCardNo())).findFirst().get();
                f.setRealName(fetcher.getRealName());
                f.setMobilePhone(fetcher.getMobilePhone());
                f.setIsDefault(fetcher.getIsDefault());
                return ResultFactory.commonSuccess(f.getId());
            } else {
                if (fetcher.getIsDefault()) {
                    this.fetchers.stream().forEach(i -> {
                        if (i.getIsDefault()) i.setIsDefault(false);
                    });
                }

                this.fetchers.add(fetcher);
                return ResultFactory.commonSuccess();
            }
        }
    }

    /**
     * 更新取件人信息
     *
     * @param newFetcher 更新后的取件人
     */
    public void updateFetchers(Fetcher newFetcher) {
//        if (this.fetchers.contains(newFetcher)) {
//            int index = this.fetchers.indexOf(newFetcher);
//            this.fetchers.set(index, newFetcher);
//        }
        for (Fetcher f : this.fetchers) {
            if (f.getId().equals(newFetcher.getId())) {
                replace(f, newFetcher);
                break;
            }
        }
    }


    /**
     * 更新取件人信息
     *
     * @param newFetcher 更新后的取件人
     */
    public CommonResult updateFetcher(Fetcher newFetcher) {
        for (Fetcher f : this.fetchers) {
            if (f.getId().equals(newFetcher.getId())) {
                //该信息是否被应用 名称 手机 身份证号
                if ((f.getRealName() != null && !f.getRealName().equals(newFetcher.getRealName()) && this.fetchers.stream().anyMatch(i -> i.getRealName().equals(newFetcher.getRealName())))
                        || (f.getMobilePhone() != null && !f.getMobilePhone().equals(newFetcher.getMobilePhone()) && this.fetchers.stream().anyMatch(i -> i.getMobilePhone().equals(newFetcher.getMobilePhone())))
                        || (f.getIdCardNo() != null && !f.getIdCardNo().equals(newFetcher.getIdCardNo()) && this.fetchers.stream().anyMatch(i -> i.getIdCardNo().equals(newFetcher.getIdCardNo()))))
                    return new CommonResult(false, "该取票人姓名、电话号码或者身份证信息已经存在");
                else {

                    //这里建议修改信息时不可以改动默认信息

                    //该更改人是否为默认更改人
                    //默认信息一致
                    if (f.getIsDefault().equals(newFetcher.getIsDefault())) {
                        replace(f, newFetcher);
                    } else {
                        //默认信息不一致,修改为默认信息
                        if (newFetcher.getIsDefault()) {
                            this.fetchers.stream().forEach(i -> {
                                if (i.getIsDefault()) i.setIsDefault(false);
                            });
                            replace(f, newFetcher);
                        } else {
                            //默认信息不一致,修改bu为默认信息
                            replace(f, newFetcher);
                            //随意设置不是原来的取票人为默认取票人
                            this.fetchers.stream().forEach(i -> {
                                if (!i.getId().equals(newFetcher.getId())) {
                                    i.setIsDefault(true);
                                    return;
                                }
                            });

                        }
                    }
                    return new CommonResult(true);
                }
            }
        }
        return new CommonResult(false, "未找到匹配人");
    }

    //设置默认值
    public CommonResult resetDefault(Long fetcherId) {
        if (this.fetchers.stream().anyMatch(i -> i.getId().equals(fetcherId))) {
            this.fetchers.stream().forEach(i -> {
                if (i.getId().equals(fetcherId))
                    i.setIsDefault(true);
                else
                    i.setIsDefault(false);

            });
            return new CommonResult(true);
        }
        return new CommonResult(false, "不存在该取票人");
    }

    /**
     * 移除取件人信息
     *
     * @param fetcherId 取件人信息标识
     */
    public void removeFetchers(Long fetcherId) {

        this.fetchers.removeIf(i -> i.getId().equals(fetcherId));
    }

    /**
     * 获取到指定的取件人
     *
     * @param fetcherId
     * @return
     */
    public Fetcher getFetcherById(Long fetcherId) {
        if (this.fetchers == null || this.fetchers.isEmpty()) {
            return null;
        }
        for (Fetcher f : this.fetchers) {
            if (f.getId().equals(fetcherId)) {
                return f;
            }
        }
        return null;
    }

    /**
     * 获取游客默认取件人
     *
     * @return 已设置默认取件人则返回，否则返回NULL
     */
    public Fetcher getDefaultFetcher() {
        return this.fetchers.stream().filter(i -> i.getIsDefault()).findFirst().orElse(null);
    }

    //endregion

    //copy fetcher case
    private void replace(Fetcher oldFetcher, Fetcher newFetcher) {
        oldFetcher.setMobilePhone(newFetcher.getMobilePhone());
        oldFetcher.setIdCardNo(newFetcher.getIdCardNo());
        oldFetcher.setIsDefault(newFetcher.getIsDefault());
        oldFetcher.setRealName(newFetcher.getRealName());
        oldFetcher.setLastUpdateTime(new Date());
    }
}
