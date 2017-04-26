/**
 * Created by xiazl on 2016/9/8.
 */
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
    $("#searchCoupon").click(function () {
        loadData();
    });

    //加载数据
    function loadData() {
        var tpl = require("text!app/tpl/coupon/couponTableTpl.html");
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
    $("body").delegate("#upload", "click", function () {

        $.ajaxFileUpload({
            //处理文件上传操作的服务器端地址(可以传参数,已亲测可用)
            url: 'ajax_coupon_upload',
            secureuri: false,                       //是否启用安全提交,默认为false
            fileElementId: ['upFile'],           //文件选择框的id属性
            dataType: 'text',
            success: function (data, status) {        //服务器响应成功时的处理函数
                var resultStart = data.indexOf("{");
                var resultEnd = data.indexOf("}");
                var result = JSON.parse(data.substring(resultStart, resultEnd + 1));

                if (result.isSuccess) {     //0表示上传成功(后跟上传后的文件路径),1表示失败(后跟失败描述)
                    alert("上传文件成功！");
                    loadForm();
                } else {
                    alert(result.msg);
                }
            },
            error: function (data, status, e) { //服务器响应失败时的处理函数
                $('#result').html('文件上传失败，请重试！！');
            }
        });
    })

    //新增
    var callb = function callback() {
        $(".modal-dialog").css("width", "800px");
        $("#addCouponInfoForm").validate({
            submitHandler: function (form) {
                //表单提交句柄,为一回调函数，带一个参数：form
                var url = $("#addOrUpdateCoupon").attr("data-url");
                var options = {
                    url: url + "?t=" + new Date().getMilliseconds(),
                    type: 'post',
                    data: $("#addCouponInfoForm").serializeObject(),
                    success: function (data) {
                        if (data.isSuccess) {
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
