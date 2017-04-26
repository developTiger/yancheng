/**
 * Created by temp on 2016/7/28.
 */
define(function (require, exprots, model) {

    require("validate");
    $("body").delegate(".js_update_ticket","click",function(){
        var url=$(this).attr("data-url");
        var content=$(this).attr("data-title");
        $.post(url +"/" +content).done(function (data) {

            $("#consignee-name").val(data.realName);
            $("#consignee-mobile").val(data.mobilePhone);
            $("#id-card-no").val(data.idCardNo);
            $("#consignee-name").val(data.realName);
            if(data.isDefault){
                $("#set_default_address").prop("checked",true);
            }else{
                $("#set_default_address").prop("checked",false);
            }
            if(data.id==null){
                $("#consignee-id").val();
            }else{
                $("#consignee-id").val(data.id);
            };
            /*var x=$("body").scrollTop=0;
            window.scrollTo(x,x);*/
            $('body,html').animate({scrollTop:0},500);
        });
    });

//身份证
    jQuery.validator.addMethod("idCard", function (value, element) {
        var isIDCard1=/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;//(15位)
        var isIDCard2=/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;//(18位)

        return this.optional(element) || (isIDCard1.test(value)) || (isIDCard2.test(value));
    }, "格式不对");

    $(".js_submit_form").validate({
        submitHandler: function(form) {
            var url=$(".js_submit_form").attr("data-url");
            var options = {
                url: url + "?t=" + new Date().getMilliseconds(),
                type: 'post',
                data: $(".js_submit_form").serialize(),
                success: function (data) {
                    if (data.isSuccess) {
                        alert("保存成功");
                        window.location.href="ticketHolderInfo";
                    }else{
                        alert("保存失败");
                    }
                }
            };
            $.ajax(options);
        }
    });


    $("body").delegate(".js_delete_ticket","click",function(){
        var url=$(this).attr("data-url");
        var content=$(this).attr("data-title");
        $.post(url+"/"+content).done(function (data) {
            if(data.isSuccess){
                alert("删除成功");
                window.location.href="ticketHolderInfo";
            }else{
                alert("删除失败");
            }
        });
    });

    //设置默认取票人
    $("body").delegate(".js_setDefault","click",function(){
        var url=$(this).attr("data-url");
        var pid=$(this).attr("data-title");
        $.post(url+"/"+pid).done(function (data) {
            if(data.isSuccess){
                alert("设置成功");
                window.location.reload(true);
            }else{
                alert("设置失败");
            }
        });
    });

    $(".js_form_ticket").validate({
        submitHandler: function(form) {
            var _url = $(".js_submit_form").attr("data-url");
            var options = {
                url: _url + "?t=" + new Date().getMilliseconds(),
                type: 'post',
                data: $(".js_submit_save").serialize(),
                success: function (data1) {
                    if (data1.isSuccess) {
                       alert("保存成功");
                        window.location.href="ticketHolderInfo";
                    }else{
                       alert("保存失败");
                    }
                }
            };
            $.ajax(options);
        }
    });

});

