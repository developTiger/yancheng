define(function(require,exprots,model){
	
	require("jquery");

    require("ajaxUpload");
    var List = require("../common/pagelist");//分页
    var check = require("../common/checkbox");//复选框
    var wDatePicker = require("wdatePicker");//时间插件
    //widget插件作用：点击按钮，弹出模态框，对按钮进行初始化
    var widget = require("../common/widget");
    require("../common/jquery.serializeObject");

    check.checkAll("body", ".checkAll", ".checkBtn")
    loadData();
    $("#touristQueryBtn").click(function () {
        loadData();
    });

    //加载数据
    function loadData() {
        var tpl = require("text!app/tpl/editPicture/editPictureTpl.html");
        var url = $("#searchForm").attr("data-url");
        var data = $("#searchForm").serialize().toString();
        List("#table", tpl, url, data, 1, 10);
    };

    $("body").delegate(".Js_uploadFile", "click", function () {
        var type=   $(this).attr("data-type");
        var pid = $("#pid").val();
        var location=$("#location").val();
        var pictureType=$("#type option:selected").val();
        $.ajaxFileUpload({
            //处理文件上传操作的服务器端地址(可以传参数,已亲测可用)
            url: 'ajax_add_webPicture',
            secureuri: true,                       //是否启用安全提交,默认为false
            fileElementId: [type],           //文件选择框的id属性
            dataType: 'text',                       //服务器返回的格式,可以是json或xml等
            data: $("#picEdicForm").serializeObject(),
            success: function (data, status) {        //服务器响应成功时的处理函数
                var resultStart = data.indexOf("{");
                var resultEnd = data.indexOf("}");
                var result = JSON.parse(data.substring(resultStart, resultEnd + 1));
                if (result.isSuccess) {     //0表示上传成功(后跟上传后的文件路径),1表示失败(后跟失败描述)
                    alert("上传文件成功！");
                    window.location.reload();
                } else {
                    alert(result.msg);
                }
            },
            error: function (data, status, e) { //服务器响应失败时的处理函数
                $('#result').html('文件上传失败，请重试！！');
            }
        });
    })
    $("body").delegate("#Js_resetPic", "click", function () {
        $.getJSON("http://store.cn-yc.com.cn/resetPic", function (data) {
                if (data.isSuccess) {
                    alert("操作成功");
                }
                else {
                    alert(data.msg);
                }
            }
        )
    });

    $("body").delegate(".disabledStatus","click",function(){
        var id = $(this).attr("data-id");
        var title=$(this).attr("data-title");
        $.ajax({
            url:"ajax_update_picture"+"?t="+new Date().getMilliseconds()+"&id="+id+"&title="+title,
            type:'get',
            success:function(data){
                if(data.isSuccess){
                    //alert("状态设置成功");
                    loadData();
                }
            }
        })
    })


    $("body").delegate(".enableStatus","click",function(){
        var id = $(this).attr("data-id");
        var title=$(this).attr("data-title");
        $.ajax({
            url:"ajax_update_picture"+"?t="+new Date().getMilliseconds()+"&id="+id+"&title="+title,
            type:'get',
            success:function(data){
                if(data.isSuccess){
                    //alert("状态设置成功");
                    loadData();
                }
            }
        })
    })


    /* $("body").delegate(".Js_uploadFile", "click", function () {
         var type=   $(this).attr("data-type");
         var pid = $("#pid").val();
         $.ajaxFileUpload({
             //处理文件上传操作的服务器端地址(可以传参数,已亲测可用)
             url: 'ajax_add_productPicture',
             secureuri: true,                       //是否启用安全提交,默认为false
             fileElementId: [type],           //文件选择框的id属性
             dataType: 'text',                       //服务器返回的格式,可以是json或xml等
             data: {id:pid,fileType:type},
             success: function (data, status) {        //服务器响应成功时的处理函数
                 var resultStart = data.indexOf("{");
                 var resultEnd = data.indexOf("}");
                 var result = JSON.parse(data.substring(resultStart, resultEnd + 1));

                 if (result.isSuccess) {     //0表示上传成功(后跟上传后的文件路径),1表示失败(后跟失败描述)
                     alert("上传文件成功！");
                     window.location.reload();

                 } else {
                     alert(result.msg);
                 }
             },
             error: function (data, status, e) { //服务器响应失败时的处理函数
                 $('#result').html('文件上传失败，请重试！！');
             }
         });
     });*/
})