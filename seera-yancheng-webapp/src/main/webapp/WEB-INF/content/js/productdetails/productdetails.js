/**
 * Created by temp on 2016/7/28.
 */
define(function(require,exprots,model){

    require("superslide");
    var wDatePicker=require("wdatePicker");

    $("body").delegate(".Wdate","click",function(){
        wDatePicker({minDate:'%y-%M-{%d}',dateFmt: 'yyyy-MM-dd 00:00:00'});
    });

    $(".hd").slide({ mainCell:"ul",effect:"leftLoop",autoPlay:false,vis:4,prevCell:".sPrev",nextCell:".sNext" });

    $(".gallery").slide({ mainCell:".bd ul",effect:"leftLoop", autoPlay:false, delayTime:500, defaultIndex:4 });

})