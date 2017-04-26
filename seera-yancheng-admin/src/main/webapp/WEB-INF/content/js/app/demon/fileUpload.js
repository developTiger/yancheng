define(function (require, exports, module) {

    require("jquery");
    require("ajaxUpload");
    require("../common/jquery.serializeObject");
    $("#upload").click(function(){

        $.ajaxFileUpload({
            //处理文件上传操作的服务器端地址(可以传参数,已亲测可用)
            url:'ajax_fileupload',
            secureuri:false,                       //是否启用安全提交,默认为false
            fileElementId:['myBlogImage','dddd'],           //文件选择框的id属性
            dataType:'json',                       //服务器返回的格式,可以是json或xml等
            data:$("#test").serializeObject(),
            success:function(data, status){        //服务器响应成功时的处理函数
                data = data.replace("<PRE>", '');  //ajaxFileUpload会对服务器响应回来的text内容加上<pre>text</pre>前后缀
                data = data.replace("</PRE>", '');
                data = data.replace("<pre>", '');
                data = data.replace("</pre>", ''); //本例中设定上传文件完毕后,服务端会返回给前台[0`filepath]
                if(data.substring(0, 1) == 0){     //0表示上传成功(后跟上传后的文件路径),1表示失败(后跟失败描述)
                    $("img[id='uploadImage']").attr("src", data.substring(2));
                    $('#result').html("图片上传成功<br/>");
                }else{
                    $('#result').html('图片上传失败，请重试！！');
                }
            },
            error:function(data, status, e){ //服务器响应失败时的处理函数
                $('#result').html('图片上传失败，请重试！！');
            }
        });
    })

})