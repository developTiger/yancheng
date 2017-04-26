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
    check.checkAll("body",".checkAll",".checkBtn")
    loadData();
    $("#DeptQueryBtn").click(function(){
        loadData();
    });
    function loadData(){
        var tpl = require("text!app/tpl/uauth/empGroupTableTpl.html");
        var url=$("#searchForm").attr("data-url");
        var data = $("#searchForm").serialize();
        List("#table",tpl,url,data,1,10);
    }

    $("body").delegate("#cancelModal","click",function(){
        $("#myModal").modal("hide")
    });
    $("#deleteMenu").click(function(){
        var chk_value =[];
        $('.checkBtn:checked').each(function(){
            chk_value.push($(this).val());
        });
        setUserStaus(chk_value.toString());
    })
    function setUserStaus(selectids){
        var options = {
            url:"ajax_deleteEmpGroup?t=" + new Date().getMilliseconds(),
            type: 'post',
            data:{ids:selectids},
            success: function (data) {
                if (data.isSuccess) {
                    alert("修改成功");
                    loadData();
                }
                else
                    alert(data);
            }
        };
        $.ajax(options);
    }

    $("body").delegate("#deleteEmpGroupItem","click",function() {
        setUserStaus($(this).attr("data-title"));
    });

    var vaLi= function validates() {
        $("#addDeptForm").validate({
            rules: {
                deptName: {
                    required: true
                },
                deptNo: {
                    required: true
                },
                brief: {
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
                var _url = $("#addOrUpdateDept").attr("data-url");
                var options = {
                    url: _url + "?t=" + new Date().getMilliseconds(),
                    type: 'post',
                    data: $("#addDeptForm").serializeObject(),
                    success: function (data) {
                        if (data.isSuccess)
                            alert("新增成功");
                        $("#myModal").modal("hide")
                        loadData();
                    }
                };
                $.ajax(options);
            }
        })
    }
    widget.init(vaLi);
});