define(function (require, exprots, model) {

    require("jquery");
    var Verificationcode = require("verificationcode");
    var form = "#loginForm",
        loginBtn = "#loginBtn",
        unText = "input[name='username']",
        pwText = "input[name='password']",
        loginError = "#loginForm label.error";
    //loginErrorText = ""


    $(function(){
        //获取Canvas对象(画布)
        var canvas = document.getElementById("myCanvas");
        //简单地检测当前浏览器是否支持Canvas对象，以免在一些不支持html5的浏览器中提示语法错误
        if(canvas.getContext){
            //获取对应的CanvasRenderingContext2D对象(画笔)
            var ctx = canvas.getContext("2d");
            //线条的颜色
            ctx.strokeStyle="#FF9933";
            //线条的宽度像素
            ctx.lineWidth=10;
            //线条的两关形状
            ctx.lineCap="round";
            //注意，Canvas的坐标系是：Canvas画布的左上角为原点(0,0)，向右为横坐标，向下为纵坐标，单位是像素(px)。
            //开始一个新的绘制路径
            ctx.beginPath();
            //定义直线的起点坐标为(10,10)
            var from_begin = Math.floor(Math.random()*290);
            var from_end=Math.floor(Math.random()*140);
            ctx.moveTo(from_begin, from_end);
            //定义直线的终点坐标为(50,10)
            var to_begin;
            var to_end;
            if(from_begin>=145){
                to_begin=Math.floor(Math.random()*145);
            }else{
                to_begin=Math.floor(Math.random()*290);
                while(to_begin<=145){
                    to_begin=Math.floor(Math.random()*290);
                }
            }
            if(from_end>=70){
                to_end=Math.floor(Math.random()*70);
            }else{
                to_end=Math.floor(Math.random()*140);
                while(to_end<=70){
                    to_end=Math.floor(Math.random()*140);
                }
            }
            ctx.lineTo(to_begin, to_end);

            //沿着坐标点顺序的路径绘制直线
            ctx.stroke();
            //关闭当前的绘制路径
            ctx.closePath();
        }
    });




    $("body").delegate(".logon-QR-code-label", "click", function () {

        $(".content").css({display: "none"});
        $(".content-cont").css({display: ""});
    });

    function verifyQr() {
        return $.post("/verify_qr").done(function () {
            window.location.href = "/";
        });
    }

    $(".logon-QR-code").click(function () {
        $.post("/verify_qr").done(function () {
            window.location.href = "/";
        }).error(function (data) {
            if (data.status == 504) {
                $.post("/get_qr").done(function (data) {
                    $("#imgQr").attr("src", "data:image/jpg;base64," + data);
                    $(".logon-QR-code").click();
                });

            }
        });
    });

    $("body").delegate(".account-logon", "click", function () {
        $(".content").css({display: ""});
        $(".content-cont").css({display: "none"});
    });
    var vc = new Verificationcode();
    vc.createCode();

    $("body").delegate(loginBtn, "click", function () {
        var CodeResult = vc.validate();
        if (CodeResult) {
            return true;
        } else {
            return false;
        }
    });

    exprots.run = function () {
        $(unText).blur(function () {
            if ($(this).val() != "" && $(pwText).val() != "") {
                $(loginError).html("")
            }
        })
        $(pwText).blur(function () {
            if ($(this).val() != "" && $(unText).val() != "") {
                $(loginError).html("")
            }
        })
        //检测是否登陆成功

        $(pwText).bind("keypress", function (e) {
            if (e.keyCode === 13) {
                $(loginBtn).click();
            }
        });
        $(unText).bind("keypress", function (e) {
            if (e.keyCode === 13) {
                $(loginBtn).click();
            }
        });
    }
})