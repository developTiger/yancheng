/**
 * Created by zwork on 2016/8/23.
 */
define(function (require, exports, module) {

    var template = require("template");
    var addTr = require("../common/addTr");
    var tpl = require("text!order/tpl/Addressee.html");
    var couponTpl = require("text!order/tpl/couponTpl.html");
    var template = require("template");
    var myWidGet = require("../common/myWidGet");
    var uri = require("../common/uri");
    template.helper('getNormalNumber', function (str) {
        return myWidGet.getNormalNumber(str);
    })
    $(function () {
        exeCount();
        $("body").on("click", ".addBtn", function () {
            addTr("#addressseeTable", tpl);
        }).on("click", ".Js_delete", function () {
            if ($(this).closest("tr").find("input[name=id]").val() == "") {
                $(this).closest("tr").remove();
            } else {
                var id = $(this).closest("tr").find("input[name=id]").val();
                if (confirm("确定删除吗？")) {
                    $.post("/order/deleteReceiver", {id: id}, function (data) {
                        alert(data.msg || "删除成功");
                        window.location.reload(true)
                    })
                }
            }
        }).on("click", ".Js_modify", function () {
            $(this).closest("tr").find("input[type=text]").attr("disabled", false);
            $(this).addClass("hidden").siblings(".Js_confirm").removeClass("hidden")
        }).on("click", ".Js_confirm", function () {
            var that = this;
            var input = $(this).closest("tr").find("input[type=text]");
            var data = [];
            var isSuccess = 0;
            var msg = "";
            input.each(function () {
                var value = $(this).attr("name") + "=" + $(this).val();
                data.push(value);
                if (($(this).attr("name") == "idCardNo" || $(this).attr("name") == "mobilePhone" || $(this).attr("name") == "realName") && $(this).val() == "") {
                    isSuccess++;
                    msg += "所有信息必须填写完整";
                    return false;
                } else {
                    if ($(this).attr("name") == "idCardNo") {
                        if (!isCardNo($(this).val())) {
                            isSuccess++;
                            msg += "身份证号码错误\n"
                        }
                    }
                    if ($(this).attr("name") == "mobilePhone") {
                        if (!checkMobile($(this).val())) {
                            isSuccess++;
                            msg += "手机号码错误"
                        }
                    }
                }

            })
            if (isSuccess > 0) {
                alert(msg)
            } else {
                input.attr("disabled", true);
                $(this).attr("disabled", true);
                $.post("order/addReceiver", data.join("&"), function (data) {
                    if (data.isSuccess) {
                        $(that).addClass("hidden").attr("disabled", false).siblings(".Js_modify").removeClass("hidden");
                        $(that).closest("tr").find("input[name=id]").val(data.id);
                    } else {
                        alert(data.msg)
                        $(that).attr("disabled", false);
                        input.attr("disabled", false);
                    }
                })
            }

        }).on("click", "input[name=selectedReceiver]", function () {
            //判断是不是可以购买
            var ICNo=$(this).closest("tr").find("input[name=idCardNo]").val().substring(0,2);
            var list=document.getElementsByClassName("area");
            for(var i=0;i<list.length;i++){
                if(list[i].innerHTML.indexOf(ICNo)!=-1){
                    alert("抱歉！您买的票含有区域限定票，而选择持票人的身份省份不符合要求，请重新选择！");
                    this.checked = false;
                    return;
                }
            }

        }).on("click", ".Js_checkBtn", function () {
            //添加优惠券一系列操作
            var that = this;
            var num = $("input[name=num]").val();
            $(this).attr("disabled", true);
            $.post("order/checkCoupon", {num: num}, function (data) {
                if (data.isSuccess) {
                    $.post("order/getCoupon", {num: num}, function (data) {
                        addTr(".order-confirm-select", couponTpl, data);//实际要使用,目前注释
                    })
                } else {
                    alert(data.msg)
                }
                //addTr(".order-confirm-select",couponTpl,{money:100,full:200,date:'2010-10-10'});//实际要删除,目前测试
                $(that).attr("disabled", false);
                setEnable(".order-confirm-selectCoupon input[type=radio]", !$(".Js_userCoupon").prop("checked"))
            })
        }).on("click", ".Js_userCoupon", function () {
            setEnable(".order-confirm-selectCoupon input[type=radio]", !$(this).prop("checked"));
            exeCount();
        }).on("change", "input[name=coupon]", function () {
            exeCount();
        }).on("click", ".Js_order", function () {
            var fetcherId = $("input[name=selectedReceiver]:checked").closest("tr").find("input[name=id]").val();
            var couponId;
            if ($(".Js_userCoupon").prop("checked")) {
                couponId = $("input[name=coupon]:checked").attr("data-id");
            }
            if (fetcherId == undefined) {
                alert("请选择收件人")
            } else {
                var url = "/createOrder";
                if (couponId == undefined) {
                    url = uri.setParams(url, {fetcherId: fetcherId})
                } else {
                    url = uri.setParams(url, {fetcherId: fetcherId, couponId: couponId})
                }
                window.location.href = url;

            }
        });
        //调用默认持票人检测
        autodetection();

    })

    /**
     * 计算总金额
     */
    function exeCount() {
        var totalCount = +$("#totalCount").val();
        var coupon = 0;
        if ($(".Js_userCoupon").prop("checked")) {
            coupon = +($("input[name=coupon]:checked").attr("data-val") == undefined ? 0 : $("input[name=coupon]:checked").attr("data-val"));
        }
        var count = totalCount - coupon;
        $(".Js_count em").html(count.toFixed(2));
        $(".Js_coupon em").html(coupon.toFixed(2));
    }

    /**
     * 设置是否禁用
     * @param selector
     * @param enabled
     */
    function setEnable(selector, enabled) {
        $(selector).attr("disabled", enabled)
    }

    /**
     * 验证身份证
     * @param card
     * @returns {boolean}
     */
    function isCardNo(card) {
        // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
        var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
        return reg.test(card);
    }

    /**
     * 验证手机号
     * @param str
     * @returns {boolean}
     */
    function checkMobile(str) {
        var re = /^1\d{10}$/
        return re.test(str);
    }

    /**
     * 默认持票人检测
     */
    function autodetection() {
        if ($("input[name=selectedReceiver]:checked").length>0) {
            var ICNo = $("input[name=idCardNo]").val().substring(0, 2);
            var list = document.getElementsByClassName("area");
            var result = false;
            for (var i = 0; i < list.length; i++) {
                if (list[i].innerHTML.indexOf(ICNo) != -1) {
                    alert("抱歉！您买的票含有区域限定票，而默认持票人的身份省份不符合要求，请重新选择！");
                    result = true;
                    break;
                }
            }
            if (result) {
                $("input[name=selectedReceiver]:checked").attr("checked", false);
            }
        }
    }

})