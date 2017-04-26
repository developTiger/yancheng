/**
 * Created by temp on 2016/7/28.
 */
define(function(require,exprots,model){

    require("jquery");

    $("body").delegate(".logon-QR-code-label", "click", function () {

        $(".content").css({display:"none"});
        $(".content-cont").css({display:""});
    });

    $("body").delegate(".account-logon", "click", function () {
        $(".content").css({display:""});
        $(".content-cont").css({display:"none"});
    });



})