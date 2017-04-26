/**
 * Created by kkk on 2017/1/12.
 */


$(function(){

    if(location.hash !="#light1" && location.hash!="" ){
        JumpUrl()
    }

    var num=$("input[name=num]").val();
    checkUrl();
    function checkUrl(){
    }

    function JumpUrl(){
        $.router.load("#light1", true);
    }


    $(document).on("pageInit", function(e, pageId, $page) {
        window.pageId = pageId;
    });

    window.addEventListener('popstate', function () {
       // $.router.load("#"+window.pageId, true);

        window.location.href="/light/"+num;
    });

    function checkAnswer(answer,answering){
        var arr = [];
        answering=answering.trim();
        if(answer.indexOf("／")>=0) {
            arr = answer.split("／");
        }else{
            arr = answer.split("/");
        }
        for(var i=0;i<arr.length;i++){
            if(arr[i]==answering){
                return true;
            }
        }
        return false;

    }

    $("#btn1").click(function(){
        var answer=$("input[name=answer1]").val();
        var answering=$("textarea[name=answering1]").val();
        if(checkAnswer(answer,answering)) {
            var count=$("input[name=count]").val();
            $("input[name=count]").val(++count);
            $.router.load("#light2", false);
        }else{
            var num=$("input[name=num]").val();
            window.location.href="/result/"+num;
        }
    });
    $("#btn2").click(function(){
        var answer=$("input[name=answer2]").val();
        var answering=$("textarea[name=answering2]").val();
        if(checkAnswer(answer,answering)) {
            var count=$("input[name=count]").val();
            $("input[name=count]").val(++count);
            $.router.load("#light3", false);
        }else{

            $(".js_hidden2").removeClass("hidden");
        }

    });
    $("#btn3").click(function(){
        var answer=$("input[name=answer3]").val();
        var answering=$("textarea[name=answering3]").val();
        if(checkAnswer(answer,answering)) {
            var count=$("input[name=count]").val();
            $("input[name=count]").val(++count);
            $.router.load("#light4", false);
        }else{
            $(".js_hidden3").removeClass("hidden");
        }

    });
    $("#btn4").click(function(){
        var answer=$("input[name=answer4]").val();
        var answering=$("textarea[name=answering4]").val();
        if(checkAnswer(answer,answering)) {
            var count=$("input[name=count]").val();
            $("input[name=count]").val(++count);
            $.router.load("#light5", false);
        }else{
            $(".js_hidden4").removeClass("hidden");
        }

    });
    //$("#btn5").click(function(){
    //    var typeId=$("input[name=Id0]").val();
    //    var count=$("input[name=count]").val();
    //    var answer=$("input[name=answer5]").val();
    //    var answering=$("textarea[name=answering5]").val();
    //    if(checkAnswer(answer,answering)) {
    //        count++;
    //        $.ajax({
    //            url:"/ajax_subject_result?t="+new Date().getMilliseconds(),
    //            type:'post',
    //            data:{typeId:typeId,count:count},
    //            success:function(data){
    //                var countRes = data.resultCount;
    //                var couponId;
    //                if (data.coupon) {
    //                    couponId = data.coupon.id;
    //
    //                }
    //                if(data.msg==null) {
    //                    if (countRes == 5)
    //                        window.location.href = "/receiving?typeId=" + typeId;
    //                }else{
    //                    if(countRes==4){
    //                        $.alert(data.msg);
    //                        window.location.href="/coupon?count="+countRes+"&couponId="+couponId+"&num"+num;
    //                    }
    //                }
    //            }
    //        });
    //    }else{
    //        $(".js_hidden5").removeClass("hidden");
    //    }
    //});
    $(".reward").click(function(){
        var count=$("input[name=count]").val();
        if($(this).hasClass("btn5")){
                var answer=$("input[name=answer5]").val();
                    var answering=$("textarea[name=answering5]").val();
                    if(checkAnswer(answer,answering)) {
                        count++ ;
                    }
        }
        var typeId=$("input[name=Id0]").val();

        $("#lcount").val(count);
        $("#resultForm").submit();
        //$.ajax({
        //    url:"/ajax_subject_result?t="+new Date().getMilliseconds(),
        //    type:'post',
        //    data:{typeId:typeId,count:count},
        //    success:function(data){
        //        var countRes=data.resultCount;
        //        var couponId;
        //        if(data.coupon){
        //            couponId=data.coupon.id;
        //            $("#lcouponId").val(couponId);
        //        }
        //        if(countRes<5){
        //
        //            $("#resultForm").submit();
        //        }
        //    }
        //});
    });

})