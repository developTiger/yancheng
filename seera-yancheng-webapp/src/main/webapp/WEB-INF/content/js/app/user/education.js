/**
 * Created by swb on 2016/6/13.
 */
define(function (require, exports, module) {

    require('../init');
    var template = require("template");
    var List = require("../common/pagelist");
    require("../common/jquery.serializeObject");
    var widget = require("../common/widget");
   // widget.init();
    var check = require("../common/checkbox");
    var validate = require("validate");
    check.checkAll("body", ".checkAll", ".checkBtn");

    loadData();
    $("#eduQueryBtn").click(function () {
        loadData();
    });

    function loadData() {
        var tpl = require("text!app/tpl/uauth/educationTableTpl.html");
        var url = $("#searchForm").attr("data-url");
        var data = $("#searchForm").serialize().toString();
        List("#table", tpl, url, data, 1, 10);
    }
    /*$("body").delegate("#addOrUpdateEducation","click",function(){
     var _url = $("#addOrUpdateEducation").attr("data-url");

     var options = {
     url: _url + "?t=" + new Date().getMilliseconds(),
     type: 'post',
     data: $("#addEducationform").serializeObject(),
     success: function (data) {
     if (data== "success")
     alert("新增成功");
     $("#myModal").modal("hide")
     loadData();
     }
        };
        $.ajax(options);
    });*/
    /*$("body").delegate("#addOrUpdateEducation","click",function(){
        var _url = $("#addOrUpdateEducation").attr("data-url");
        var options = {
            url: _url + "?t=" + new Date().getMilliseconds(),
            type: 'post',
            data: $("#addEducationform").serializeObject(),
            success: function (data) {
                if (data== "success")
                    alert("新增成功");
                $("#myModal").modal("hide")
                loadData();
            }
        };
        $.ajax(options);
    });*/

    $("#deleteEducation").click(function(){
        var chk_value=[];
        $('.checkBtn:checked').each(function(){
            chk_value.push($(this).val());
        });
        deleteEducation(chk_value.toString(),false);
    });
    function deleteEducation(selectids){
        var options = {
            url:"ajax_deleteEducation?t=" + new Date().getMilliseconds()+"&ids="+selectids,
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
    var callb= function() {
        $("#addEducationform").validate({
            rules: {
                school: {
                    required: true
                }
              /*  level:{
                    required:true,
                    number:true
                    //digits:true
                }*/
                /*email: {
                 required: true,
                 email: true
                 },
                 realName: {
                 required: true
                 }*/
            },
            messages: {
                school: {
                    required: "必填"
                }
               /* level:{
                    required:"必须为合法数字",
                    number:"必须为合法数字"
                }*/
                /*email: {
                 required: "必填",
                 email: "E-Mail格式不正确"
                 },
                 realName: {
                 required: "不能为空"
                 }*/
            },
            submitHandler: function (form) {   //表单提交句柄,为一回调函数，带一个参数：form

                /*var option = {};
                option.treeId = "tree1";
                option.inputId = "menuId";
                myWidGet.submitTreeVal(option);*/
                var _url = $("#addOrUpdateEducation").attr("data-url");
                var options = {
                    url: _url + "?t=" + new Date().getMilliseconds(),
                    type: 'post',
                    data: $("#addEducationform").serializeObject(),
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
        /*var options = {
            // jsonData:eval($("#tree1").attr("data-json")),
            url:"ajax_menu_query_list",
            id:"#tree1"
        }

        zTree.init(options);*/
    }
    widget.init(callb);
});
