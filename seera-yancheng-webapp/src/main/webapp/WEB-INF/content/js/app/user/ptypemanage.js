/**
 * Created by MJ006 on 2016/6/3.
 */
define(function (require, exports, module) {
    require('../init');
    var template = require("template");
    var List = require("../common/pagelist");
    require("../common/jquery.serializeObject");
    var widget = require("../common/widget");
    var check = require("../common/checkbox");
    var validate = require("validate");
    check.checkAll("body", ".checkAll", ".checkBtn")
    loadData();
    $("#ptypemanageQueryBtn").click(function () {
        loadData();
    });

    function loadData() {
        var tpl = require("text!app/tpl/parameter/parameterTableTbl.html");
        var url = $("#searchForm").attr("data-url");
        var data = $("#searchForm").serialize().toString();
        List("#table", tpl, url, data, 1, 10);
    }
    $("body").delegate("#cancelModal", "click", function () {
        $("#myModal").modal("hide")
    });
    $("#disableUser").click(function () {
        var chk_value = [];
        $('.checkBtn:checked').each(function () {
            chk_value.push($(this).val());
        });
        setUserStaus(chk_value.toString(), false);
    })

    $("#enableUser").click(function () {
        var chk_value = [];
        $('.checkBtn:checked').each(function () {
            chk_value.push($(this).val());
        });
        setUserStaus(chk_value.toString(), true);
    })
    var validate = function(){

    }

    widget.init(validate);

});
