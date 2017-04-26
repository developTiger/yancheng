/**
 * Created by wgyy on 2016/7/16.
 */
define(function (require, exports, module) {
    require("jquery");
    require("./unslider.min");
        if($(".Js_top_select").length>0){
            $(".Js_top_select").hover(function(){
                $(this).removeClass("down").addClass("up");
                $(this).find("div").removeClass("dis-none");
            },function(){
                $(this).removeClass("up").addClass("down");
                $(this).find("div").addClass("dis-none")
            })
        }

 /*       $(".Js_right_div div").hover(function(){
            $(this).find("p").removeClass("dis-none")
        },function(){
            $(this).find("p").addClass("dis-none")
        });*/

    $(".Js_right_div1").hover(function(){
        $(this).find("p").removeClass("dis-none")
    },function(){
        $(this).find("p").addClass("dis-none")
    });

    $(".Js_right_div2").hover(function(){
        $(this).find("div").addClass("dis-none");
        $(this).find("p").removeClass("dis-none")
    },function(){
        $(this).find("div").removeClass("dis-none");
        $(this).find("p").addClass("dis-none");
    });



        $(window).scroll(function(){
            if ($(window).scrollTop()>100){
                $(".Js_toTop").fadeIn(500);
            }
            else
            {
                $(".Js_toTop").fadeOut(500);
            }
        });
        $(".Js_toTop").click(function(){
            $('body,html').animate({scrollTop:0},500);
        })
    if($(".Js_search_select").length>0){
        $(".Js_search_select").hover(function(){
            $(this).removeClass("down").addClass("up");
            $(this).find("ul").removeClass("dis-none");
        },function(){
            $(this).removeClass("up").addClass("down");
            $(this).find("ul").addClass("dis-none")
        })
    }
    $(".Js_search_down li").click(function(){
        $(this).closest("div").find("span").html($(this).html());

        switch ($(this).html()){
            case "门票":
                $("input[name=searchType]").val("Ticket");
                break;
            case "餐饮":
                $("input[name=searchType]").val("Catering");
                break;
            case "纪念品":
                $("input[name=searchType]").val("Souvenirs");
                break;
            case "酒店":
                $("input[name=searchType]").val("Hotel");
                break;
            case "其他":
                $("input[name=searchType]").val("Other");
                break;
        }
        $(".Js_search_select ul").addClass("dis-none");
    })
    if($('.banner').length>0){
        $('.banner').unslider({
                dots: true,
                speed:1000,
                delay: 5000
            }
        );
    }
})

