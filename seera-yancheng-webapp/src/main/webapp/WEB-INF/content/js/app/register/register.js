define(function(require,exprots,model){

    require("jquery");
    var Verificationcode=require("verificationcode");
    require("../pwdfindback/checkpwdstrong");

    var form = "#regForm",
        regBtn = "#regBtn",
        unText = "input[name='username']",
        pwText = "input[name='password']",
        regError = "#regForm label.error";
    //loginErrorText = ""

    $("body").delegate(".reg-QR-code-label", "click", function () {

        $(".reg-qr-code-background").css({display:"none"});
        $(".selectQR").css({display:""});
    });

    $("body").delegate(".account-reg", "click", function () {
        $(".reg-qr-code-background").css({display:""});
        $(".selectQR").css({display:"none"});
    });
    var vc=new Verificationcode();
    vc.createCode();

    //注册用户重复验证。
    var tempResult;//全局变量，判断用户是否存在
    $("input[name='username']").change(function(){
        var tempName=$(this).val();
        $.post("ajax_check_userName/" + encodeURI(tempName)).done(function (data) {
            tempResult=data.isSuccess;
            if (data.isSuccess) {
                $(".js_name_ok").removeClass("hidden");
                $(".js_login_error").addClass("hidden");
            }else{
                $(".js_name_ok").addClass("hidden");
                $(".js_login_error").removeClass("hidden");
                $("#regBtn").attr("disabled","disabled");
            }
        });
     });
    $("body").delegate(regBtn,"click",function () {
        var passWord=$(".js_check_passWord").val();
        var commitPassword=$(".js_commit_password").val();
        if(!$(".js_agree_item").prop("checked")){
            alert("请接收春秋乐园条款");
            return false;
        };
        var CodeResult =vc.validate();
        if (CodeResult == true) {
            return true;
        }else{
            return false;
        };
        return tempResult;
    });

    $(".js_check_passWord").change(function(){
        var passWord=$(".js_check_passWord").val();
        var commitPassword=$(".js_commit_password").val();

        if(passWord.length<6){
            $(".js_password_error").removeClass("hidden");
            $(".js_showError").html("密码少于六个字符");
            $(".js_success").addClass("hidden");
            $("#regBtn").attr("disabled","disabled");
            return false;
        }else{
            $(".js_showError").html("");
        }
        if(commitPassword != ""){
            checkpasswordEquals(passWord,commitPassword);
        }
    });

    function checkpasswordEquals(passWord,commitPassword){
        if(passWord==commitPassword){
            if(passWord==null || commitPassword==""){
                $("#regBtn").attr("disabled","disabled");
                return false;
            }
            $(".js_password_error").addClass("hidden");
            $(".js_success").removeClass("hidden");
            $("#regBtn").attr("disabled",false);
        }else{
            $(".js_password_error").removeClass("hidden");
            $(".js_showError").html("密码输入不一致");
            $(".js_success").addClass("hidden");
            $("#regBtn").attr("disabled","disabled");
        }
    }

    $(".js_commit_password").change(function(){
        var passWord=$(".js_check_passWord").val();
        var commitPassword=$(".js_commit_password").val();
        checkpasswordEquals(passWord,commitPassword);
    });

    exprots.run = function() {
        $(unText).blur(function () {
            if ($(this).val() != "" && $(pwText).val() != "") {
                $(regError).html("")
            }
        })
        $(pwText).blur(function () {
            if ($(this).val() != "" && $(unText).val() != "") {
                $(regError).html("")
            }
        })
        $(pwText).bind("keypress", function (e) {
            if (e.keyCode === 13) {
                $(regBtn).click();
            }
        });
        $(unText).bind("keypress", function (e) {
            if (e.keyCode === 13) {
                $(regBtn).click();
            }
        });
    }
})