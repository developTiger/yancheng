/**
 * Created by liulin on 2016/7/16.
 */

define(function(require,exports,module){

    var List = require("../common/pagelist");//分页
    var check = require("../common/checkbox");//复选框
    var wDatePicker = require("wdatePicker");//时间插件
    var widget = require("../common/widget");//widget插件作用：点击按钮，弹出模态框，对按钮进行初始化
    var validate = require("validate");//对表单进行验证
    require("../common/jquery.serializeObject");//对提交的数据 进行序列化 返回一个object
    var template = require("../common/templeteHelper")



    check.checkAll("body", ".checkAll", ".checkBtn")
    loadData();
    $("#touristQueryBtn").click(function () {
        loadData();
    });

    //加载数据
    function loadData() {
        var tpl = require("text!app/tpl/tourist/touristTpl.html");
        var url = $("#searchForm").attr("data-url");
        var data = $("#searchForm").serialize().toString();
        List("#table", tpl, url, data, 1, 10);
    }

    //时间插件
    $("body").delegate(".Wdate", "click", function () {
        wDatePicker({ dateFmt: 'yyyy-MM-dd' });
    });


    // 手机号码验证
    jQuery.validator.addMethod("isMobile", function(value, element) {
        var length = value.length;
        var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
        return this.optional(element) || (length == 11 && mobile.test(value));
    }, "请正确填写您的手机号码");


    //新增 在弹窗出来之后 callback回调函数触发
    var callb= function callback() {
        $("#addTouristInfoForm").validate({

            rules: {
                userName: {
                    required: true
                },
                email:{
                    required:true,
                    email:true
                },
                mobilePhone: {
                    required : true,
                    minlength : 11,
                    // 自定义方法：校验手机号在数据库中是否存在
                    // checkPhoneExist : true,
                    isMobile : true
                },
                idCardNo:{
                    required:true
                }
            },
            messages: {
                userName: {
                    required: "必填"
                },
                email:{
                    required:"必须输入正确格式的电子邮件",
                    email:"必须输入正确格式的电子邮件"
                },
                mobilePhone:{
                    required : "请输入手机号",
                    minlength : "确认手机不能小于11个字符",
                    isMobile : "请正确填写您的手机号码"
                },
                idCardNo:{
                    required:"必填"
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
                            alert("新增成功");
                            $("#myModal").modal("hide")
                            loadData();
                    }
                };
                $.ajax(options);
            }
        });
    }
    widget.init(callb);

    $("body").delegate(".disabledStatus","click",function(){
        var id = $(this).parent().parent().children(".firstTd").children().children(".checkBtn").val();
        $.ajax({
            url:"ajax_disabled_touristStatus"+"?t="+new Date().getMilliseconds()+"&id="+id,
            type:'get',
            success:function(data){
                if(data.isSuccess){
                    alert("状态设置成功");
                    loadData();
                }
            }
        })
    })



    $("body").delegate(".enableStatus","click",function(){
        var id = $(this).parent().parent().children(".firstTd").children().children(".checkBtn").val();
        $.ajax({
            url:"ajax_enable_touristStatus"+"?t="+new Date().getMilliseconds()+"&id="+id,
            type:'get',
            success:function(data){
                if(data.isSuccess){
                    alert("状态设置成功");
                    loadData();
                }
            }
        })
    })



})
