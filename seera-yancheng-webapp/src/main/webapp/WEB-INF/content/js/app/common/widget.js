/**
 * Created by zhouzh on 2016/5/19.
 */
define(function (require, exports, module) {
    require("jquery");
    var template = require("template");
    var modal = require("text!../tpl/modal.html");
    template.isEscape = false;
    //暴露modal方法
    exports.modal = function (opt) {
        var options = {
            id: "myModal",
            title: "",
            body: ""
        };
        var data = $.extend({}, options, opt);
        var html = template.compile(modal)(data);
        _existModalDel();
        $(document.body).append(html);
        $("#" + data.id).modal();
    }
    exports.init = function (callback) {
        _addBtn(".i_modal",callback);
    }

    //添加按钮事件
    var _addBtn = function (btn,callback) {

        $("body").delegate(btn, "click", function () {
            var th = $(this);
            $.post(th.attr("data-url"), function (data) {
                exports["modal"]({
                    title: th.attr("data-title"),
                    body: data
                });
                callback && (typeof  callback == "function") &&callback();
            })
        })

        //var that = $(btn);
        //that.each(function () {
        //
        //})

    }

    exports.showModal= function(url,title,callback){
        $.post(url, function (data) {
            exports["modal"]({
                title:title,
                body: data
            });
            callback && (typeof  callback == "function") &&callback();
        })
    }

    //若页面存在modal，则删除重新创建，避免ID重复
    var _existModalDel = function () {
        var $Js_modal = $(".Js_modal");
        if ($Js_modal.length > 0) {
            $Js_modal.remove();
        }
    }


    //取消按钮
    $("body").delegate("#cancelModal", "click", function () {
        $("#myModal").modal("hide")
    });
})
