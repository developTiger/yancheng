/**
 * Created by zhouz on 2016/5/25.
 */
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
    require('treeTable');
    var zTree= require('../common/tree');

    $(function () {
        check.relCheck("#example-advanced");
        check.relCheck("#table");
    })

    check.checkAll("body",".checkAll",".checkBtn")
    loadData();
    $("#menuQueryBtn").click(function(){
        loadData();
    });

    $("#example-advanced").treetable({ expandable: true });

    // Highlight selected row

    function loadData(callaback){
        var tpl = require("text!app/tpl/uauth/menuTableTpl.html");
        var url=$("#searchForm").attr("data-url");
        var data = $("#searchForm").serialize().toString();
        List("#table",tpl,url,data,1,10);

        $(".treetable").treetable({ expandable: true ,initialState: "expanded" });
        callaback&&typeof callaback=="function"&&callaback();
    }

    $("body").on("click","#cancelModal",function(){
        $("#myModal").modal("hide")
    });
    $("#deleteMenu").click(function(){
        var chk_value =[];
        $('.checkBtn:checked').each(function(){
            chk_value.push($(this).val());
        });
        deleteMenu(chk_value.toString(),false);
    })

    function deleteMenu(selectids){
        var options = {
            url:"ajax_deleteMenu?t=" + new Date().getMilliseconds()+"&ids="+selectids,
            type: 'get',
            success: function (data) {
                if (data== "success") {
                    alert("删除成功");
                    loadData();
                }
                else
                    alert(data);
            }
        };
        $.ajax(options);
    }



    var callb = function(){
        //validate



    }

    widget.init(callb);
    $("body").delegate("#addOrUpdateMenu","click",function(){
        var _url = $("#addOrUpdateMenu").attr("data-url");

        var options = {
            url: _url + "?t=" + new Date().getMilliseconds(),
            type: 'post',
            data: $("#addMenuform").serializeObject(),
            success: function (data) {
                if (data== "success")
                    alert("操作成功");
                $("#myModal").modal("hide")
                loadData();
            }
        };
        $.ajax(options);
    });
    /////ztree demon

});