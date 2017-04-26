/**
 * Created by temp on 2016/10/21.
 */


define(function(require,exports,module){

    require("../common/kkpager.min");

    var wDatePicker=require("wdatePicker");

    $("body").delegate(".Wdate", "click", function () {
        wDatePicker({minDate: '%y-%M-{%d}', dateFmt: 'yyyy-MM-dd'});
    });

    $("body").delegate(".js_save_update_password","click",function(){

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
                    $(".checkError").text(data.msg);
                }
            }
        };
        $.ajax(options);
    });


})

