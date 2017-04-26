/**
 * Created by zhaowy on 2016/9/9.
 */
define(function (require, exports, module) {
    require("../common/jquery.serializeObject");//对提交的数据 进行序列化 返回一个object
    var tpl_itemTake = require("text!app/tpl/orderManager/orderItemTakeTpl.html");
    require("../common/templeteHelper");

    String.prototype.startWith = function (compareStr) {
        return this.indexOf(compareStr) == 0;
    };
    document.onkeydown = function () {
        if (event.keyCode == 13) {
            $("#readItemBtn").click();
        }

    };

    var codes = [];
    var cleanFlag = false;
    $("body").delegate("#readItemBtn", "click", function () {//扫码获取商品项
        var actionUrl = "";
        var num = $("#takeNum").val().trim().toUpperCase();
        if (cleanFlag) {
            $("#orderItemsPlaceHolder").html("");
            codes = [];
        }
        if (num == "") {
            $("#msg").html("请输入消费编码！");
            cleanFlag = true;
            return;
        }
        if (codes.indexOf(num) >= 0) {
            $("#msg").html("该消费码已扫描，请勿重复扫描！");

            return;

        }
        if (num.startWith("http")) {
            actionUrl = num + "&t=" + new Date().getMilliseconds();
            num = null;
        }
        else {
            actionUrl = $(this).attr("data-url");
            actionUrl += "?t=" + new Date().getMilliseconds();
        }
        var options = {
            url: actionUrl,
            type: 'post',
            data: {takeNum: num},
            success: function (data) {
                if (data.isSuccess) {
                    $("#msg").html("");
                    codes.push(num);
                    if (0 == $(".hiddenTakeNum[value= '" + num + "']").length) {
                        if (data.t.haveTaked) {
                            cleanFlag = true;
                        }
                        else
                            cleanFlag = false;
                        var html = template.compile(tpl_itemTake)(data);
                        if (cleanFlag)
                            $("#orderItemsPlaceHolder").html(html);
                        else
                            $("#orderItemsPlaceHolder").append(html);
                    }
                } else
                    $("#msg").html(data.msg);
            }
        };
        $.ajax(options);
    }).delegate("#orderProductQueryBtn", "click", function () {//核销商品
        var actionUrl = $(this).attr("data-url");
        cleanFlag = true;
        if ($(".hiddenTakeNum") != null) {
            $(".hiddenTakeNum").each(function () {
                var thist = $(this);
                var takeNum = $(this).val();
                var options = {
                    url: actionUrl,
                    type: 'post',
                    data: {takeNum: takeNum},
                    success: function (data) {

                        if (data.isSuccess)
                            thist.parent().parent().children("#info").html("<span style='color: #985f0d;font-weight: 600'  >已领取</span>");
                        else    thist.parent().parent().children("#info").html("<span class='red'>" + data.msg + "</span>");
                    }
                };
                $.ajax(options);
            });
        }
    });
});