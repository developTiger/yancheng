/**
 * Created by liulin on 2016/7/16.
 */

define(function(require,exports,module){

    var List = require("../common/pagelist");//分页
    var check = require("../common/checkbox");//复选框
    var wDatePicker = require("wdatePicker");//时间插件
    //widget插件作用：点击按钮，弹出模态框，对按钮进行初始化
    var widget = require("../common/widget");
    var validate = require("validate");//对表单进行验证
    require("../common/jquery.serializeObject");//对提交的数据 进行序列化 返回一个object


    require("kindeditor");
    KindEditor.create('textarea[name="content"]',{
        resizeType:0,
        heigh:'150px',
        width:"100%",
        allowPreviewEmoticons:true,
        allowImageUpload:true,
        fontSizeTable:['150px','16px','280px'],
        cssData:'body{}',
        afterCreate:(function() {
            this.sync();
        })
    });



    check.checkAll("body", ".checkAll", ".checkBtn")
    loadData();
    $("#backstageUserQueryBtn").click(function () {
        loadData();
    });

    //加载数据
    function loadData() {
        var tpl = require("text!app/tpl/wordEdit/wordEditTpl.html");
        var url = $("#searchForm").attr("data-url");
        var data = $("#searchForm").serialize().toString();
        List("#table", tpl, url, data, 1, 10);
    }

    //时间插件
    $("body").delegate(".Wdate", "click", function () {
        wDatePicker({ dateFmt: 'yyyy-MM-dd ' });
    });

    //新增

        $("#editWordEditInfoForm").validate({

            rules: {
                title: {
                    required: true
                },
                content:{
                    required:true
                },
                author:{
                    required:true
                }
            },
            messages: {
                title: {
                    required: "必填"
                },
                content:{
                    required:"必填"
                },
                author:{
                    required:"必填"
                }
            },

            submitHandler: function (form) {
                //表单提交句柄,为一回调函数，带一个参数：form
                var _url = $("#editWordEdit").attr("data-url");

                var content = $("iframe").contents().find("body").text();//获取iframe里面的body
                $("#hiddenCon").val(content);

                var options = {
                    url: _url + "?t=" + new Date().getMilliseconds(),
                    type: 'post',
                    data: $("#editWordEditInfoForm").serializeObject(),
                    success: function (data) {
                        if (data.isSuccess)
                            alert("新增成功");
                            window.location.href="sra_t_w";
                    }
                };
                $.ajax(options);
            }
        });



})
