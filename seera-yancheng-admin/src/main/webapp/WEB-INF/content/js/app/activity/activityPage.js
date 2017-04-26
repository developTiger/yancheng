/**
 * Created by liulin on 2016/7/16.
 */

define(function (require, exports, module) {

    var List = require("../common/pagelist");//分页
    var check = require("../common/checkbox");//复选框
    var wDatePicker = require("wdatePicker");//时间插件
    //widget插件作用：点击按钮，弹出模态框，对按钮进行初始化
    var widget = require("../common/widget");
    require("ajaxUpload");
    var validate = require("validate");//对表单进行验证
    require("../common/jquery.serializeObject");//
    require("validate");
    require("kindeditor");

    var editorProfile = KindEditor.create('#profile', {
        height: '1050px',
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

    $("#editForm").validate({
        submitHandler: function (form) {
            //表单提交句柄,为一回调函数，带一个参数：form
            editorProfile.sync();
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
