define(function (require, exports, module) {

    require("ajaxUpload");
    var validate = require("validate");//对表单进行验证
    require("kindeditor");
    require("../common/jquery.serializeObject");//对提交的数据 进行序列化 返回一个object
    var tpl_img = require("text!app/tpl/product/addProductPicturesTpl.html");
    require("../common/templeteHelper");

    var editorProfile = KindEditor.create('#profile', {
        heigh: '250px',
        resizeType: 1,
        width: '100% !important',
        allowPreviewEmoticons: true,
        allowImageUpload: true,
        uploadJson : 'kindEditUpload', //上传
        fileManagerJson : 'kindEdit_file_manager', // 文件管理
        allowFileManager : true,
        fontSizeTable: ['250px', '14px', '280px'],
        cssData: 'body{}'
    });
    var  editorNotice = KindEditor.create('#notice', {
        heigh: '250px',
        resizeType: 1,
        width: '100% !important',
        allowPreviewEmoticons: true,
        allowImageUpload: true,
        uploadJson : 'kindEditUpload', //上传
        fileManagerJson : 'kindEdit_file_manager', // 文件管理
        allowFileManager : true,
        fontSizeTable: ['250px', '14px', '280px'],
        cssData: 'body{}'
    });

    var  editorTrafficGuide = KindEditor.create('#trafficGuide', {
        heigh: '250px',
        resizeType: 1,
        width: '100% !important',
        allowPreviewEmoticons: true,
        uploadJson : 'kindEditUpload', //上传
        fileManagerJson : 'kindEdit_file_manager', // 文件管理
        allowFileManager : true,
        allowImageUpload: true,
        fontSizeTable: ['250px', '14px', '280px'],
        cssData: 'body{}'
    });

    $("#editForm").validate({
        submitHandler: function (form) {
            //表单提交句柄,为一回调函数，带一个参数：form

            editorProfile.sync();
            editorNotice.sync();
            editorTrafficGuide.sync();
            var _url = $("#editForm").attr("data-url");
            var options = {
                url: _url + "?t=" + new Date().getMilliseconds(),
                type: 'post',
                data: $("#editForm").serializeObject(),
                success: function (data) {
                    if (data.isSuccess)
                        alert("修改成功");
                }
            };
            $.ajax(options);
        }

    });
})
