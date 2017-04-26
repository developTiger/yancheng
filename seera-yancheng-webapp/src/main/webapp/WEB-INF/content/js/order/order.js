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
   var pingppPc=  require("pingpp/pingpp-pc");

    var channel = "alipay_pc_direct";

    $("body").delegate(".js_check_pay1", "click", function () {
        $(this).find("div").removeClass("dis-none");
        $(".js_check_pay2").find("div").addClass("dis-none");
        $(".sure_order").removeClass("hidden");
        $("#wxpay").addClass("hidden");
        channel = "alipay_pc_direct";
    });

    $("body").delegate(".js_check_pay2", "click", function () {
        $(this).find("div").removeClass("dis-none");

        $(".js_check_pay1").find("div").addClass("dis-none");
        //channel = "upacp_pc";
        $("#wxpay").removeClass("hidden");
        $(".sure_order").addClass("hidden");
    });

    $("body").delegate(".sure_order", "click", function () {
        var orderNum = $("#orderNum").html();
        if(channel=="alipay_pc_direct"){

        //    window.location.href="/alipay/dopay?orderNum="+orderNum;
        //    //$.post("/ajax_alipay", {orderNum: orderNum}, function (data) {
        //    //    $("#alipay").html(decodeURI(data));
        //    //});
        //
        //}else {
            $.post("/order/pay", {channel: channel, orderNum: orderNum}, function (data) {
                data = JSON.parse(data);
                pingppPc.createPayment(data, function (result, err) {
                    if (result == "success") {
                        console.log("wx_pub pay success！");
                        //window.location.href='';//成功支付跳转
                    }
                    else if (result == "fail") {
                        // charge 不正确或者微信公众账号支付失败时会在此处返回
                        console.log("wx_pub pay success！");
                    } else if (result == "cancel") {
                        // 微信公众账号支付取消支付
                        console.log("wx_pub pay canceled！");
                    }
                    console.log(result);
                    console.log(err.msg);
                    console.log(err.extra);
                });
            });
        }
    });
});

