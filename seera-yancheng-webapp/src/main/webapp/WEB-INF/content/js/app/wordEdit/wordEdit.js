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
    var callb= function callback() {
        $("#editWordEditInfoForm").validate({

            rules: {
                num: {
                    required: true
                },
                title:{
                    required:true
                },
                time: {
                    required: true
                },
                author:{
                    required:true
                }
            },
            messages: {
                num: {
                    required: "必填"
                },
                title:{
                    required:"必填"
                },
                time:{
                    required:"必填"
                },
                author:{
                    required:"必填"
                }

            },

            submitHandler: function (form) {
                //表单提交句柄,为一回调函数，带一个参数：form
                var _url = $("#editWordEdit").attr("data-url");

                var options = {
                    url: _url + "?t=" + new Date().getMilliseconds(),
                    type: 'post',
                    data: $("#editWordEditInfoForm").serializeObject(),
                    success: function (data) {
                        if (data.isSuccess)
                            alert("修改成功");
                        $("#myModal").modal("hide")
                        loadData();
                    }
                };
                $.ajax(options);
            }

        });

    }
    widget.init(callb);


    $("body").delegate(".deleteWord","click",function(){
        var id = $(this).parent().parent().children(".firstTd").children().children(".checkBtn").val();
        $.ajax({
            url:"ajax_delete_wordEdit"+"?t="+new Date().getMilliseconds()+"&id="+id,
            type:'get',
            success:function(data){
                if(data.isSuccess){
                    alert("删除成功!");
                    loadData();
                }
            }
        })
    })


    //删除文字性编辑页面数据
    var chk_value = [];
    $("#deleteWordEdit").click(function () {

        $('.checkBtn:checked').each(function () {
            chk_value.push($(this).val());
        });
        deleteMenu(chk_value.toString(), false);
    })
    /*$(".dynamicChoose").html(chk_value.length);*/
    function deleteMenu(selectids) {
        var options = {
            url: "ajax_deleteWordEdit?t=" + new Date().getMilliseconds() + "&ids=" + selectids,
            type: 'get',
            success: function (data) {
                if (data.isSuccess) {
                    alert("删除成功");
                    loadData();
                }
                else
                    alert(data);
            }
        };
        $.ajax(options);
    }

})
