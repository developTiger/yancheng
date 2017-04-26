/**
 * Created by temp on 2016/7/26.
 */

define(function (require, exports, module) {

    require('../init');
    var template = require("template");
    require("../common/jquery.serializeObject");
    var widget= require("../common/widget");
    var validate = require("validate");
    var check= require("../common/checkbox");
    var validate = require("validate");
    require("iCheck");

    $('input').iCheck({
        checkboxClass:'icheckbox_flat-red',  //每个风格都对应一个，这个不能写错哈。
        radioClass: 'icheckbox_flat-red'
    });
    check.checkAll("body",".checkAll",".checkBtn");

    /*
     *
     * 增加商品事件！！！
     */
    $("body").delegate(".Product_Add", "click", function () {
        var Product_Sum= $(".Product_Sum").val();
        alert(Product_Sum);
    });

    /*
     *
     * 减少商品事件！！！
     */
    $("body").delegate(".Product_Minus", "click", function () {
        var Product_Sum= $(".Product_Sum").val();
        alert(Product_Sum);
    });
});