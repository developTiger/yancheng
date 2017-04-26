define(function(require,exports,module){

    require("ajaxUpload");
    var wDatePicker = require("wdatePicker");//时间插件
    //widget插件作用：点击按钮，弹出模态框，对按钮进行初始化
    var widget = require("../common/widget");
    var validate = require("validate");//对表单进行验证
    require("../common/jquery.serializeObject");//对提交的数据 进行序列化 返回一个object
    var tpl = require("text!app/tpl/goodsManager/addgoodsItemNameAndNumTpl.html");
    var tpl_img = require("text!app/tpl/goodsManager/addgoodsPicturesTpl.html");
    require("../common/templeteHelper");


    //时间插件
    $("body").delegate(".Wdate", "click", function () {
        wDatePicker({ dateFmt: 'yyyy-MM-dd' });
    });



    var id=3;
    $("body").delegate(".addGoodsImg","click",function(){
        var imgData = {num:id};
        var html = template.compile(tpl_img)(imgData);
        $("#addGoodsImgs").append(html);
        id++;
    })



    $("body").delegate(".deleteGoodsImg","click",function(){
        $(this).parent().parent().remove();
    })


    require("kindeditor");
    KindEditor.create('textarea[name="profile"]',{
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

    require("kindeditor");
    KindEditor.create('textarea[name="notice"]',{
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

    require("kindeditor");
    KindEditor.create('textarea[name="trafficGuide"]',{
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

    $("body").delegate(".chooseGoodsItem","change",function(){

            if($(this).val() == "2" || $(this).val() == "3"){
                $(this).parent().next().next().children("#plus").show()
                $(this).parent().next().next().children("#secondNum").show()
            }else{
                $(this).parent().next().next().children("#plus").hide()
                $(this).parent().next().next().children("#secondNum").hide()
            }

    })




    $("#addGoodsForm").validate({
        rules: {
            name:{
                required:true
            },
            typeList:{
                required:true
            },
            goodsItemNum:{
                required:true,
                digits:true
            },
            discountPrices:{
                required:true,
                number:true
            },
            prices:{
                required:true,
                number:true
            },
            goodsNumber:{
                required:true,
                digits:true
            },
            time:{
                required:true
            },
            stock:{
                required:true,
                digits:true
            },
            goodsStatus:{
                required:true
            }

        },
        messages: {
            name:{
                required:"必填"
            },
            typeList:{
                required:"必须选择"
            },
            goodsItemNum:{
                required:"必填",
                digits:"必须输入整数"
            },
            discountPrices:{
                required:"必填",
                number:"必须是合法数字"
            },
            prices:{
                required:"必填",
                number:"必须是合法数字"
            },
            goodsNumber:{
                required:"必填",
                digits:"必须输入整数"
            },
            time:{
                required:"必填"
            },
            stock:{
                required:"必填",
                digits:"必须输入整数"
            },
            goodsStatus:{
                required:"必须选择"
            }
        },


        submitHandler: function (form) {
            //表单提交句柄,为一回调函数，带一个参数：form

            var profile = $("#profile").val();
            var notice = $("#notice").val();
            var trafficGuide = $("#trafficGuide").val();
            $("#hidPro").val(profile);
            $("#hidNot").val(notice);
            $("#hidTra").val(trafficGuide);

            var goodsItemVal =[];
            var goodsItemsNums = [];
            var goodsItemsTypes = [];
            $(".chooseGoodsItem").each(function(){
                goodsItemVal.push($(this).find("option:selected").val());
                goodsItemsTypes.push($(this).find("option:selected").attr("data-value"));
            })

            $(".goodsItemNums").each(function(){
                goodsItemsNums.push($(this).val());
            })

            $("#hidTxt").val(goodsItemVal);//id
            $("#hidNumber").val(goodsItemsNums);//数量
            $("#hidType").val(goodsItemsTypes);//type

            //判断商品项重复
            for(var i=0;i<goodsItemVal.length;i++){
                for(var j=goodsItemVal.length-1;j>0;j--){
                    if(goodsItemVal[i] == goodsItemVal[j] ){

                        if(i == j){
                            break ;
                        }
                        alert("第"+[i+1]+"个商品项和第"+[j+1]+"个商品项重复，请重新选择！");
                        return;
                    }
                }
            }



            var _url = $("#addOrUpdateGoods").attr("data-url");
            var options = {
                url: _url + "?t=" + new Date().getMilliseconds(),
                type: 'post',
                data: $("#addGoodsForm").serializeObject(),
                success: function (data) {
                    if (data.isSuccess)
                        alert("新增成功");
                }
            };
            $.ajax(options);
        }

    });

    var html="";
    $.ajax({
        url:"ajax_get_goodsItemsInfo",
        type:'post',
        success:function(data){
            if(data){
                html = template.compile(tpl)(data)
            }
        }
    });

    $("body").delegate(".addGoodsItem","click",function(){
        $("#addgoodsItemNameAndNum").append(html);
    })

    $("body").delegate(".deleteGoodsItem","click",function(){
        $(this).parent().parent().parent().remove();
    })




})
