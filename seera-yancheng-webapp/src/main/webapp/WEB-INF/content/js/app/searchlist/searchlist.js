
define(function (require, exports, module) {
    require("jquery");
    require("../../common/common");

    $("#prev").click(function(){
        var searchType= $("input[name=searchType]").val();
        var toPage=$(".paging").html()-1;
        var searchContent=$("input[name=searchContent]").val();
        var kind=$(this).attr("data-title");
        if(kind==null || kind=="")
            window.location.href="searchlist?searchType="+searchType+"&searchContent="+searchContent+"&toPage="+toPage;
        else
            window.location.href="searchlist?kind="+kind+"&toPage="+toPage;
    });

    $("#next").click(function(){
        var searchType= $("input[name=searchType]").val();
        var toPage=parseInt($(".paging").html())+1;
        var searchContent=$("input[name=searchContent]").val();
        var kind=$(this).attr("data-title");
        if(kind==null || kind=="")
            window.location.href="searchlist?searchType="+searchType+"&searchContent="+searchContent+"&toPage="+toPage;
        else
            window.location.href="searchlist?kind="+kind+"&toPage="+toPage;
});
})

