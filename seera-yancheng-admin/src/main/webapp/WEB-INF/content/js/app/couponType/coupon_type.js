/**
 * Created by xiazl on 2016/9/6.
 */

define(function (require, exports, module) {

    var List = require("../common/pagelist");//分页
    var check = require("../common/checkbox");//复选框
    var wDatePicker = require("wdatePicker");//时间插件
    //widget插件作用：点击按钮，弹出模态框，对按钮进行初始化
    var widget = require("../common/widget");
    require("ajaxUpload");
    var validate = require("validate");//对表单进行验证
    require("../common/jquery.serializeObject");//
    require("validate");


    check.checkAll("body", ".checkAll", ".checkBtn")
    loadData();
    $("#searchCouponType").click(function () {
        loadData();
    });

    //加载数据
    function loadData() {
        var tpl = require("text!app/tpl/couponType/couponTypeTableTpl.html");
        var url = $("#searchForm").attr("data-url");
        var data = $("#searchForm").serialize().toString();
        List("#table", tpl, url, data, 1, 10);
    }

    //时间插件
    $("body").delegate(".Wdate", "click", function () {
        wDatePicker({dateFmt: 'yyyy-MM-dd'});
    });


    $("body").delegate(".changeStatus", "click", function () {
        var url = $(this).attr("data-url");
        var options = {
            url: url + "&t=" + new Date().getMilliseconds(),
            type: 'get',
            success: function (data) {
                if (data.isSuccess) {
                    alert("操作成功");
                    loadData();
                }
                else {
                    alert(data.msg)
                }
            }
        };
        $.ajax(options);
    });


    //新增
    var callb = function callback() {
        $(".modal-dialog").css("width", "800px");
        $("#addCouponTypeInfoForm").validate({
            submitHandler: function (form) {
                //表单提交句柄,为一回调函数，带一个参数：form
                var url = $("#addOrUpdateCouponType").attr("data-url");
                var options = {
                    url: url + "?t=" + new Date().getMilliseconds(),
                    type: 'post',
                    data: $("#addCouponTypeInfoForm").serializeObject(),
                    success: function (data) {
                        if (data) {
                            alert("保存成功");
                            $("#myModal").modal("hide");
                            loadData();
                        } else {
                            alert(data.msg)
                        }
                    }
                }
                $.ajax(options);
            }
        });
    }
    widget.init(callb);

})
