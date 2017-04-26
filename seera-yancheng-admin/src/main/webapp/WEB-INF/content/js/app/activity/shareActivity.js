/**
 * Created by liulin on 2016/7/16.
 */

define(function (require, exports, module) {

    var List = require("../common/pagelist");//分页
    //widget插件作用：点击按钮，弹出模态框，对按钮进行初始化
    var widget = require("../common/widget");
    require("../common/jquery.serializeObject");//


    loadData();
    function loadData() {
        var tpl = require("text!app/tpl/activity/shareActivityTableTpl.html");
        var url = $("#searchForm").attr("data-url");
        var data = $("#searchForm").serialize().toString();
        List("#table", tpl, url, data, 1, 10);
    };



    $("body").delegate("#searchActivity","click",function () {
        loadData();
    })



})
