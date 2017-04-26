/**
 * Created by liulin on 2016/7/16.
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
    require("kindeditor");

    check.checkAll("body", ".checkAll", ".checkBtn")
    loadData();
    function loadData() {
        var tpl = require("text!app/tpl/activity/activityTableTpl.html");
        var url = $("#searchForm").attr("data-url");
        var data = $("#searchForm").serialize().toString();
        List("#table", tpl, url, data, 1, 10);
    };


    $("#editForm").validate({
        submitHandler: function (form) {
            //表单提交句柄,为一回调函数，带一个参数：form
            var _url = $("#editForm").attr("data-url");
            var options = {
                url: _url + "?t=" + new Date().getMilliseconds(),
                type: 'post',
                data: $("#editForm").serializeObject(),
                success: function (data) {
                    if (data.isSuccess)
                        alert("修改成功");
                }
            };
            $.ajax(options);
        }

    });

    $("#type").change(function(){
        if($(this).val()=="GroupEvent")
        {
            $("#groupActivity").removeClass("hidden");
        }
        else{
            $("#groupActivity").addClass("hidden");
        }
    })


    $("body").delegate("#searchActivity","click",function () {
        loadData();
    }).delegate(".Wdate", "click", function () {
        wDatePicker({dateFmt: 'yyyy-MM-dd'});
    }).delegate(".changeStatus", "click", function () {
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
        $("#addActivityInfoForm").validate({
            submitHandler: function (form) {
                //表单提交句柄,为一回调函数，带一个参数：form
                var _url = $("#addOrUpdateActivity").attr("data-url");
                $.ajaxFileUpload({
                    //处理文件上传操作的服务器端地址(可以传参数,已亲测可用)
                    url: _url,
                    secureuri: true,                       //是否启用安全提交,默认为false
                    fileElementId: ['image'],           //文件选择框的id属性
                    dataType: 'text',                       //服务器返回的格式,可以是json或xml等
                    data: $("#addActivityInfoForm").serializeObject(),
                    success: function (data, status) {        //服务器响应成功时的处理函数
                        var resultStart = data.indexOf("{");
                        var resultEnd = data.indexOf("}");
                        var result = JSON.parse(data.substring(resultStart, resultEnd + 1));

                        if (result.isSuccess) {     //0表示上传成功(后跟上传后的文件路径),1表示失败(后跟失败描述)
                            alert("操作成功！");
                            window.location.href="sra_t_AddOrUpActicity?id="+result.id;
                        } else {
                            alert(result.msg);
                        }
                    },
                    error: function (data, status, e) { //服务器响应失败时的处理函数
                        $('#result').html('操作失败，请重试！！');
                    }
                });
            }
        });

   /* //新增
    var callb = function callback() {
        $(".modal-dialog").css("width", "800px");
        $("#addActivityInfoForm").validate({
            submitHandler: function (form) {
                //表单提交句柄,为一回调函数，带一个参数：form
                var _url = $("#addOrUpdateActivity").attr("data-url");
                $.ajaxFileUpload({
                    //处理文件上传操作的服务器端地址(可以传参数,已亲测可用)
                    url: _url,
                    secureuri: true,                       //是否启用安全提交,默认为false
                    fileElementId: ['image'],           //文件选择框的id属性
                    dataType: 'text',                       //服务器返回的格式,可以是json或xml等
                    data: $("#addActivityInfoForm").serializeObject(),
                    success: function (data, status) {        //服务器响应成功时的处理函数
                        var resultStart = data.indexOf("{");
                        var resultEnd = data.indexOf("}");
                        var result = JSON.parse(data.substring(resultStart, resultEnd + 1));

                        if (result.isSuccess) {     //0表示上传成功(后跟上传后的文件路径),1表示失败(后跟失败描述)
                            alert("操作成功！");
                            $("#myModal").modal("hide")
                            loadData();
                        } else {
                            alert(result.msg);
                        }
                    },
                    error: function (data, status, e) { //服务器响应失败时的处理函数
                        $('#result').html('操作失败，请重试！！');
                    }
                });
            }
        });
    }
    widget.init(callb);*/


})
