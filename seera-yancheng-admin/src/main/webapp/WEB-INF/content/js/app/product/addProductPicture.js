define(function (require, exports, module) {

    require("kindeditor");
    var pf = require("photoClip");
    require("hammer");
    require("iscrollzoom");
    require("ajaxUpload");
    require("lrzallbundle");
    require("../common/jquery.serializeObject");//对提交的数据 进行序列化 返回一个object
    var tpl_img = require("text!app/tpl/product/addProductPicturesTpl.html");
    require("../common/templeteHelper");

    var id = 3

    $("body").delegate(".addGoodsImg", "click", function () {
        var imgData = {num: id};
        var html = template.compile(tpl_img)(imgData);
        $("#addGoodsImgs").append(html);
        id++;
    }).delegate(".deleteGoodsImg", "click", function () {
        $(this).parent().parent().remove();
    });


    $(".del_image").click(function(){

        var _url = $(this).attr("data-url");
        var options = {
            url:encodeURI(_url + "&t=" + new Date().getMilliseconds()),
            type: 'get',
            success: function (data) {
                if (data.isSuccess) {
                    alert("删除成功");
                    window.location.reload();
                }
            }
        };
        $.ajax(options);

    });
    $("body").delegate(".Js_uploadFile", "click", function () {
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
    })

})
