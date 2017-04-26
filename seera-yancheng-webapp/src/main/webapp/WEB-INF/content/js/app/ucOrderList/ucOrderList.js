

define(function(require,exports,module){

  /*  $("body").delegate(".order-search","click",function(){
        var orderSearch=$(".js_order_content").val();
        var startTime=$().val();
        var endTime=$().val();
        var orderType=$().val();
        var orderStatus=$().val();
        $.post("ajax_search_order/" + encodeURI(orderSearch)).done(function (data) {
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
    });*/
   // require("../common/jquery.page");
    require("../common/kkpager.min");

    var wDatePicker=require("wdatePicker");
    $("body").delegate(".Wdate", "click", function () {
        wDatePicker({minDate: '%y-%M-{%d+1}', dateFmt: 'yyyy-MM-dd'});
    })

    /*$(".tcdPageCode").createPage({
        pageCount:6,
        current:1,
        backFn:function(p){
            console.log(p);
        }
    });*/
    function getParameter(name) {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r!=null) return unescape(r[2]); return null;
    };

    $("body").delegate(".js_show_alert","click",function(){
        $(".real-name-hint").css({"display":"block"});
        $(".box-shadow").css({"display":"block"});
        $(".js_show_original_date").html($(this).attr("data-url"));
        var tourDate=$(this).attr("data-url");
        var hotelDate=$(this).attr("data-title");
        var productId=$(this).attr("data-id");
        var str="";
        if(tourDate!=null){
            str+="<div class='validate-name'><label>入园日期：</label><span class='js_show_tour_date'>"+tourDate+"</span></div><div class='validate-ID'>" +
            "<label>改签日期：</label><input type='text' class='Wdate js_edit_tour_date' data-id="+productId+"></div>";
        }
        if(hotelDate!=null){
            str+="<div class='validate-name'><label>入住日期：</label><span class='js_show_hotel_date'>"+hotelDate+"</span></div>" +
            "<div class='validate-ID'><label>改签日期：</label><input type='text'class='Wdate js_edit_hotel_date' data-id="+productId+"></div>";
        }
        $(".js_showTime").html(str);
      /*  var temp=$(this);
        $(this).closest("tr").find("span input[type=text]").each(function(){

            if($(this).hasClass("js_mark")) {
                $(this).attr("disabled", false);
                $(this).removeClass("show-time-back");
                $(this).addClass("Wdate");
                temp.addClass("dis-none");
                temp.closest("td").find("input[type=button]").removeClass("dis-none");
                temp.closest("td").find("input[type=button]").attr("disabled",false);

            }
        });*/
    });

    $("body").delegate(".js_sure_show_submit","click",function(){
        var temp= $(this).closest("tr").find("span input[type=text]");
 /*       var mealArray=[];*/
        var tourDate;
        var hotelDate;
        var orderProductId;
        temp.each(function(index,element) {
            if($(this).hasClass("js_mark")){
                orderProductId=$(this).attr("data-id");
                var dateType=$(this).attr("data-title");
                if(dateType=="tour")
                    tourDate =$(this).val();
                if(dateType=="hotel")
                    hotelDate=$(this).val();
            }
        });
        $.post("/ajax_update_date",{"tourDate": tourDate,"hotelDate":hotelDate,"orderProductId":orderProductId}).done(function (data){
            if(data.isSuccess){
                alert("改签成功!!!");
                window.location.reload(true);
            }else{
                alert(data.msg);
            }
        });
    });


    $("body").delegate(".cross","click",function(){
        $(".real-name-hint").css({"display":"none"});
        $(".box-shadow").css({"display":"none"});
    });

    $("body").delegate(".js_sure_edit","click",function(){

        var tourDate=$(".js_edit_tour_date").val();
        var hotelDate=$(".js_edit_hotel_date").val();
        var productId=$(".js_edit_tour_date").attr("data-id");
        $.post("/ajax_update_date", {"tourDate": tourDate,"hotelDate":hotelDate,"orderProductId": productId}).done(function (data) {
            if(data.isSuccess){
                alert("改签成功!!!");
                window.location.reload(true);
            }else{
                alert(data.msg);
            }
        });
    });

    $("body").delegate(".hint-canel","click",function(){
        $(".real-name-hint").css({"display":"none"});
        $(".box-shadow").css({"display":"none"});
    });

    /*$(function(){
        var totalPage = 20;
        var totalRecords = 390;
        var pageNo = getParameter('pno');
        if(!pageNo){
            pageNo = 1;
        }
        //初始化分页控件
        //有些参数是可选的，比如lang，若不传有默认值
        kkpager.init({
            pno : pageNo,
            //总页码
            total : totalPage,
            //总数据条数
            totalRecords : totalRecords,
            //链接前部
            hrefFormer : 'orderlist',
            //链接尾部
           // hrefLatter : '.html',
            getLink : function(n){
                return this.hrefFormer + this.hrefLatter + "?pno="+n;
            },
            lang : {
                prePageText : '上一页',
                nextPageText : '下一页',
                totalPageBeforeText : '共',
                totalPageAfterText : '页',
                totalRecordsAfterText : '条数据',
                gopageBeforeText : '转到',
                gopageButtonOkText : '确定',
                gopageAfterText : '页',
                buttonTipBeforeText : '第',
                buttonTipAfterText : '页'
            }
        });
        //生成
        kkpager.generPageHtml();
    });*/

})

