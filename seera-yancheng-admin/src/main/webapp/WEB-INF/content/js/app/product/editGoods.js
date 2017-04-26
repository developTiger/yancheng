define(function (require, exports, module) {

    require("ajaxUpload");
    var wDatePicker = require("wdatePicker");//时间插件
    //widget插件作用：点击按钮，弹出模态框，对按钮进行初始化
    var widget = require("../common/widget");
    var validate = require("validate");//对表单进行验证
    require("kindeditor");
    var pf = require("photoClip");
    require("hammer");
    require("iscrollzoom");
    require("lrzallbundle");
    require("../common/jquery.serializeObject");//对提交的数据 进行序列化 返回一个object
    var tpl = require("text!app/tpl/product/addProductItemNameAndNumTpl.html");
    var tpl_img = require("text!app/tpl/product/addProductPicturesTpl.html");
    require("../common/templeteHelper");

    var id = 3

    $("body").delegate(".Wdate", "click", function () {
        wDatePicker({dateFmt: 'yyyy-MM-dd'});
    }).delegate(".addGoodsItem", "click", function () {
        if(html==""){
            get_goodsSelector();
        }
        $("#addgoodsItemNameAndNum").append(html);

    }).delegate(".addGoodsImg", "click", function () {
        var imgData = {num: id};
        var html = template.compile(tpl_img)(imgData);
        $("#addGoodsImgs").append(html);
        id++;
    }).delegate(".deleteGoodsImg", "click", function () {
        $(this).parent().parent().remove();
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
        $(this).parent().parent().remove();
    }).delegate("[name=goodsItemNum]", "input propertychange", function () {
        resetPrice();
    });


    window.editorProfile = KindEditor.create('textarea[name="profile"]', {
        heigh: '250px',
        resizeType: 1,
        width: '100% !important',
        allowPreviewEmoticons: true,
        allowImageUpload: true,
        fontSizeTable: ['250px', '14px', '280px'],
        cssData: 'body{}',
        afterCreate: (function () {
            this.sync();
            this.html($("#hidPro").val());
        })
    });
    window.editorNotice = KindEditor.create('textarea[name="notice"]', {
        heigh: '250px',
        resizeType: 1,
        width: '100% !important',
        allowPreviewEmoticons: true,
        allowImageUpload: true,
        fontSizeTable: ['250px', '14px', '280px'],
        cssData: 'body{}',
        afterCreate: (function () {
            this.sync();
            this.html($("#hidNot").val());

        })
    });
    window.editorTrafficGuide = KindEditor.create('textarea[name="trafficGuide"]', {
        heigh: '250px',
        resizeType: 1,
        width: '100% !important',
        allowPreviewEmoticons: true,
        allowImageUpload: true,
        fontSizeTable: ['250px', '14px', '280px'],
        cssData: 'body{}',
        afterCreate: (function () {
            this.sync();
            this.html($("#hidTra").val());

        })
    });

    $("#editGoodsForm").validate({
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
            debugger;
            $("#hidPro").val(editorProfile.html());
            $("#hidNot").val(editorNotice.html());
            $("#hidTra").val(editorTrafficGuide.html());

            /*var content = $("iframe").contents().find("body").text();//获取iframe里面的body
             $("#hiddenCon").val(content);*/

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


            var _url = $("#editGoods").attr("data-url");
            var options = {
                url: _url + "?t=" + new Date().getMilliseconds(),
                type: 'post',
                data: $("#editGoodsForm").serializeObject(),
                success: function (data) {
                    if (data.isSuccess)
                        alert("修改成功");
                }
            };
            $.ajax(options);
        }

    });

    var html = "";
    function get_goodsSelector() {
        $.ajax({
            url: "ajax_get_goodsItemsInfo",
            type: 'post',
            async:false,
            success: function (data) {
                debugger;
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
            var price = _self.parent().parent().find("[name=typeList] option:selected").data("price")
            countPrice += ~~number * (~~price);
        })
        $("#prices").val(countPrice)

    }

    //$("#clipArea").photoClip({
    var clipArea = new bjj.PhotoClip("#clipArea",{
        size: [275, 165],
        outputSize: [275, 165],
        file: "#file",
        view: "#view",
        ok: "#clipBtn",
        loadStart: function() {
            console.log("照片读取中");
        },
        loadComplete: function() {
            console.log("照片读取完成");
        },
        clipFinish: function(dataURL) {
            debugger;
            var srcBase64 =  dataURL;
            console.log(dataURL);
        }
    });

})
