/**
 * Created by liulin on 2016/7/16.
 */

define(function(require,exports,module){

    var List = require("../common/pagelist");//分页
    var check = require("../common/checkbox");//复选框
    var wDatePicker = require("wdatePicker");//时间插件
    //widget插件作用：点击按钮，弹出模态框，对按钮进行初始化
    var widget = require("../common/widget");

    check.checkAll("body", ".checkAll", ".checkBtn")
    loadData();
    $("#orderQuery").click(function () {
        loadData();
    });

    //加载数据
    function loadData() {
        var tpl = require("text!app/tpl/orderManager/orderMealTpl.html");
        var url = $("#searchForm").attr("data-url");
        var data = $("#searchForm").serialize().toString();
        List("#table", tpl, url, data, 1, 10);
    };

    $("body").delegate(".js_mealCheck","click",function(){

        var id=$("#id").val();

        var orderNum=$("#orderNum").val();
        var url=$(this).attr("data-url");
        var options = {
            url: url + "?t=" + new Date().getMilliseconds(),
            type: 'post',
            data:{id:id,orderNum:orderNum},
            success: function (data) {
                if (data.isSuccess) {
                    alert("改签成功");
                    $("#myModal").modal("hide");
                    window.location.reload(true);
                }else{
                    alert(data.msg);
                }
            }
        };
        $.ajax(options);

    });


    //时间插件
    $("body").delegate(".Wdate", "click", function () {
        wDatePicker({ dateFmt: 'yyyy-MM-dd' });
    });

    //订单审核
    var callb= function callback() {
        $("#addCarInfoForm").validate({

            rules: {
                carType: {
                    required: true,
                    maxlength:50
                },
                carNo:{
                    required:true,
                    maxlength:50
                    //digits:true
                },
                companyId: {
                    required: true
                },
                status:{
                    required:true
                },
                repairLog:{
                    required:true
                },
                controlName:{
                    required:true,
                    maxlength:50
                }
            },
            messages: {
                carType: {
                    required: "必填",
                    maxlength:"长度不能超过50个字符"
                },
                carNo:{
                    required:"必填",
                    maxlength:"长度不能超过50个字符"

                },
                companyId:{
                    required:"必须选择"
                },
                status:{
                    required:"必须选择"
                },
                repairLog:{
                    required:"必填"
                },
                controlName:{
                    required:"必填",
                    maxlength:"长度不能超过50个字符"
                }

            },

            submitHandler: function (form) {
                //表单提交句柄,为一回调函数，带一个参数：form
                var _url = $("#addOrUpdateCar").attr("data-url");

                var options = {
                    url: _url + "?t=" + new Date().getMilliseconds(),
                    type: 'post',
                    data: $("#addCarInfoForm").serializeObject(),
                    success: function (data) {
                        if (data == "success")
                            alert("新增成功");
                        $("#myModal").modal("hide")
                        loadData();
                        window.location.href="sra_c_car";
                    }
                };
                $.ajax(options);
            }
        });
    }
    widget.init(callb);
})
