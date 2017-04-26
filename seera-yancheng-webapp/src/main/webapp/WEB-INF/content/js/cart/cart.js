define(function (require, exports, module) {
    var shoppingcart = require("../common/shoppingcat");
    var sc = new shoppingcart({reduceBtn: ".btnReduce", addBtn: ".btnPlus", txtNum: ".txtNum"});
    sc.init();
    var checkAll=require("../common/checkAll");
    var wDatePicker = require("wdatePicker");
    $("body").delegate(".Wdate", "click", function () {
        wDatePicker({minDate: '%y-%M-{%d}', dateFmt: 'yyyy-MM-dd'});
    })
    $(function(){
        executeCount();
    })
        $(".txtNum").change(function(){
            var count=(+$(this).val())*(+$(this).data("price"))
            $(this).closest("tr").find(".cart-Money").find("span").html(count.toFixed(2));
            executeCount();
        })
        $(".Js_check").change(function(){
            executeCount();
        })
        function executeCount(){
            var count=0;
            $(".Js_check:checked").each(function(){
                count+=(+$(this).closest("tr").find(".cart-Money").find("span").html());
            })
            $(".moneyShow").html(count.toFixed(2))
        }

        var ca=new checkAll(".shopCart",".checkAll",function(){
            executeCount();
        });

        $(".js_del").click(function () {
            if(confirm("确定删除吗？")){
                var shoppingItemId = $(this).data("id");
                $.post("ajax_cart_singleRemoveCount/" + shoppingItemId).done(function (data) {
                    if (data.isSuccess) {
                        window.location.reload(true);
                    }
                });
            }

        });

        $(".js_del_select").click(function () {
            if(confirm("确定删除吗？")){
                var ids = [];
                $(".js_chk_shopping_item_id:checked").each(function () {
                    ids.push($(this).data("id"));
                });
                $.post("ajax_sc_removeItemCount", {"ids": ids.join()}).done(function (data) {
                    if (data.isSuccess) {
                        window.location.reload(true);
                    }
                })
            }

        })
    })
$(".orderConfirm").click(function(){
    var confirmMap=[];
    var orderProducts= [];
    $(".Js_check:checked").each(function(index,element){
        var orderProduct={};
        var obj=$(this).closest("tr").find(".txtNum");
        var id  =Number(obj.attr("data-id"));
        var count =Number(obj.val());
        var hotelDate = $(this).closest("tr").find(".hotelScheduleDate").val();
        var tureDate= $(this).closest("tr").find(".tourScheduleDate").val();

        orderProducts.push("orderProducts["+index+"].productId="+id);
        orderProducts.push("orderProducts["+index+"].count="+count);

        if(tureDate!=undefined)
            orderProducts.push("orderProducts["+index+"].tourScheduleDate="+tureDate);
        if(hotelDate!=undefined)
            orderProducts.push("orderProducts["+index+"].hotelScheduleDate="+hotelDate);
    })
    $.post("/ajax_order_confirm", orderProducts.join("&"),function(data){
        if(data.isSuccess){
            window.location.href="/orderConfirm"
        }else{
            alert("请选择商品")
        }
    })
})

