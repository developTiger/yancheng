/**
 * Created by zhouz on 2016/5/15.
 */
define(function (require, exports, module) {

    require("../../common/jquery.imagezoom.min");
    $(".jqzoom").imagezoom();

    $(".right-coupon-info-show label").click(function () {
        $(this).addClass("current").siblings().removeClass("current");
        $("#" + $(this).attr("data-div")).removeClass("hidden").siblings(".right-coupon-content").addClass("hidden")
    });

    $(".js_delete_btn").click(function(){
        var deleteId=$(this).attr("data-title");
        $.ajax({
            url: "ajax_delete_one_ticket?t="  + new Date().getMilliseconds(),
            type: 'post',
            data:"deleteId="+deleteId,
            success: function (data) {
                if (data.isSuccess) {
                    alert("删除成功");
                    window.location.reload(true);
                } else {
                    alert("删除失败");
                }
            }

        });
    });

    $(".Js_checkBtn").click(function(){
        var js_check_btn=$(".js_coupon_number").val();
        var option={
            url:"add_ticket" + "?t="+new Date().getMilliseconds(),
            type:'post',
            data:{"add_ticket":js_check_btn},
            success:function (data){
                if(data.isSuccess){
                    alert("绑定成功");
                }else{
                    alert("绑定失败");
                }
            }
        };
        $.ajax(option);
    });

});