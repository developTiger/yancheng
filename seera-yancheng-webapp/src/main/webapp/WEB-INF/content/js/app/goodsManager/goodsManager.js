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


    /*var target;
    if ($("#syy_train_v_01").length>0) {
        target=$("#syy_train_v_01");
        var tpl = require("text!app/tpl/forms/syy_rs_lc01_01_v_Tpl.html");//部门 子表单
    }
    //加载主表单数据
    if($("#syy_train_v").length>0){
        target=$("#syy_train_v");
        var tpl = require("text!app/tpl/forms/syy_rs_lc01_vTpl.html");//人事 主表单
    }
    if(!target) return;*/

    //加载数据
    function loadData(target) {

        var url = target.attr("data-url");
        var tpl;
        if(url == "ajax_goods_query"){
            var tplSearch = require("text!app/tpl/goodsManager/searchGoodsTpl.html");
            $("#searchGoodsOrGoodsItem").html(tplSearch);
            tpl= require("text!app/tpl/goodsManager/goodsTpl.html");

        }
        if(url == "ajax_goodsItem_query"){
            var  tplSearch = require("text!app/tpl/goodsManager/searchGoodsItemTpl.html");
            $("#searchGoodsOrGoodsItem").html(tplSearch)
            tpl= require("text!app/tpl/goodsManager/goodsItemTpl.html");

        }
        List("#table", tpl, url, "", 1, 10);
    }


    $("body").delegate(".goods","click",function(){

        loadData($(this));
        //loadData();

    })


    $("#gList").click();

    $("body").delegate("#goodsListQueryBtn","click",function(){
        var url = "ajax_goodsItem_query";
        var tpl = require("text!app/tpl/goodsManager/goodsItemTpl.html");
        var data = $("#searchForm").serialize().toString();
        List("#table", tpl, url, data, 1, 10);
    })

    $("body").delegate("#goodsManager","click",function(){
        var url = "ajax_goods_query";
        var tpl = require("text!app/tpl/goodsManager/goodsTpl.html");
        var data = $("#searchForm").serialize().toString();
        List("#table", tpl, url, data, 1, 10);
    })


    //时间插件
    $("body").delegate(".Wdate", "click", function () {
        wDatePicker({ dateFmt: 'yyyy-MM-dd ' });
    });

    //商品类目：门票+纪念品
    $("body").delegate("#typeList","change",function(){
        if($(this).val() == "2" || $(this).val() == "3"){
            $("#divSouvenir").show();
            $("#divNumber").show();
        }else{
            $("#divSouvenir").hide();
            $("#divNumber").hide();
        }
    })

    //新增
    var callb= function callback() {
        $("#addGoodsItemForm").validate({

            rules: {
                typeList: {
                    required: true
                },
                name:{
                    required:true,
                    maxlength:50
                },
                prices:{
                    required:true,
                    number:true
                },
                seller:{
                    required:true,
                    maxlength:50
                }
            },
            messages: {
                typeList: {
                    required: "必须选择"
                },
                name:{
                    required:"必填",
                    maxlength:"长度不能超过50个字符"
                },
                prices:{
                    required:"必填",
                    number:"必须输入合法数字"
                },
                seller:{
                    required:"必填",
                    maxlength:"长度不能超过50个字符"
                }

            },

            submitHandler: function (form) {
                //表单提交句柄,为一回调函数，带一个参数：form
                var _url = $("#addOrUpdateGoodsItem").attr("data-url");
                var options = {
                    url: _url + "?t=" + new Date().getMilliseconds(),
                    type: 'post',
                    data: $("#addGoodsItemForm").serializeObject(),
                    success: function (data) {
                        if (data.isSuccess)
                            alert("新增成功");
                        $("#myModal").modal("hide")
                        loadData();
                    }
                };
                $.ajax(options);
            }

        });

    }
    widget.init(callb);

    //删除 商品页面 单个删除
    $("body").delegate(".deleteGoodsList","click",function(){

        var _url = $(this).attr("data-url")
        var id = $(this).parent().parent().children(".firstTd").children().children(".checkBtn").val();
        $.ajax({
            url:_url+"?t"+new Date().getMilliseconds()+"&id="+id,
            type:'get',
            success:function(data){
                if(data.isSuccess){
                    alert("删除成功！")
                }else{
                    alert("删除失败！")
                }
            }
        })
    })

    //删除 商品页面 单个删除
    $("body").delegate(".deleteGoodsItem","click",function(){

        var _url = $(this).attr("data-url")
        var id = $(this).parent().parent().children(".firstTd").children().children(".checkBtn").val();
        $.ajax({
            url:_url+"?t"+new Date().getMilliseconds()+"&id="+id,
            type:'get',
            success:function(data){
                if(data.isSuccess){
                    alert("删除成功！")
                }else{
                    alert("删除失败！")
                }
            }
        })
    })

    //设置状态
    $("body").delegate(".setStatus","click",function(){
        var status = $(this).parent().parent().children(".firstTd").children().children(".checkBtn").attr("data-value");
        var id = $(this).parent().parent().children(".firstTd").children().children(".checkBtn").val();
        $.ajax({
            url:"ajax_set_goodsStatus"+"?t="+new Date().getMilliseconds()+"&status="+status+"&id="+id,
            type:'get',
            success:function(data){
                if(data == "success"){
                    alert("商品状态设置成功！");
                }else{
                    alert("商品状态设置失败!")
                }
            }
        })
    })


    //删除商品页面数据 批量删除
    var chk_value = [];
    $("body").delegate("#Js_deleteGoods","click",function(){
        $('.checkBtn:checked').each(function () {
            chk_value.push($(this).val());
        });
        deleteMenu1(chk_value.toString(), false);
    })

    function deleteMenu1(selectids) {
        var options = {
            url: "ajax_delete_goods_list?t=" + new Date().getMilliseconds() + "&ids=" + selectids,
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

    //删除商品项页面数据 批量删除
    var chk_value = [];
    $("body").delegate("#Js_deleteGoodItems","click",function(){
        $('.checkBtn:checked').each(function () {
            chk_value.push($(this).val());
        });
        deleteMenu(chk_value.toString(), false);
    })

    function deleteMenu(selectids) {
        var options = {
            url: "ajax_deleteGoodsItemsList?t=" + new Date().getMilliseconds() + "&ids=" + selectids,
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

    //判断选中的个数
    var numberLength=0;
    $("body").delegate(".checkBtn","ifChecked",function(){
        var i=0;
        var number = [];
        $(".checkBtn").each(function(){
            if($(this).prop("checked")){
                i++;
                number.push(i);
            }
        })
        numberLength=number.length;
        $(".dynamicChoose").html(number.length);
    })
    $("body").delegate(".checkBtn","ifUnchecked",function(){
                numberLength--;
        $(".dynamicChoose").html(numberLength);
    })



})
