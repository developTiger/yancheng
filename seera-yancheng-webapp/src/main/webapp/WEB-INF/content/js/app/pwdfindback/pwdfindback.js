define(function(require,exprots,model){

    require("jquery");
    var Verificationcode=require("verificationcode");
    var Checkpwdstrong=require("./checkpwdstrong");

    var vc=new Verificationcode();
    vc.createCode();

    $("body").delegate(".js_submit_application","click",function(){

        var email=$("#email").val();
        var options = {
            url: "/send_email_link?t=" + new Date().getMilliseconds(),
            type: 'post',
            data:{email:email},
            success: function (data3) {
                if(data3.isSuccess){
                    alert("邮件已发送邮箱，请进入邮箱查看。。");
                }
            }
        };
        $.ajax(options);
    });


    $("body").delegate(".js_finish_update_password","click",function(){

        var password=$(".js_check_pwd_strong").val();
        var userName=$(".js_userName").val();
        var options = {
            url:"/update_password_by_email?t=" + new Date().getMilliseconds(),
            type: 'post',
            data:{password:password,userName:userName},
            success: function (data) {
                if(data.isSuccess){
                    window.location.href="editPasswordSuccess";
                }
            }
        };
        $.ajax(options);
    });





   /* $(function(){
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
    });*/

    $("body").delegate(".js_next_step","click",function(){

        var CodeResult = vc.validate();
        if (CodeResult) {
            $(".show-code-result").val("");
        }else{
            return false;
        }
        var token=$(".js_token").val();
        var options = {
            url: "sure_account" + "?t=" + new Date().getMilliseconds() ,
            type: 'post',
            data: {token:token},
            success: function (data) {

                if(data.isSuccess){
                    window.location.href="/toAnQuanValidate/"+data.id;
                }else{
                    alert("账号不存在!!!");
                }
            }
        };
        $.ajax(options);
    });

})
