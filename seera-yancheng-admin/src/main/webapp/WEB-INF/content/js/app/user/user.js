/**
 * Created by zhouz on 2016/5/15.
 */
define(function (require, exports, module) {
    require('../init');
    var template = require("template");
    var List = require("../common/pagelist");
    require("../common/jquery.serializeObject");
    var widget = require("../common/widget");
    var check = require("../common/checkbox");
    var validate = require("validate");
    require("iCheck");

    $('input').iCheck({
        checkboxClass:'icheckbox_flat-red',  //每个风格都对应一个，这个不能写错哈。
        radioClass: 'icheckbox_flat-red'
    });
    check.checkAll("body", ".checkAll", ".checkBtn")
    loadData();
    $("#empQueryBtn").click(function () {
        loadData();
    });


    function loadData() {
        var tpl = require("text!app/tpl/uauth/userTableTpl.html");
        var url = $("#searchForm").attr("data-url");
        var data = $("#searchForm").serialize().toString();
        List("#table", tpl, url, data, 1, 10);
    }

    $("body").delegate("#cancelModal", "click", function () {
        $("#myModal").modal("hide")
    });
    $("#disableUser").click(function () {
        var chk_value = [];
        $('.checkBtn:checked').each(function () {
            chk_value.push($(this).val());
        });
        setUserStaus(chk_value.toString(), false);
    })
    $("#enableUser").click(function () {
        var chk_value = [];
        $('.checkBtn:checked').each(function () {
            chk_value.push($(this).val());
        });
        setUserStaus(chk_value.toString(), true);
    })

    function setUserStaus(selectids, isActive) {
        var options = {
            url: "ajax_setUserStstus?t=" + new Date().getMilliseconds(),
            type: 'post',
            data: {isActive: isActive, ids: selectids},
            success: function (data) {
                if (data == "success") {
                    alert("修改成功");
                    loadData();
                }
                else
                    alert(data);
            }
        };
        $.ajax(options);
    }

    var vali= function validates() {

        // 联系电话(手机/电话皆可)验证
        jQuery.validator.addMethod("isTel", function(value, element) {
            var length = value.length;
            var mobile =/^1\d{10}$/;
            return  (length == 11 && mobile.test(value));
        }, "");


        $("#addUserform").validate({
            rules: {
                loginName: {
                    required: true
                },
                email: {
                    required: true,
                    email: true
                },
                realName: {
                    required: true
                },
                phone:{
                    required:true,
                    isTel:$("#middle-name").val()
                }
            },
            messages: {
                loginName: {
                    required: "必填"
                },
                email: {
                    required: "必填",
                    email: "E-Mail格式不正确"
                },
                realName: {
                    required: "不能为空"
                },
                phone:{
                    required:"必填",
                    isTel:"请输入正确的手机验证格式"
                }
            },
            submitHandler: function (form) {   //表单提交句柄,为一回调函数，带一个参数：form
                var _url = $("#addOrUpdateUser").attr("data-url");
                var options = {
                    url: _url + "?t=" + new Date().getMilliseconds(),
                    type: 'post',
                    data: $("#addUserform").serializeObject(),
                    success: function (data) {
                        if (data == "success")
                            alert("新增成功");
                        $("#myModal").modal("hide")
                        loadData();
                    }
                };
                $.ajax(options);
            }
        })
    }
    widget.init(vali);
});