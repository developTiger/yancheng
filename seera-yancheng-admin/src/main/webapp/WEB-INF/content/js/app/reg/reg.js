/**
 * Created by temp on 2016/7/28.
 */
define(function(require,exprots,model){

    require("jquery");

    $("body").delegate(".reg-qr-code-background", "click", function () {

        $(".selectZHANGHAO").css({display:"none"});
        $(".selectQR").css({display:""});
    });

    $("body").delegate(".reg-logon-zhijie", "click", function () {
        $(".selectZHANGHAO").css({display:""});
        $(".selectQR").css({display:"none"});
    });



})