define(function (require, exports, module) {

    require("ajaxUpload");
    var wDatePicker = require("wdatePicker");//时间插件
    //widget插件作用：点击按钮，弹出模态框，对按钮进行初始化
    var widget = require("../common/widget");
    var validate = require("validate");//对表单进行验证
    require("../common/jquery.serializeObject");//对提交的数据 进行序列化 返回一个object
    var tpl = require("text!app/tpl/product/addProductItemNameAndNumTpl.html");
    require("../common/templeteHelper");

    $("body").delegate(".Wdate", "click", function () {
        wDatePicker({dateFmt: 'yyyy-MM-dd'});
    }).delegate(".addGoodsItem", "click", function () {
        if (html == "") {
            get_goodsSelector();
        }
        $("#addgoodsItemNameAndNum").append(html);

    }).delegate(".chooseGoodsItem", "change", function () {
        if ($(this).val() == "2" || $(this).val() == "3") {
            $(this).parent().next().next().children("#plus").show()
            $(this).parent().next().next().children("#secondNum").show()
        } else {
            $(this).parent().next().next().children("#plus").hide()
            $(this).parent().next().next().children("#secondNum").hide()
        }
        resetPrice();
    }).delegate(".deleteGoodsItem", "click", function () {
        $(this).closest(".Js_outer").remove();
        resetPrice();
    }).delegate("[name=goodsItemNum]", "input propertychange", function () {
        resetPrice();
    });

    /**
     * 第一次加载的时候查看是否选中
     */
    $(function(){
        if($("#isLimt").is(":checked")){
            $("#provinces").removeClass("hidden");
        }else{
            $("#provinces").addClass("hidden");
        }
    })
    /**
     * 点击区域限定选项
     */
    $("body").delegate("#isLimt","click",function(){
        if($("#isLimt").is(":checked")){
            $("#provinces").removeClass("hidden");
        }else{
            $("#provinces").addClass("hidden");
        }
    })

    $("#productCt").change(function(){
        if($(this).val()=="TimeSpan"){
            $(".Js_timespan").removeClass("hidden");
            $(".Js_Today").addClass("hidden");
            return;
        }else if($(this).val()=="Today"){
            $(".Js_Today").removeClass("hidden");
            $(".Js_timespan").addClass("hidden");
            return;

        }else{
            $(".Js_timespan").addClass("hidden");
            $(".Js_Today").addClass("hidden");
            return;
        }


    })
    $("#editForm").validate({
        rules: {
            name: {
                required: true
            },
            typeList: {
                required: true
            },
            goodsItemNum: {
                required: true,
                digits: true
            },
            discountPrices: {
                required: true,
                number: true
            },
            prices: {
                required: true,
                number: true
            },
            goodsNumber: {
                required: true,
                digits: true
            },
            time: {
                required: true
            },
            stock: {
                required: true,
                digits: true,
                number: true
            },
            goodsStatus: {
                required: true
            }

        },
        messages: {
            name: {
                required: "必填"
            },
            typeList: {
                required: "必须选择"
            },
            goodsItemNum: {
                required: "必填",
                digits: "必须输入整数"
            },
            discountPrices: {
                required: "必填",
                number: "必须是合法数字"
            },
            prices: {
                required: "必填",
                number: "必须是合法数字"
            },
            goodsNumber: {
                required: "必填",
                digits: "必须输入整数"
            },
            time: {
                required: "必填"
            },
            stock: {
                required: "必填",
                digits: "必须输入整数"
            },
            goodsStatus: {
                required: "必须选择"
            }
        },


        submitHandler: function (form) {
            //表单提交句柄,为一回调函数，带一个参数：form
            /*var content = $("iframe").contents().find("body").text();//获取iframe里面的body
             $("#hiddenCon").val(content);*/
            var vals=[];
            var goodsItemVal = [];
            var goodsItemsNums = [];
            var goodsItemsTypes = [];
            $(".chooseGoodsItem").each(function () {
                goodsItemVal.push($(this).find("option:selected").val());
                goodsItemsTypes.push($(this).find("option:selected").attr("data-value"));
            })

            $(".goodsItemNums").each(function () {
                goodsItemsNums.push($(this).val());
            })

            if($("#productCt").val()=="TimeSpan"){
                if($("#sctBeginDate").val()==""){
                    alert("请选择结束日期");
                    return false;
                }
                if($("#sctEndDate").val()==""){
                    alert("请选择结束日期");
                    return false;
                }
                if($("#sctBeginDate").val()>$("#sctEndDate").val()){
                    alert("结束日期不能小于起始日期");
                    return false;
                }


            }
            if($("#isLimt").is(":checked")){
                var limits=$(".check_reject_province:checked");
                if(limits.length==0){
                    alert("区域限购的商品，请选择限购区域");
                    return false;
                }else{
                    limits.each(function(){
                        vals.push($(this).val());
                    });
                }


            }
            $("#hidTxt").val(goodsItemVal);//id
            $("#hidNumber").val(goodsItemsNums);//数量
            $("#hidType").val(goodsItemsTypes);//type

            //判断商品项重复
            for (var i = 0; i < goodsItemVal.length; i++) {
                for (var j = goodsItemVal.length - 1; j > 0; j--) {
                    if (goodsItemVal[i] == goodsItemVal[j]) {

                        if (i == j) {
                            break;
                        }
                        alert("第" + [i + 1] + "个商品项和第" + [j + 1] + "个商品项重复，请重新选择！");
                        return;
                    }
                }
            }


            var _url = $("#addproduct").attr("data-url");
            var options = {
                url: _url + "?t=" + new Date().getMilliseconds()+"&limitAreas="+vals.toString(),
                type: 'post',
                data: $("#editForm").serializeObject(),
                success: function (data) {
                    if (data.isSuccess) {

                        alert("操作成功！");
                        window.location.href = 'sra_p_add_or_update_product?id=' + data.id;
                    }
                    else{
                        alert(data.msg);
                    }
                }
            };
            $.ajax(options);
        }

    })
    ;

    var html = "";

    function get_goodsSelector() {
        $.ajax({
            url: "ajax_get_goodsItemsInfo",
            type: 'post',
            async: false,
            success: function (data) {

                if (data) {
                    html = template.compile(tpl)(data)
                }
            }
        });
    }

    function resetPrice() {
        var countPrice = 0;
        $("[name=goodsItemNum]").each(function () {
            var _self = $(this);
            var number = _self.val();
            //var price = _self.parent().parent().find("[name=typeList] option:selected").data("price")
            var price = _self.closest(".Js_outer").find("[name=typeList] option:selected").data("price");
            countPrice += ~~number * (~~price);
        })
        $("#prices").val(countPrice)

    }

})
