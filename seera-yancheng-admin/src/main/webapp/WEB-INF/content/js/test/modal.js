/**
 * Created by zhouzh on 2016/5/19.
 */
define(function (require, exports, module) {
    require("jquery");
    require("bootstrap");
    var widget=require("./common/widget");
    $(function(){

        widget.init();

        $(".modalBtn").click(function(){
            //自己何必写这个玩意儿呢，不过貌似用起来也不错
            $.post("/test",function(data){
                widget.modal({
                    title:"随便写写",
                    body:data,
                    id:"test1"
                });
            })
            //更简单的写法在下面。。。
            //$("#myModal").modal({
            //    remote: "/test"
            //});

        })
        $(".modalBtn2").click(function(){
            $.post("/test",function(data){
                widget.modal({
                    title:"随便写写",
                    body:data,
                    id:"test2"
                });
            })
        })
    })
})