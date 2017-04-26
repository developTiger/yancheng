/**
 * Created by zhouz on 2016/5/15.
 */
define(function (require, exports, module) {
    require('../init');
    var List = require("../common/pagelist");
    var check= require("../common/checkbox");
    require("../common/jquery.serializeObject");
    var template = require("template");
    var widget= require("../common/widget");
    var validate = require("validate");
    require('treeTable');
    var myWidGet=require("../common/myWidGet");
    var zTree= require('../common/tree');
    //widget.init();
    check.checkAll("body",".checkAll",".checkBtn")
    loadData();
    $("#permissionQueryBtn").click(function(){
        loadData();
    });

    function loadData(){
        var tpl = require("text!app/tpl/uauth/permissionTableTpl.html");
        var url=$("#searchForm").attr("data-url");
        var data = $("#searchForm").serialize().toString();
        List("#table",tpl,url,data,1,10);
    }


       var callb= function callback() {
            $("#addPermissionForm").validate({
                rules: {
                    permissionName: {
                        required: true
                    },
                    permissionSort:{
                        required:true,
                        number:true
                        //digits:true
                    }
                    /*email: {
                     required: true,
                     email: true
                     },
                     realName: {
                     required: true
                     }*/
                },
                messages: {
                    permissionName: {
                        required: "必填"
                    },
                    permissionSort:{
                        required:"必须为合法数字",
                        number:"必须为合法数字"
                    }
                    /*email: {
                     required: "必填",
                     email: "E-Mail格式不正确"
                     },
                     realName: {
                     required: "不能为空"
                     }*/
                },
                submitHandler: function (form) {   //表单提交句柄,为一回调函数，带一个参数：form

                    var option = {};
                    option.treeId = "tree1";
                    option.inputId = "menuId";
                    myWidGet.submitTreeVal(option);

                    var _url = $("#addOrUpdatePermission").attr("data-url");
                    var options = {
                        url: _url + "?t=" + new Date().getMilliseconds(),
                        type: 'post',
                        data: $("#addPermissionForm").serializeObject(),
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
           var options = {
              // jsonData:eval($("#tree1").attr("data-json")),
               url:"ajax_menu_query_list",
               id:"#tree1"
           }

           zTree.init(options);
       }
    widget.init(callb);

    $("#deletePermission").click(function(){
        var chk_value =[];
        $('.checkBtn:checked').each(function(){
            chk_value.push($(this).val());
        });
        deleteMenu(chk_value.toString(),false);
    })

    function deleteMenu(selectids){
        var options = {
            url:"ajax_deletePermission?t=" + new Date().getMilliseconds()+"&ids="+selectids,
            type: 'get',

            success: function (data) {
                if (data== "success") {
                    alert("修改成功");
                    loadData();
                }
                else
                    alert(data);
            }
        };
        $.ajax(options);
    }

    $("body").delegate("#cancelModal","click",function(){
        $("#myModal").modal("hide")
    });

   /* $("body").delegate("#addOrUpdatePermission","click",function(){
        var _url = $("#addOrUpdatePermission").attr("data-url");

        var options = {
            url: _url + "?t=" + new Date().getMilliseconds(),
            type: 'post',
            data: $("#addPermissionForm").serializeObject(),
            success: function (data) {
                if (data== "success")
                    alert("新增成功");
                $("#myModal").modal("hide")
                loadData();
            }
        };
        $.ajax(options);
    });*/

});