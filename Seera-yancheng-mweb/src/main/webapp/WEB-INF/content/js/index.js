/**
 * Created by jade on 16/8/23.
 */
$(function () {
    $(".swiper-container").swiper({autoplay: 3000});
    $("a.Js_spinner").bind("update", function (event, target, val) {
        calculateCart($(this));

    })

    //$(".js_validate1").click(function(){
    //   var container=$(this).parent().parent().parent();
    //    return checkNaN(container);
    //});
    //
    //$(".js_validate2").click(function(){
    //    var container=$(this).parent().parent().parent();
    //    return checkNaN(container);
    //});
    $("body").delegate(".js_validate", "click", function () {
        return checkNaN($(".rec_form"));
    });

    function showMessage(msg) {
        $.alert(msg);
        return false;
    }

    function checkNaN(target) {
        var temp = true;
        var count = 0;
        //target.find("div ul li div div div input[type=text]").each(function(){

        var name = $("input[name=realName]").val();

        if (name == "") {
            temp = false;
            return showMessage("姓名不能为空");
        }
        var cardNo = $("input[name=cardNo]").val();
        if (cardNo == "") {
            temp = false;
            return showMessage("身份证不能为空");
        } else {
            if (!IdentityCodeValid(cardNo)) {
                temp = false;
                return showMessage("身份证信息错误");
            }
            ;
        }
        var mobilePhone = $("input[name=mobilePhone]").val();
        if (mobilePhone == "") {
            temp = false;
            return showMessage("手机号码不能为空");
        } else {
            if (!checkPhone(mobilePhone)) {
                temp = false;
                return showMessage("手机号码有误，请重填");
            }
        }
        return temp;
    };


    function checkPhone(phone) {
        if (!(/^1[34578]\d{9}$/.test(phone))) {
            return false;
        }
        return true;
    }

    function IdentityCodeValid(code) {
        var city = {
            11: "北京",
            12: "天津",
            13: "河北",
            14: "山西",
            15: "内蒙古",
            21: "辽宁",
            22: "吉林",
            23: "黑龙江 ",
            31: "上海",
            32: "江苏",
            33: "浙江",
            34: "安徽",
            35: "福建",
            36: "江西",
            37: "山东",
            41: "河南",
            42: "湖北 ",
            43: "湖南",
            44: "广东",
            45: "广西",
            46: "海南",
            50: "重庆",
            51: "四川",
            52: "贵州",
            53: "云南",
            54: "西藏 ",
            61: "陕西",
            62: "甘肃",
            63: "青海",
            64: "宁夏",
            65: "新疆",
            71: "台湾",
            81: "香港",
            82: "澳门",
            91: "国外 "
        };
        var tip = "";
        var pass = true;

        if (!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)) {
            tip = "身份证号格式错误";
            pass = false;
        }

        else if (!city[code.substr(0, 2)]) {
            tip = "地址编码错误";
            pass = false;
        }
        else {
            //18位身份证需要验证最后一位校验位
            if (code.length == 18) {
                code = code.split('');
                //∑(ai×Wi)(mod 11)
                //加权因子
                var factor = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
                //校验位
                var parity = [1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2];
                var sum = 0;
                var ai = 0;
                var wi = 0;
                for (var i = 0; i < 17; i++) {
                    ai = code[i];
                    wi = factor[i];
                    sum += ai * wi;
                }
                var last = parity[sum % 11];
                if (parity[sum % 11] != code[17]) {
                    tip = "校验位错误";
                    pass = false;
                }
            }
        }
        return pass;
    }

    $(".Js_spinner").each(function () {
        var defaults = {value: 1, min: 1};
        container = $(this).parent();
        var textField = $(this).addClass('value').attr('maxlength', '2').val(defaults.value);
        var increaseButton = $(this).next().click(function () {
            changeValue(1)
        });// $('<input class="increase" value="+">').click(function () { changeValue(1) })
        var decreaseButton = $(this).prev().click(function () {
            changeValue(-1)
        });//$('<input class="decrease"   value="-">').click(function () { changeValue(-1) })
        function changeValue(delta) {
            textField.text(getValue() + delta);
            validateAndTrigger(textField);
        }

        function validateAndTrigger(field) {
            var value = validate(field);
            textField.trigger('update', [field, value]);
        }

        function validate(field) {
            var value = getValue();
            if (value <= defaults.min) decreaseButton.attr('disabled', 'disabled');
            else decreaseButton.removeAttr('disabled');
            return value;
        }

        function getValue(field) {
            field = field || textField;
            return parseInt(field.text() || 0, 10);
        }
    })


    function initDate() {


        //page-shoppingCart


        var date = new Date();
        var tmpdate = date.setDate(date.getDate() - 1);
        var strDate = dateFormat(new Date(), "yyyy-MM-dd");
        var strtmpdate = dateFormat(tmpdate, "yyyy-MM-dd");
        var minDate = strtmpdate;
        var proCt = $("#productCt").val();
        if (proCt == "TimeSpan") {
            var sdate = $("#productCtDate").attr("data-sdt");
            var edate = $("#productCtDate").attr("data-edt");


            if (sdate < strDate) {
                sdate = strDate;

            }
            else {
                var startDate = new Date(sdate);
                minDate = dateFormat(startDate.setDate(startDate.getDate() - 1), "yyyy-MM-dd");
            }

            $("#Js_tourScheduleDate").val(sdate);
            if (sdate == edate) {
                $("#Js_tourScheduleDate").attr("disabled", true);

            } else {
                $("#Js_tourScheduleDate").calendar({
                    minDate: minDate,
                    maxDate: edate,
                    value: [sdate]
                });
            }
        }
        if (proCt == "Today") {
            $("#Js_tourScheduleDate").val(strDate);
            $("#Js_tourScheduleDate").attr("disabled", true);
        }

        if (proCt == "Nomal") {
            var nowdate = new Date();

            var strNowdate = dateFormat(nowdate.setDate(nowdate.getDate() + 1), "yyyy-MM-dd")
            $("#Js_tourScheduleDate").val(strNowdate);

            //$("#Js_tourScheduleDate").calendar({
            //    minDate: strDate,
            //    value: [strNowdate]
            //});
        }
        //if(proCt != "Nomal") {
            if ($("#Js_tourScheduleDate").length > 0) {
                $("#Js_tourScheduleDate").val(strDate);
                $("#Js_tourScheduleDate").calendar({
                    minDate: strtmpdate,
                    value: [strDate]
                });
            }


            if ($("#Js_hotelScheduleDate").length > 0) {
                $("#Js_hotelScheduleDate").val(strDate);
                $("#Js_hotelScheduleDate").calendar({
                    minDate: strtmpdate,
                    value: [strDate]
                });
            }


        $(".js_scCartDate").calendar({
            minDate: strtmpdate
        });

    }

    initDate();
    $(document).on("pageInit", function (e, pageId, $page) {

        if (pageId == "activeDetailIndex") {
            $(".details a").addClass("external");
        }
        if (pageId == "changeDate") {
            var minDate = dateFormat(new Date(), "yyyy-MM-dd");
            $(".Js_changeDate").calendar({
                minDate: minDate
            });
        }
        if (pageId == "comments") {

            var sufuStar = function () {
                function getIndex(event) {  //兼容IE
                    var e = event || window.event;
                    var t = e.target || e.srcElement;
                    if (t.tagName.toLowerCase() === 'a') {
                        return parseInt(t.innerHTML);
                    }
                }

                function showInfo(target, index, msg) {
                    var info = target;
                    info.css("display", "block")
                    info.css("left", index * 21 - 51 + 'px')

                    info.html("<strong> " + index + '分 ' + msg[index - 1].match(/(.+)\|/)[1] + '<br />' + '</strong>' + msg[index - 1].match(/\|(.+)/)[1]);
                }

                function appenStar(elem, nums) {
                    var frag = document.createElement("div");  //为了提高性能,因使用DocumentFragment一次性append,这样页面只重新渲染一次
                    for (var i = 0; i < nums; i++) {
                        var a = document.createElement('a');
                        a.innerHTML = i + 1;
                        a.href = "javascript:;"; //阻止浏览器的点击链接的默认行为

                        frag.appendChild(a);
                    }
                    elem.each(function () {
                        $(this).html(frag.innerHTML);
                    })


                }

                //主体函数
                function star(num, myMsg) {
                    var n = num || 5;  //当num有值则取其值,无值则取默认值5;
                    var clickStar = curentStar = 0; //clickStar保存点击状态
                    var msg = myMsg || [
                            "很不满意",
                            "不满意",
                            "一般",
                            "满意",
                            "非常满意"
                        ];
                    var starContainer = $('.star-div');
                    appenStar(starContainer, n);
                    starContainer.on("click", function () {
                        if (getIndex(event)) {
                            var index = getIndex(event);
                            clickStar = index;  //保存点击状态
                            var target = $(this);
                            target.next().html("<strong>" + msg[index - 1] + '</strong>');

                            target.parent().find(".scores").val(index);
                            change(target, index);
                        }
                    });

                    function change(target, index) {
                        curentStar = index || clickStar;
                        target.children().each(function () {
                            var i = parseInt($(this).html());
                            $(this).attr("class", i <= curentStar ? 'on' : '');
                        })
                    }
                }

                return {
                    star: star
                }
            }(); //这里的()表示函数立即执行,这样变量sufuStar才能调用匿名函数的返回值star
            sufuStar.star();
        }
    }).on("click", ".Js_addCart", function () {

        if ($("#Js_hotelScheduleDate").length > 0) {
            if ($("#Js_hotelScheduleDate").val() == "") {
                $.alert("请选择入住日期！");
                return;
            }

        }
        if ($("#Js_tourScheduleDate").length > 0) {
            if ($("#Js_tourScheduleDate").val() == "") {
                $.alert("请选择游玩日期！");
                return;
            }

        }

        $.ajax({
            type: "post",
            url: "/order/shoppingCart",
            data: {
                id: $("#Js_ticket_id").val(),
                num: $("#Js_ticket_num").html(),
                hotelScheduleDate: $("#Js_hotelScheduleDate").val(),
                tourScheduleDate: $("#Js_tourScheduleDate").val()
            },
            success: function () {
                window.location.href = '/order/shoppingCart.html';
            },
            error: function () {
            }
        })
    }).on("click", ".Js_deleteItem", function () {
        var id = $(this).attr("data-id");
        $.ajax({
            type: "post",
            url: "/order/shoppingCart/del/" + id,
            success: function () {
                window.location.href = '/order/shoppingCart.html';
            },
            error: function () {

            }
        })
    }).on("click", ".Js_buy", function () {
        if ($("#Js_hotelScheduleDate").length > 0) {
            if ($("#Js_hotelScheduleDate").val() == "") {
                $.alert("请选择入住日期！");
                return;
            }

        }
        if ($("#Js_tourScheduleDate").length > 0) {
            if ($("#Js_tourScheduleDate").val() == "") {
                $.alert("请选择游玩日期！");
                return;
            }

        }
        var id = $("#Js_ticket_id").val();
        var hotDate = $("#Js_hotelScheduleDate").val();
        var ticDate = $("#Js_tourScheduleDate").val();
        var num = $("#Js_ticket_num").html();


        $.ajax({
            type: "post",
            url: "/order/buyNow",
            data: {
                pid: id, tsd: ticDate, hsd: hotDate, num: num
            },
            success: function (data) {
                if (data.isSuccess) {
                    window.location.href = '/order/confirm';
                }
                else {

                    $.alert(data.msg);
                }


            }
        })

    }).on("click", ".Js_receiver_del", function () {
        var $this = $(this);
        $.ajax({
            type: "post",
            url: "/uc/receiver/del",
            data: {
                id: $this.attr("data-id")
            },
            success: function () {
                window.location.href = '/uc/receiverlist.html';
            }
        })
    }).on("change", ".Js_shopping_cart_all", function () {
        $(".Js_shopping_cart_item").prop("checked", $(this).prop("checked"));
        calculateCart();
    }).on("click", ".Js_shopping_cart_item", function () {
        $(".Js_shopping_cart_all").prop("checked", $(".Js_shopping_cart_item:checked").length == $(".Js_shopping_cart_item").length);
        calculateCart()
    }).on("click", ".Js_go_confirm", function () {
        var idWithCount = {};
        var orderProducts = [];
        var $shoppingItems = $(".Js_shopping_cart_item:checked");
        if ($shoppingItems.length == 0) {
            $.alert("请选择要结算的商品！");
            return;
        }
        $shoppingItems.each(function (index) {


            idWithCount[$(this).attr("data-name")] = $(this).attr("data-count");
            var cset = $(this).closest(".card");
            var obj = cset.find(".Js_spinner");
            var id = Number(obj.attr("data-id"));
            var count = Number(obj.html());
            var hotelDate = cset.find(".Js_hotelScheduleDate").val();
            var tureDate = cset.find(".Js_tourScheduleDate").val();

            orderProducts.push("orderProducts[" + index + "].productId=" + id);
            orderProducts.push("orderProducts[" + index + "].count=" + count);
            if (tureDate != undefined)
                orderProducts.push("orderProducts[" + index + "].tourScheduleDate=" + tureDate);
            if (hotelDate != undefined)
                orderProducts.push("orderProducts[" + index + "].hotelScheduleDate=" + hotelDate);

        })
        var $this = $(this);
        $.ajax({
            type: "post",
            url: "/order/ajax_confirm",
            data: orderProducts.join("&"),
            success: function () {
                window.location.href = '/order/confirm';
            }
        })
    }).on("click", ".Js_confirmOrder", function () {
        var fetcherId = $("#fetcherId").val();

        var couponId = $("#couponId").val();
        if (!$("#couponId").val() == undefined) {
            couponId = "";
        }
        var url = $(this).attr("data-url");
        if (fetcherId == undefined || fetcherId == "")
            alert("请选择收件人");
        else if (fetcherId == undefined && url == "/order/createOrder") {
            alert("请选择收件人")
        } else {
            var result = false;
            var idcard = $("#fetcherIdCard").val().substring(0, 2).toString();
            $(".will-comfirm-item").each(function () {
                if ($(this).attr("data-price").indexOf(idcard) != -1) {
                    alert($(this).text() + "商品对" + $(this).attr("data-value") + "区域限购！");
                    result = true;
                    //return false;
                }
            });

            if (!result) {
                if (couponId != "") {
                    $.ajax({
                        type: "get",
                        url: "/uc/ajax_check_conpon?id=" + couponId,
                        success: function (data) {
                            if (data.isSuccess) {

                                if ($("#isConfirmed").val() == "false") {
                                    $("#isConfirmed").val("true")
                                    url = url + "?fetcherId=" + fetcherId + "&couponId=" + couponId;
                                    window.location.href = url;
                                }
                                else {
                                    $.confirm("订单已提交，直接去付款？", "提示",
                                        function () {
                                            window.location.href = "/uc/order/list/waitPay.html";
                                        }
                                    );

                                }
                            } else {
                                $.alert(data.msg);
                            }

                        }
                    })
                } else {
                    if ($("#isConfirmed").val() == "false") {
                        $("#isConfirmed").val("true")
                        url = url + "?fetcherId=" + fetcherId + "&couponId=" + couponId;
                        window.location.href = url;
                    }
                    else {
                        $.confirm("订单已提交，直接去付款？", "提示",
                            function () {
                                window.location.href = "/uc/order/list/waitPay.html";
                            }
                        );

                    }
                }
            }

        }
    }).on("click", ".Js_checkFetcher", function () {
        var checked = '<div class="col-5 Js_checked">  <strong><span class="icon icon-check" style="color:lawngreen"></span></strong></div>'
        $(".Js_checked").remove();
        var fid = $("#fetcherId").val();
        if (fid != $(this).attr("data-id")) {
            $("#isConfirmed").val("false")
        }
        $("#fetcherId").val($(this).attr("data-id"));

        $("#fetcherItem").html($(this).find(".checkItem").html());
        $(".Js_checked").remove();
        $(this).find(".checkItem").before(checked);

        $.router.back();


    }).on("click", ".Js_checkCoupon", function () {
        var checked = '<div class="col-5 Js_couponChecked">  <strong><span class="icon icon-check" style="color:lawngreen"></span></strong></div>'

        $("#couponId").val($(this).attr("data-id"));

        $("#couponChecked").html($(this).find(".checkItem").html());
        $(".Js_couponChecked").remove();
        $(this).find(".checkItem").before(checked);

        $.router.back();
    }).on("click", ".Js_addReceivers", function () {
        var url = $(this).attr("data-url");
        var couponId = $("#couponId").val();
        if (couponId)
            url = url + "?couponId=" + couponId;
        $.router.load(url, true);
    });
    $("body").delegate(".pull-to-refresh-content", "refresh", function () {
        setTimeout(function () {
            window.location.reload();
        }, 1000);
    })


    $("#confirm_conpon").click(function () {
        if ($("#uconponNum").val() != "") {
            $.ajax({
                type: "post",
                url: "/uc/ajax_confirm_conpon",
                data: {uconponNum: $("#uconponNum").val()},
                success: function (data) {

                    if (data.isSuccess) {


                        var html = '<div class="col-20 icon icon-gift" style="color: #f6383a"></div>' +
                            '<span class="col-80">   满  <span>' + data.t.useCondition + '</span>   减  <span>' + data.t.quota + '</span></span>';
                        $("#couponId").val(data.t.id);
                        $("#couponChecked").append(html);
                    } else {
                        if (data.msg != "")
                            $.alert(data.msg);
                        else
                            $.alert("优惠券绑定失败！")
                    }

                }
            })
        }

    });
    $.init()

    function calculateCart(target) {
        if (target) {
            var currentTotalPrice = target.attr("data-price") * target.closest(".card").find(".Js_spinner").text()
            target.closest(".card").find(".ui-rmb-discount").html("总价： ¥" + toDecimal2(currentTotalPrice))
        }
        var $shoppingItems = $(".Js_shopping_cart_item:checked");
        $("#Js_shoppingcart_count").text($shoppingItems.length)

        var totalPrice = 0;
        $shoppingItems.each(function () {
            totalPrice += +$(this).attr("data-price") * $(this).closest(".card").find(".Js_spinner").text();
        })
        $("#Js_shoppingcart_total_price").text(totalPrice);
    }

    function toDecimal2(x) {
        var f = parseFloat(x);
        if (isNaN(f)) {
            return false;
        }
        var f = Math.round(x * 100) / 100;
        var s = f.toString();
        var rs = s.indexOf('.');
        if (rs < 0) {
            rs = s.length;
            s += '.';
        }
        while (s.length <= rs + 2) {
            s += '0';
        }
        return s;
    }

    function getDate() {
        var result = [];
        var date = new Date();

        result.push(date.getFullYear())
        result.push(date.getMonth() + 1)
        result.push(date.getDate())
        result.push(date.getHours())
        result.push(date.getMinutes())
        result.push(date.getSeconds())
        return result;
    }

    $('img').error(function () {
        $(this).attr("src", "/images/default.png");
    });
    $.fn.serializeObject = function () {
        var o = {};
        var a = this.serializeArray();

        $.each(a, function () {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };

    $("#commentBtn").click(function () {
        $.ajax({
            url: "/ajax_product_evaluation?t=" + new Date().getMilliseconds(),
            type: 'post',
            data: $("#commentForm").serializeObject(),
            success: function (data) {
                if (data.isSuccess) {
                    alert("评价成功");
                    window.location.href = "/uc/order/list/payed.html";
                } else {
                    alert("评价失败");
                }
            }

        });
    })


    $("#changeBtn").click(function () {
        $.ajax({
            url: "/ajax_update_date?t=" + new Date().getMilliseconds(),
            type: 'post',
            data: $("#changeDateForm").serializeObject(),
            success: function (data) {
                if (data.isSuccess) {
                    alert("改签提交成功，请等待审核！");
                } else {
                    alert("改签失败!" + data.msg);
                }
                window.location.href = "/uc/order/list/payed.html";
            }

        });
    })

    $("#changeUserInfo").click(function () {
        if ($("#password").length > 0) {
            if ($("#password").val() == $("#repassword").val()) {

                $("#changeUserInfoForm").submit();
            }
            else {
                alert("两次输入密码不一致!");
            }
        } else {
            $("#changeUserInfoForm").submit();
        }

    });
    function dateFormat(date, format) {
        if (!date)return;
        if (!format) {
            format = "yyyy-MM-dd hh:mm:ss";
        }
        date = new Date(date);
        var map = {
            "M": date.getMonth() + 1, //月份
            "d": date.getDate(), //日
            "h": date.getHours(), //小时
            "m": date.getMinutes(), //分
            "s": date.getSeconds(), //秒
            "q": Math.floor((date.getMonth() + 3) / 3), //季度
            "S": date.getMilliseconds() //毫秒
        };

        format = format.replace(/([yMdhmsqS])+/g, function (all, t) {
            var v = map[t];
            if (v !== undefined) {
                if (all.length > 1) {
                    v = '0' + v;
                    v = v.substr(v.length - 2);
                }
                return v;
            }
            else if (t === 'y') {
                return (date.getFullYear() + '').substr(4 - all.length);
            }
            return all;
        });
        return format;
    }
});

function wap_pay(channel) {
    // if (typeof PINGPP_IOS_SDK !== 'undefined') {
    //     PINGPP_IOS_SDK.callPay(channel, amount);
    // } else if (typeof PINGPP_ANDROID_SDK !== 'undefined') {
    //     PINGPP_ANDROID_SDK.callPay(channel, amount);
    // }

    var orderNum = $("#orderNum").html();
    $.post("/order/pay", {channel: channel, orderNum: orderNum}, function (data) {
        pingpp.setAPURL('/ali_wx_pay.htm');

        debugger;
        pingpp.createPayment(data, function (result, err) {
            if (result == "success") {
                console.log("wx_pub pay success！");
                window.location.href = "/order/pay/success?order_no=" + orderNum;//成功支付跳转
            }
            else if (result == "fail") {
                // charge 不正确或者微信公众账号支付失败时会在此处返回
                console.log("wx_pub pay success！");
                window.location.href = "/order/pay/error?order_no=" + orderNum;
                ;//
            } else if (result == "cancel") {
                // 微信公众账号支付取消支付
                console.log("wx_pub pay canceled！");
                window.location.href = "/order/pay/error?order_no=" + orderNum;
                ;//
            }
            console.log(result);
            console.log(err.msg);
            console.log(err.extra);
        });
    });

};