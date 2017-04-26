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

    var form = "#loginForm",
        loginBtn = "#loginBtn",
        unText = "input[name='username']",
        pwText = "input[name='password']",
        loginError = "#loginForm label.error";
    //loginErrorText = ""

    exprots.run = function(){

        $(unText).blur(function(){
            if($(this).val() != "" && $(pwText).val() != ""){
                $(loginError).html("")
            }
        })
        $(pwText).blur(function(){
            if($(this).val() != "" && $(unText).val() != ""){
                $(loginError).html("")
            }
        })
        //检测是否登陆成功
        $(loginBtn).click(function(){

            var userName = $(unText).val();
            var password = $(pwText).val();

            if(userName == "" || password == ""){
                $(loginError).html("请输入用户名和密码。")
                return false;
            }
            $.ajax({
                type : "post",
                url : "/loginForm?t=" + new Date().getMilliseconds(),
                data:{userName:$.trim(userName),password:$.trim(password)},
                success : function(data){
                    debugger;
                    if(data== "success"){
                        $(loginError).html("&nbsp;");
                        //成功
                        var rurl = $.trim($(form).attr("data-rurl"));
                        if(rurl != ''){
                            window.location.href = rurl;
                        }else{
                            window.location.href = "sra_index";
                        }
                        return true;
                    }else {
                        $(loginError).html(data);
                        return false;//失败
                    }
                }
            })
        });
        $(pwText).bind("keypress",function(e){
            if(e.keyCode === 13){
                $(loginBtn).click();
            }
        });
        $(unText).bind("keypress",function(e){
            if(e.keyCode === 13){
                $(loginBtn).click();
            }
        });
    }





})