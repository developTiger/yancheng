/**
 * Created by liulin on 2016/7/16.
 */

define(function (require, exports, module) {

    //widget插件作用：点击按钮，弹出模态框，对按钮进行初始化
    var widget = require("../common/widget");
    //订单审核
    var callb = function callback() {
        $("#addCarInfoForm").validate({

            rules: {
                carType: {
                    required: true,
                    maxlength: 50
                },
                carNo: {
                    required: true,
                    maxlength: 50
                    //digits:true
                },
                companyId: {
                    required: true
                },
                status: {
                    required: true
                },
                repairLog: {
                    required: true
                },
                controlName: {
                    required: true,
                    maxlength: 50
                }
            },
            messages: {
                carType: {
                    required: "必填",
                    maxlength: "长度不能超过50个字符"
                },
                carNo: {
                    required: "必填",
                    maxlength: "长度不能超过50个字符"

                },
                companyId: {
                    required: "必须选择"
                },
                status: {
                    required: "必须选择"
                },
                repairLog: {
                    required: "必填"
                },
                controlName: {
                    required: "必填",
                    maxlength: "长度不能超过50个字符"
                }

            },

            submitHandler: function (form) {
                //表单提交句柄,为一回调函数，带一个参数：form
                var _url = $("#addOrUpdateCar").attr("data-url");

                var options = {
                    url: _url + "?t=" + new Date().getMilliseconds(),
                    type: 'post',
                    data: $("#addCarInfoForm").serializeObject(),
                    success: function (data) {
                        if (data == "success")
                            alert("新增成功");
                        $("#myModal").modal("hide");
                        window.location.reload(true);
                    }
                };
                $.ajax(options);
            }

        });

    }
    widget.init(callb);




    $("body").delegate(".orderOperator", "click", function () {
        var op = $(this).find("span i").html();
        if(confirm("确定要做"+op+"操作？")) {
            var _url = $(this).attr("data-url");

            var options = {
                url: _url + "&t=" + new Date().getMilliseconds(),
                type: 'get',
                success: function (data) {
                    if (data.isSuccess) {
                        alert("操作成功");
                        window.location.reload();
                    } else {
                        alert(data.msg);
                        window.location.reload();
                    }
                }
            };
            $.ajax(options);
        }
    });
    $(".showDetail").click(function () {
        if (!$(this).parent().parent().next().hasClass("hidden")) {
            $(this).parent().parent().next().addClass("hidden");

        }else {
            $(".js_table").addClass("hidden");

            $(this).parent().parent().next().removeClass("hidden");
        }

    })

})
