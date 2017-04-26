/**
 * Created by temp on 2016/8/17.
 */
define(function(require,exprots,model){


    $("body").delegate(".change-one","click",function(){
        Verificationcode.prototype.createCode();
    });

    function Verificationcode(){
        this.code=null;
        this.codeShow="";
    }
    Verificationcode.prototype={
        createCode:function(){

            this.code = "";
            this.codeShow="";
            var codeLength = 4;//验证码的长度
            var checkCode = $("#checkCode");
            var selectChar = new Array(0,1,2,3,4,5,6,7,8,9,'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');//所有候选组成验证码的字符，当然也可以用中文的

            for(var i=0;i<codeLength;i++)
            {
                var charIndex = Math.floor(Math.random()*36);
                if(charIndex%2) {
                    this.codeShow += "<span style='font-weight: bolder;display: inline-block;transform:rotate(" + charIndex + "deg)'>" + selectChar[charIndex] + "</span>";
                    this.code+=selectChar[charIndex];
                }else{
                    this.codeShow += "<span style='font-weight: bolder;display: inline-block;transform:rotate(" + charIndex*(-1) + "deg)'>" + selectChar[charIndex] + "</span>";
                    this.code+=selectChar[charIndex];
                }
            }
            if(checkCode){
                checkCode.addClass("code");
                checkCode.html(this.codeShow);
                $(".checkedCode").attr("data-id",this.code);
            }
        },
        validate:function(){

            var inputCode = $(".checkedCode");
            if(inputCode.length <=0)
            {
                $(".show-code-result").html("请输入验证码！");
                return false;
            }
            else if(inputCode.val().toLowerCase() != inputCode.attr("data-id").toLowerCase() )
            {
                $(".show-code-result").html("验证码输入错误！");
                Verificationcode.prototype.createCode();//刷新验证码
                return false;
            }
            else
            {
                $(".show-code-result").html();
                return true;
            }
        }
    }
    model.exports=Verificationcode;
});