/**
 * Created by temp on 2016/7/28.
 */
define(function (require, exprots, model) {
    require("superslide");
    require("../common/jquery.imagezoom.min")
    var wDatePicker = require("wdatePicker");

    $("body").delegate(".Wdate2", "click", function () {
        wDatePicker({minDate: '%y-%M-{%d}', dateFmt: 'yyyy-MM-dd'});
    })

    $("body").delegate(".Wdate", "focus", function () {

        var ctBeginDate=$(".js_ctBeginDate").text();
        var ctEndDate=$(".js_ctEndDate").text();

        if(ctEndDate==""){
            ctEndDate=ctBeginDate;
        }
        if(ctBeginDate==""){
            ctBeginDate=new Date();
        }
        wDatePicker({minDate: ctBeginDate,maxDate:ctEndDate, dateFmt: 'yyyy-MM-dd'});
    });


    var shoppingcart = require("../common/shoppingcat");
    var sc = new shoppingcart({reduceBtn: ".reduce", addBtn: ".add", txtNum: ".sum"});
    sc.init();


    $(".hd").slide({
        mainCell: "ul",
        effect: "left",
        autoPlay: false,
        vis: 4,
        prevCell: ".sPrev",
        nextCell: ".sNext"
    });
    $(".gallery").slide({mainCell: ".bd ul", effect: "left", autoPlay: false, delayTime: 0, defaultIndex: 4});

    $("img").removeClass("dis-none");
    $(".hd").find("a em").removeClass("dis-none");

    $(function () {

        $(".jqzoom").imagezoom();
        $("#thumblist li a").click(function () {
            $(this).parents("li").addClass("tb-selected").siblings().removeClass("tb-selected");
            $(".jqzoom").attr('src', $(this).find("img").attr("mid"));
            $(".jqzoom").attr('rel', $(this).find("img").attr("big"));
        });

        $(".order-content-left-top label").click(function () {
            $(this).addClass("current").siblings().removeClass("current");
            $("#" + $(this).attr("data-div")).removeClass("hidden").siblings(".order-content-left-content").addClass("hidden")
        });

        $(".reservation").click(function () {
            var pid = $("#hdId").val();
            var num = $(".sum").val();
            var tsd = $("#tsd").val();
            var hsd = $("#hsd").val();
            if(tsd===""||hsd===""){
                alert("请填写日期");
            }
            $.post("/ajax_reservation", {"pid": pid, "num": num, "tsd": tsd, "hsd": hsd},function (data) {
                if (data.isSuccess) {
                    window.location.href = "/orderConfirm";
                }else{
                    $(".js_in_time_error").html(data.msg);
                    if(data=="notlogin"){
                        window.location.href="/login";
                    }
                }
            });
        });

        /*        $(".js_check_show_error").onblur(function(){
         var date=$(this).val();
         if(date==null ||date==""){
         $(".js_in_time_error").val();
         }
         });*/


        $(".js_check_empty1").blur(function () {
            var temp=$(".js_in_hotel_error").html();
            var time=$(".js_check_empty1").val();
            if(time!=null){
                $(".js_in_time_error1").html("");
            }
        })
        $(".js_check_empty2").blur(function () {
            var temp=$(".js_in_time_error").html();
            var time=$(".js_check_empty2").val();
            if(time!=null){
                $(".js_in_time_error2").html("");
            }
        })

        function sleep(numberMillis){
            var now=new Date();
            var exitTime=now.getTime()+numberMillis;
            while(true){
                now=new Date();
                if(now.getTime()>exitTime)
                   return ;
            }
        }

        $("#join-shop-cart").click(function () {
            var pid = $("#hdId").val();
            var count = $(".sum").val();
            var tsd = $("#tsd").val();
            var hsd = $("#hsd").val();

            $.post("/ajax_cart_addItem", {"pid": pid, "num": count, "tsd": tsd, "hsd": hsd}, function (data) {
                if (data.isSuccess) {
                    var shoppingCartCount = $.trim($("#Js_shoppingCart").html()) / 1;
                    shoppingCartCount++;
                    var scBtn_left = $("#Js_shoppingCart").offset().left;
                    var scBtn_top = $("#Js_shoppingCart").offset().top;
                    $(".shoppingCartAdd").css({"left": "50%", "top": "50%", "opacity": "1"}).fadeIn();
                    setTimeout(function () {
                        $(".shoppingCartAdd").animate({
                            "left": scBtn_left,
                            "top": scBtn_top,
                            "opacity": "0"
                        }, 500, function () {
                            $(".shoppingCartAdd").hide();
                            $(".shoppingCartFail").hide();
                            //$("#Js_shoppingCart").html(shoppingCartCount);
                        });
                    }, 1000);

                    $("#Js_shoppingCart").text(data.id);

                } else {
                    $(".shoppingCartFail").css({"left": "50%", "top": "50%", "opacity": "1"}).fadeIn();
                    setTimeout(function () {
                        $(".shoppingCartFail").animate({
                            "opacity": "0"
                        }, 500, function () {
                            $(".shoppingCartFail").hide();
                        });
                    }, 1000);

                    if(tsd=="")
                        $(".js_in_time_error2").html(data.msg);
                    if(hsd=="")
                        $(".js_in_time_error1").html(data.msg);
                    if(data=="notlogin"){
                        window.location.href="/login";
                    }

                }
            })
        });
    });

});

