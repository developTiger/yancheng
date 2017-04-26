/**
 * Created by zhouzh on 16/5/27.
 */
define(function (require, exports, module) {
    var validate = require("validate");

    $("#editForm").validate({
        rules: {
            text1: "required",
            text2: "required"

        },
        messages: {
            text1: "请输入您的名字",
            text2: "请输入您的姓氏"

        }
    })
})