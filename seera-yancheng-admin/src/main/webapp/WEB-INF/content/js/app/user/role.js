/**
 * Created by zhouz on 2016/5/15.
 */
define(function (require, exports, module) {
    require('../init');
    var List = require("../common/pagelist");
    var check = require("../common/checkbox");
    require("../common/jquery.serializeObject");
    var template = require("template");
    var widget = require("../common/widget");
    var validate = require("validate");

    require("mutiSelect");


    check.checkAll("body", ".checkAll", ".checkBtn")
    loadData();
    $("#RoleQueryBtn").click(function () {
        loadData();
    });
    function loadData() {
        var tpl = require("text!app/tpl/uauth/roleTableTpl.html");
        var url = $("#searchForm").attr("data-url");
        var data = $("#searchForm").serialize();
        List("#table", tpl, url, data, 1, 10);
    }

    $("body").delegate("#cancelModal", "click", function () {
        $("#myModal").modal("hide")
    });
    $("#deleteMenu").click(function () {
        var chk_value = [];
        $('.checkBtn:checked').each(function () {
            chk_value.push($(this).val());
        });
        setUserStaus(chk_value.toString());
    })
    function setUserStaus(selectids) {
        var options = {
            url: "ajax_deleteMenu?t=" + new Date().getMilliseconds(),
            type: 'post',
            data: {ids: selectids},
            success: function (data) {
                if (data == "success") {
                    alert("删除成功");
                    loadData();
                }
                else
                    alert(data);
            }
        };
        $.ajax(options);
    }

    $("body").delegate("#deleteRole", "click", function () {
        var _url = $("#deleteRole").attr("data-url");
        var options = {
            url: _url + "&t=" + new Date().getMilliseconds(),
            type: 'post',
            success: function (data) {
                if (data == "success")
                    alert("删除成功");
                $("#myModal").modal("hide");
                loadData();
            }
        };
        $.ajax(options);
    });

    var vaLi = function validates() {


        $('#menu-name').multiselect({
            includeSelectAllOption: true,
            maxHeight: 400,
            buttonWidth: '210px'
        });

        var hidPermissionGroupName = $("#Js_hidPermissionGroup").val();
        var h_1 = hidPermissionGroupName.substring(1,hidPermissionGroupName.length-1);
        var h_3=h_1.replace(/\s+/g,"");//去掉字符串所有空格
        var h_2 = [];
        h_2= h_3.split(",");

        $('#menu-name').multiselect('select', h_2);//弹窗弹出之后再加载


        $("#addRoleForm").validate({
            rules: {
                roleName: {
                    required: true
                },
                idCode: {
                    required: true
                },
                description: {
                    required: true
                }
            },
            messages: {
                roleName: {
                    required: "必填"
                },
                idCode: {
                    required: "必填"
                },
                description: {
                    required: "不能为空"
                }
            },
            submitHandler: function (form) {   //表单提交句柄,为一回调函数，带一个参数：form
                var _url = $("#addOrUpdateRole").attr("data-url");
                $("#pid").val($("#menu-name").val().toString())
                var options = {
                    url: _url + "?t=" + new Date().getMilliseconds(),
                    type: 'post',
                    data: $("#addRoleForm").serializeObject(),
                    success: function (data) {
                        if (data == "success")
                            alert("操作成功");
                        $("#myModal").modal("hide")
                        loadData();
                    }
                };
                $.ajax(options);
            }
        })
    }
    widget.init(vaLi);


    var $body = $("body");//全局body对象
    $body.delegate(".Js_deleteRole","click",function(){
        var id = $(this).parent().parent().children(".Js_firstTd").children().children(".checkBtn").val();
        $.ajax({
            url:"ajax_delete_roleManager"+"?t="+new Date().getMilliseconds()+"&id="+id,
            type:"post",
            success:function(data){
                if(data == "success"){
                    alert("删除成功！");
                    loadData();
                }
            }
        })
    })


});