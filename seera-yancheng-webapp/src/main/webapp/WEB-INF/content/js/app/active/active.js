

define(function (require, exports, module) {

    var List = require("../common/pagelist");//分页
    var check = require("../common/checkbox");//复选框

    check.checkAll("body", ".checkAll", ".checkBtn")
    loadData();
    function loadData() {
        var tpl = require("text!app/tpl/acticity/acticityTpl.html");
        var url = "/active/ajax_query_activity";
        var id = $("#activity").val();
        var options = {
            url: url + "?t=" + new Date().getMilliseconds() + "&id=" +id,
            type: 'post',
            success: function (data) {
                if (data) {
                    $("#table").html(data.msg);
                }
                else {
                    alert("暂无活动内容");
                }
            }
        };
        $.ajax(options);
    };
})
