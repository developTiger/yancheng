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
    //$("body").delegate(".Product_Add", "click", function () {
    //    var Product_Sum= $(".Product_Sum").val();
    //    alert(Product_Sum);
    //});

    /*
     *
     * 减少商品事件！！！
     */
    //$("body").delegate(".Product_Minus", "click", function () {
    //    var Product_Sum= $(".Product_Sum").val();
    //    alert(Product_Sum);
    //});

    /**
     * 增加购物车的商品项数量
     */
    /*$("body").delegate(".Product_Add", "click", function () {
        var Product_Sum= $(".Product_Sum").val();
        Product_Sum++;
        $(".Product_Sum").val(Product_Sum);
    });*/

    /**
     * 减少购物车的商品项数量
     */
    $("body").delegate("#Minus_1", "click", function () {
        var Product_Sum= $("#text_1").val();
        if(Product_Sum<=1)
            $(this).attr("disabled");
        else
        Product_Sum--;

        $("#text_1").val(Product_Sum);
    });

    $("body").delegate("#Add_1", "click", function () {
        var Product_Sum= $("#text_1").val();
        Product_Sum++;
        if(Product_Sum<=0) Product_Sum=0;
        $("#text_1").val(Product_Sum);
    });

    $("body").delegate("#button_3", "click", function () {
       $("#tr_3").remove();
       // $("#tr_3").delete();
    });

    function sum(Product_Sum){
        var price=$("span#price").html();


    }

});