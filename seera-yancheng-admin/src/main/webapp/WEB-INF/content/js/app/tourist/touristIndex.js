/**
 * Created by liulin on 2016/7/16.
 */


define(function (require, exports, module) {
    require('../init');
    var List = require("../common/pagelist");//分页
    var check = require("../common/checkbox");//复选框
    var wDatePicker = require("wdatePicker");//时间插件
    var widget = require("../common/widget");//widget插件作用：点击按钮，弹出模态框，对按钮进行初始化
    var validate = require("validate");//对表单进行验证
    require("../common/jquery.serializeObject");//对提交的数据 进行序列化 返回一个object
    var template = require("../common/templeteHelper")


    // 手机号码验证
    jQuery.validator.addMethod("isMobile", function (value, element) {
        var length = value.length;
        var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
        return this.optional(element) || (length == 11 && mobile.test(value));
    }, "请正确填写您的手机号码");
    // 身份证号码验证
    jQuery.validator.addMethod("isIdCardNo", function(value, element) {
        //var idCard = /^(\d{6})()?(\d{4})(\d{2})(\d{2})(\d{3})(\w)$/;
        return this.optional(element) || isIdCardNo(value);
    }, "请输入正确的身份证号码。");

    check.checkAll("body", ".checkAll", ".checkBtn");
    //加载数据
    function loadData() {
        var tpl = require("text!app/tpl/tourist/touristTpl.html");
        var url = $("#searchForm").attr("data-url");
        var data = $("#searchForm").serialize().toString();
        List("#table", tpl, url, data, 1, 10);
    }

    loadData();
    $("#touristQueryBtn").click(function () {
        loadData();
    });

    //时间插件
    $("body").delegate(".Wdate", "click", function () {
        wDatePicker({dateFmt: 'yyyy-MM-dd'});
    }).delegate(".disabledStatus", "click", function () {
        var id = $(this).attr("data-value");
        $.ajax({
            url: "ajax_disabled_touristStatus" + "?t=" + new Date().getMilliseconds() + "&id=" + id,
            type: 'get',
            success: function (data) {
                if (data.isSuccess) {
                    //alert("状态设置成功");
                    loadData();
                }
            }
        })
    }).delegate(".enableStatus", "click", function () {
        var id = $(this).attr("data-value");
        $.ajax({
            url: "ajax_enable_touristStatus" + "?t=" + new Date().getMilliseconds() + "&id=" + id,
            type: 'get',
            success: function (data) {
                if (data.isSuccess) {
                    //alert("状态设置成功");
                    loadData();
                }
            }
        })
    });


    //新增 在弹窗出来之后 callback回调函数触发
    var callb = function callback() {
        $("#addTouristInfoForm").validate({

            rules: {
                userName: {
                    required: true
                },
                email: {
                    email: true
                },
                mobilePhone: {
                    isMobile: true
                },
                idCardNo: {
                    isIdCardNo: true
                }
            },
            messages: {
                userName: {
                    required: "必填"
                },
                email: {
                    email: "必须输入正确格式的电子邮件"
                },
                mobilePhone: {
                    isMobile: "请正确填写您的手机号码"
                },
                idCardNo: {
                    isIdCardNo: "请正确输入您的身份证号码"
                }

            },

            submitHandler: function (form) {
                //表单提交句柄,为一回调函数，带一个参数：form
                var _url = $("#addOrUpdateTourist").attr("data-url");

                var options = {
                    url: _url + "?t=" + new Date().getMilliseconds(),
                    type: 'post',
                    data: $("#addTouristInfoForm").serializeObject(),
                    success: function (data) {
                        if (data.isSuccess)
                            $("#myModal").modal("hide")
                        loadData();
                    }
                };
                $.ajax(options);
            }
        });

        $("#resetPasswordForm").validate({

            rules: {
                password: {
                    required: true
                },
                rePassword:{
                    required : true,
                    equalTo:"#password"
                }
            },
            messages: {
                password: {
                    required: "必填"
                },
                rePassword: {
                    required: "必填",
                    equalTo:"输入不一致"
                }
            },

            submitHandler: function (form) {
                //表单提交句柄,为一回调函数，带一个参数：form
                var _url = $("#resetPassword").attr("data-url");

                var options = {
                    url: _url + "?t=" + new Date().getMilliseconds(),
                    type: 'post',
                    data: $("#resetPasswordForm").serializeObject(),
                    success: function (data) {
                        if (data.isSuccess)
                            $("#myModal").modal("hide")
                        loadData();
                    }
                };
                $.ajax(options);
            }
        });
    }

    widget.init(callb);
});

//身份证号码的验证规则
function isIdCardNo(num) {
    //if (isNaN(num)) {alert("输入的不是数字！"); return false;}
    var len = num.length, re;
    if (len == 15)
        re = new RegExp(/^(\d{6})()?(\d{2})(\d{2})(\d{2})(\d{2})(\w)$/);
    else if (len == 18)
        re = new RegExp(/^(\d{6})()?(\d{4})(\d{2})(\d{2})(\d{3})(\w)$/);
    else {
        //alert("输入的数字位数不对。");
        return false;
    }
    var a = num.match(re);
    if (a != null) {
        if (len == 15) {
            var D = new Date("19" + a[3] + "/" + a[4] + "/" + a[5]);
            var B = D.getYear() == a[3] && (D.getMonth() + 1) == a[4] && D.getDate() == a[5];
        }
        else {
            var D = new Date(a[3] + "/" + a[4] + "/" + a[5]);
            var B = D.getFullYear() == a[3] && (D.getMonth() + 1) == a[4] && D.getDate() == a[5];
        }
        if (!B) {
            //alert("输入的身份证号 "+ a[0] +" 里出生日期不对。");
            return false;
        }
    }
    if (!re.test(num)) {
        //alert("身份证最后一位只能是数字和字母。");
        return false;
    }
    return true;
}
