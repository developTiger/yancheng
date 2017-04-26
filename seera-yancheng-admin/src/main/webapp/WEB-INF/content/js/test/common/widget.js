/**
 * Created by zhouzh on 2016/5/19.
 */
define(function (require, exports, module) {
    require("jquery");
    var template = require("template");
    var modal=require("text!../tpl/modal.html");
    template.isEscape = false;
    //暴露modal方法
    exports.modal=function(opt){
        var options={
            id:"myModal",
            title:"",
            body:""
        };
        var data= $.extend({},options,opt);
        var html =template.compile(modal)(data);
        _existModalDel();
        $(document.body).append(html);
        $("#"+data.id).modal({
            backdrop:"static",
            keyboard:false
        });
    }
    exports.init=function(){
        _addBtn(".i_modal");
        _editBtn(".Js_edit");
    }
    //添加按钮事件
    var _addBtn=function(btn){
        var that=$(btn);
        that.each(function(){
            var th= $(this);
            th.click(function(){
                $.post(th.attr("data-url"),function(data){
                    exports["modal"]({
                        title:th.attr("data-title"),
                        body:data
                    });
                })
            })
        })
    }
    //编辑按钮事件,应用于单个操作的情况
    var _editBtn=function(btn){
        var that=$(btn);
        var id=that.attr("data-id");
        that.click(function(){
            $.post(that.attr("data-url"),function(data){
                exports["modal"]({
                    title:that.attr("data-title"),
                    body:data
                });
            })
        })
    }
    //若页面存在modal，则删除重新创建，避免ID重复
    var _existModalDel=function(){
        var $Js_modal=$(".Js_modal");
        if($Js_modal.length>0){
            $Js_modal.remove();
        }
    }
})
