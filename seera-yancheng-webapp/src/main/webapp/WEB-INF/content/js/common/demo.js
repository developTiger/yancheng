/**
 * Created by zwork on 2016/8/20.
 */
/**
 * Created by liuxiang on 2016/3/4.
 */
define(function (require, exports, module) {

    var Dialog = require("../common/dialog"),
        Tab = require("../common/tab"),
        tpl = require("../template/apply/feeItem.html"),
        template = require("template");
    require("../common/freeTalk");
    var toMath=require("./common/toMath");

    var Upload = require("./file/upload")
    var filePopup = require("./file/popup");
    var fileConfig = require("./file/config");
    var Select = require("./common/select");
    var TypeEnum = require("./common/typeEnums");



    var apply_index = {

        //region 初始化
        init: function () {
            this.render();
            this.bind();
            this.checkGoods();
            this.tip(".fj-btn");
            this.getFee(this.getPt());
            this.getSelectedAddedService();
            this.upload();
            this.technologyDomain();
        },
        render: function () {
            this.startUpBtnSelector = ".startUpBox-btn a";
            //专利类型
            this.patentType = "fm";
            //添加购物车
            this.addShoppingCartSelector = ".po-btn-addCat";

            this.startUpBoxSelector = ".po-l-f-startUpBox";

            this.patentTypeTabUlSelector = "#goods-patentType",
                //专利类型
                this.patentTypeTabSelector = "#goods-patentType li";
            //增值服务
            this.serviceTabSelector = "#product-services li";
            //费用减缓
            this.coseTabSelector = "#cost li";
            //费用明细
            this.detailsChargesSelector = "#detailsCharges";
            //服务费单项费用
            this.serviceChargeItemSelector = ".serviceChargeItem";
            //服务费总费用
            this.serviceChargeTotalSelector = "#serviceChargeTotal";
            //国知局总费用
            this.officialChargesTotalSelector = "#officialChargesTotal";
            //总费用
            this.totalFeeSelector = "#totalFee";
            //官费自缴
            this.officialChargesWay = "#gfzj";
            this.officialChargesWayValue = false;
            //商品ID
            this.goodsId = "";
            //默认产品ID
            this.productId = "";
            //加强保护Id
            this.strengthenProtectionProductId = "";
            //授权担保ID
            this.nonAuthorizedRefundProductId = "";

            this.gw_count = 0;

            //增值服务费用
            this.incrementService2 = {
                //加强保护
                strengthenProtection: 0,
                //授权担保
                nonAuthorizedRefund: 0
            }
        },
        //endregion

        //region 事件
        bind: function () {
            var that = this;
            $('body').delegate(that.startUpBtnSelector, "click", function () {
                var $this = $(this);
                var isShow = !$(that.startUpBoxSelector).hasClass("dn");
                if (isShow) {
                    $(that.startUpBoxSelector).addClass("dn");
                    $this.find("em").html("&#xe609;");
                    $this.find("span").html("展开更多");
                } else {
                    $(that.startUpBoxSelector).removeClass("dn");
                    $this.find("em").html("&#xe608;");
                    $this.find("span").html("收起更多");
                }
            });

            //专利类型
            $('body').delegate(that.patentTypeTabSelector, 'click', function () {
                var $this = $(this),
                    pt = $this.attr("data-pt");
                var type = that.officialChargesWayValue ? "_zj" : "_dj";
                pt += type;
                $this.addClass("selected").siblings().removeClass("selected");
                if (pt.split("_")[0] == 'wg') {
                    //$(that.serviceTabSelector).attr("data-readonly", "true");
                    $(that.serviceTabSelector).eq(1).addClass("selected");
                    $(that.serviceTabSelector).eq(0).removeClass("selected").hide();
                } else {
                    //$(that.serviceTabSelector).attr("data-readonly", "false");
                    $(that.serviceTabSelector).eq(0).addClass("selected").show();
                    $(that.serviceTabSelector).eq(1).removeClass("selected")
                }
                that.getFee(pt);
                that.patentType = pt;
            });

            //增值服务
            $('body').delegate(that.serviceTabSelector, "click", function () {
                var $this = $(this),
                    isReadonly = $this.attr("data-readonly");
                if (isReadonly == "true") {
                    return false;
                }
                if ($this.hasClass("selected")) {
                    $this.removeClass("selected");
                } else {
                    $this.addClass("selected");
                }
                that.refreshFee(that.getPt());
            });

            //费用减缓
            $('body').delegate(that.coseTabSelector, 'click', function () {
                var $this = $(this);
                $this.addClass("selected").siblings().removeClass("selected");
                that.getFee(that.getPt());
            });

            //下单页底部选项卡
            $("body").delegate("#pa-p-tab li", "click", function () {
                var $this = $(this);
                $this.addClass("selected").siblings().removeClass("selected");
                var dataType = $this.attr("data-type");
                $("body").animate({scrollTop: $("." + dataType).position().top - 100}, 400);
            });

            //官费自缴
            $("body").delegate(that.officialChargesWay, "click", function () {
                that.officialChargesWayValue = $(this).is(":checked");
                that.getFee(that.getPt());
            });

            //下单页底部选项卡中申请费用切换
            Tab("#a-cost-tab li", ".a-cost-bg li");

            //立即申请按钮触发事件
            $('body').delegate(".po-btn-app", "click", function () {
                that.apply();
            });

            //购物车
            $("body").delegate(that.addShoppingCartSelector, "click", function () {
                that.addShoppingCart();
            }).delegate(".po-r-changeAdviser", "click", function () {
                that.gw_count++;
                $(".po-r-a-box>li").eq(that.gw_count).show().siblings().hide();
                if (that.gw_count == 2) {
                    that.gw_count = -1;
                }
            });
        },
        //endregion

        checkGoods: function () {
            var pt = $(this.patentTypeTabUlSelector).attr("data-type");
            $(this.patentTypeTabSelector).eq(pt / 1 - 1).click();
        },

        /**
         * 获取选中状态下的增值服务
         */
        getSelectedAddedService: function () {
            var addedService = 0;
            $(this.serviceTabSelector + ".selected").each(function () {
                addedService += $(this).attr("data-val") / 1;
            });
            return addedService;
        },

        //region 专利申请
        apply: function () {
            //改正jdFile undefined错误
            var $jdFile = $("#Js_upload_hiddens1");
            //专利名称
            var pn = $("#patentName").val();
            //技术领域
            var tf = $("#technicalField").val();
            //技术交底文件
            var tFile = $jdFile.val()
            //商品
            var gid = this.goodsId;
            //产品
            var pids = this.getProductIds();
            if (gid == "" || pids == "") {
                console.log("页面未加载完~");
                return false;
            }
            var fj = this.getCostMitigation();
            $.ajax({
                url: "/ajax_apply",
                type: "POST",
                data: {
                    pn: pn,
                    tf: tf,
                    tFile: tFile,
                    gid: gid,
                    pids: pids,
                    pt: this.patentType,
                    fj: fj
                },
                dataType: "text",
                success: function (data) {
                    if (data == "true") {
                        window.location.href = "/apply/order_confirm.html";
                    }
                },
                error: function (xhr) {
                    if (xhr.status == 401) {
                        window.location.href = window.CONFIG.LOGIN_BASEURL + "?service=" + window.CONFIG.APPLY_BASEURL + "/wxoauth/cas";
                    }
                }
            });
        },
        //endregion

        //region 获取专利类型
        getPt: function () {
            var type = this.officialChargesWayValue ? "_zj" : "_dj";
            return $(this.patentTypeTabSelector + ".selected").attr("data-pt") + type;
        },
        //endregion

        //region 获取费用减缓
        getCostMitigation: function () {
            return $(this.coseTabSelector + ".selected").attr("data-val");
        },
        // endregion

        //region 获取费用明细
        /**
         * 获取费用明细
         * @param pt 专利类型
         */
        getFee: function (pt) {
            var that = this,
                data,
                costMitigation = that.getCostMitigation();
            data = that.getGoodsListInfo(pt);
            data.costMitigation = costMitigation;
            data.pt = pt.split("_")[0];
            data.officialChargesWayValue = that.officialChargesWayValue;
            var html = template.compile(tpl)(data);
            $(that.detailsChargesSelector).html(html);
            that.refreshFee(pt);
        },
        //endregion

        //region 刷新费用
        /**
         * 刷新费用
         * @param pt 专利类型
         */
        refreshFee: function (pt) {
            var that = this,
                strengthenProtectionFee = 0,
                nonAuthorizedRefundFee = 0,
                strengthenProtectionSelector = "#strengthenProtection",
                nonAuthorizedRefundSelector = "#nonAuthorizedRefund";

            //判断是否需要加强保护
            if ($(strengthenProtectionSelector).hasClass("selected")) {
                strengthenProtectionFee = that.getIncrementServiceFee(pt, "strengthenProtection");
                $(strengthenProtectionSelector + "Fee").show().find("span.fee").html(toMath.getNormalNumber(strengthenProtectionFee));
                $(strengthenProtectionSelector + "Fee").show().find("span.fee").attr("data-val",strengthenProtectionFee);
            } else {
                $(strengthenProtectionSelector + "Fee").hide();
            }
            //判断是否需要授权担保
            if ($(nonAuthorizedRefundSelector).hasClass("selected")) {
                nonAuthorizedRefundFee = that.getIncrementServiceFee(pt, "nonAuthorizedRefund");
                $(nonAuthorizedRefundSelector + "Fee").show().find("span.fee").html(toMath.getNormalNumber(nonAuthorizedRefundFee));
                $(nonAuthorizedRefundSelector + "Fee").show().find("span.fee").attr("data-val",nonAuthorizedRefundFee);
            } else {
                $(nonAuthorizedRefundSelector + "Fee").hide();
            }
            if (strengthenProtectionFee != 0 && nonAuthorizedRefundFee != 0) {
                $(strengthenProtectionSelector + "Dot").show();
                $(nonAuthorizedRefundSelector + "Dot").show();
            } else if (strengthenProtectionFee != 0 || nonAuthorizedRefundFee != 0) {
                $(strengthenProtectionSelector + "Dot").show();
                $(nonAuthorizedRefundSelector + "Dot").hide();
            } else {
                $(strengthenProtectionSelector + "Dot").hide();
                $(nonAuthorizedRefundSelector + "Dot").hide();
            }
            var serviceChargeItemTotal = 0;
            $(that.serviceChargeItemSelector).each(function () {
                serviceChargeItemTotal = +$.trim($(this).attr("data-val")) / 1;//edit by wanggy
            });
            var serviceChargeTotal = strengthenProtectionFee / 1 + nonAuthorizedRefundFee / 1 + serviceChargeItemTotal;
            //edit by wanggy
            $(that.serviceChargeTotalSelector).attr("data-val",serviceChargeTotal);
            $(that.serviceChargeTotalSelector).html(toMath.getNormalNumber(serviceChargeTotal));
            var officialChargesTotal = $.trim($(that.officialChargesTotalSelector).attr("data-val"));

            var total = serviceChargeTotal / 1 + officialChargesTotal / 1;
            //$(that.totalFeeSelector).html(total);

            if (that.officialChargesWayValue) {
                $(that.officialChargesWay + "-element").addClass("deline");
                $(that.totalFeeSelector).html(toMath.getNormalNumber(serviceChargeTotal));
            } else {
                $(that.officialChargesWay + "-element").removeClass("deline");
                $(that.totalFeeSelector).html(toMath.getNormalNumber(total));
            }
        },
        //endregion

        //region 获取增值服务费
        /**
         * 获取增值服务费
         * @param pt 专利类型
         * @param type 增值服务费类型 strengthenProtection：加强保护，nonAuthorizedRefund：授权担保
         */
        getIncrementServiceFee: function (pt, type) {
            var that = this,
                fee;
            fee = type == "strengthenProtection" ? that.incrementService2.strengthenProtection : that.incrementService2.nonAuthorizedRefund;
            return fee;
        },
        //endregion

        //region 各种弹出框
        /**
         * 弹出帮助提示
         * @param selector 触发按钮选择器
         */
        tip: function (selector) {
            Dialog.open(selector);
        },
        //endregion

        /**
         * 获取商品费用信息
         * @param pt
         * @returns {{}}
         */
        getGoodsListInfo: function (pt) {
            var charge = {}, officialCharges = [], serviceCharge = [], fee = {};
            var goodsListInfo = JSON.parse($.trim($("#goodsListInfo").html()));
            var goodsInfoName, productInfoName, sqdbInfoName, jqbhInfoName, gfInfoName;
            if (pt == "fm_dj") {
                goodsInfoName = "DJFMZL";
                productInfoName = "FMZLSQDJGF";
                sqdbInfoName = "BSQTKFMZL";
                jqbhInfoName = "JQBHFMZL";
                gfInfoName = "FMZLGFDJ";
            } else if (pt == "sy_dj") {
                goodsInfoName = "DJSYXX";
                productInfoName = "SYXXSQDJGF";
                sqdbInfoName = "BSQTKSYXX";
                jqbhInfoName = "JQBHSYXX";
                gfInfoName = "SYXXGFDJ";
            } else if (pt == "wg_dj") {
                goodsInfoName = "DJWGSJ";
                productInfoName = "WGSJSQDJGF";
                sqdbInfoName = "BSQTKWGSJ";
                gfInfoName = "WGSJGFDJ";
            } else if (pt == "tr_dj") {
                goodsInfoName = "DJTRSQ";
                productInfoName = "TRSQSQDJGF";
                sqdbInfoName = "BSQTKTRSQ";
                jqbhInfoName = "JQBHTRSQ";
                gfInfoName = "FMZLGFDJ,SYXXGFDJ";
            } else if (pt == "fm_zj") {
                goodsInfoName = "ZJFMZL";
                productInfoName = "FMZLSQZJGF";
                sqdbInfoName = "BSQTKFMZL";
                jqbhInfoName = "JQBHFMZL";
                gfInfoName = "FMZLGFDJ";
            } else if (pt == "sy_zj") {
                goodsInfoName = "ZJSYXX";
                productInfoName = "SYXXSQZJGF";
                sqdbInfoName = "BSQTKSYXX";
                jqbhInfoName = "JQBHSYXX";
                gfInfoName = "SYXXGFDJ";
            } else if (pt == "wg_zj") {
                goodsInfoName = "ZJWGSJ";
                productInfoName = "WGSJSQZJGF";
                sqdbInfoName = "BSQTKWGSJ";
                gfInfoName = "WGSJGFDJ";
            } else if (pt == "tr_zj") {
                goodsInfoName = "ZJTRSQ";
                productInfoName = "GRSQSQZJGF";
                sqdbInfoName = "BSQTKTRSQ";
                jqbhInfoName = "JQBHTRSQ";
                gfInfoName = "FMZLGFDJ,SYXXGFDJ";
            }
            //console.log(goodsListInfo);
            var goodsInfo = this.getJSONObjectFromJSONArray(goodsListInfo, "codeName", goodsInfoName);
            //console.log(goodsInfo);
            this.goodsId = goodsInfo.id;
            // console.log(this.goodsId);
            $.post("/apply/order_getFullCutInfoByUser", {goodsId: this.goodsId}, function (data) {
                if (data == "") {
                    $(".po-l-t-mj").hide();
                } else {
                    $("#Js_mj").html(data);
                    $(".po-l-t-mj").show();
                }
            });
            var goodsPriceInfo = JSON.parse(goodsInfo.goodsPriceInfo);

            //服务费
            //标准服务费
            var productInfo_bz = this.getJSONObjectFromJSONArray(goodsPriceInfo, "codeName", productInfoName);
            fee.name = productInfo_bz.priceSchemeList[0].priceList[0].feeName;
            fee.value = productInfo_bz.priceSchemeList[0].priceList[0].price;
            fee.codeName = "fwf";
            serviceCharge.push(fee);
            this.productId = productInfo_bz.id;

            //授权担保
            var productInfo_sqdb = this.getJSONObjectFromJSONArray(goodsPriceInfo, "codeName", sqdbInfoName);
            this.incrementService2.nonAuthorizedRefund = productInfo_sqdb.priceSchemeList[0].priceList[0].price;
            //console.log(productInfo_sqdb.id);
            this.nonAuthorizedRefundProductId = productInfo_sqdb.id;

            if (pt != "wg_dj" && pt != "wg_zj") {
                //加强保护
                var productInfo_jqbh = this.getJSONObjectFromJSONArray(goodsPriceInfo, "codeName", jqbhInfoName);
                this.incrementService2.strengthenProtection = productInfo_jqbh.priceSchemeList[0].priceList[0].price;
                this.strengthenProtectionProductId = productInfo_jqbh.id;
            }
            //官费
            // console.log(productInfo_bz);
            var gfInfoNameArray = gfInfoName.split(",");
            if (gfInfoNameArray.length > 1) {
                var skuList = this.getJSONObjectFromJSONArray(productInfo_bz.skuList, "codeName", gfInfoNameArray[0]);
                if (skuList != null) {
                    fee = this.getOfficialChargesFeeItem(skuList.skuPrice, "申请费", "sqf");
                    fee.name = "发明申请费";
                    if (fee != null) {
                        officialCharges.push(fee);
                    }

                    fee = this.getOfficialChargesFeeItem(skuList.skuPrice, "文件印刷费", "wyf");
                    if (fee != null) {
                        officialCharges.push(fee);
                    }

                    fee = this.getOfficialChargesFeeItem(skuList.skuPrice, "审查费", "scf");
                    if (fee != null) {
                        officialCharges.push(fee);
                    }
                } else {//特殊情况，自缴没有官费数据，所以在JS里写死
                    //发明自缴
                    fee = {};
                    fee.codeName = "sqf";
                    fee.name = "发明申请费";
                    fee.value = 900;
                    officialCharges.push(fee);
                    fee = {};
                    fee.codeName = "wyf";
                    fee.name = "文件印刷费";
                    fee.value = 50;
                    officialCharges.push(fee);
                    fee = {};
                    fee.codeName = "scf";
                    fee.name = "审查费";
                    fee.value = 2500;
                    officialCharges.push(fee);
                }

                var skuList = this.getJSONObjectFromJSONArray(productInfo_bz.skuList, "codeName", gfInfoNameArray[1]);
                if (skuList != null) {
                    fee = this.getOfficialChargesFeeItem(skuList.skuPrice, "申请费", "sqf");
                    fee.name = "实用新型申请费";
                    if (fee != null) {
                        officialCharges.push(fee);
                    }

                    fee = this.getOfficialChargesFeeItem(skuList.skuPrice, "文件印刷费", "wyf");
                    if (fee != null) {
                        officialCharges.push(fee);
                    }

                    fee = this.getOfficialChargesFeeItem(skuList.skuPrice, "审查费", "scf");
                    if (fee != null) {
                        officialCharges.push(fee);
                    }
                } else {//特殊情况，自缴没有官费数据，所以在JS里写死
                    //实用新型自缴
                    fee = {};
                    fee.codeName = "sqf";
                    fee.name = "实用新型申请费";
                    fee.value = 500;
                    officialCharges.push(fee);
                }

            } else {
                var skuList = this.getJSONObjectFromJSONArray(productInfo_bz.skuList, "codeName", gfInfoName);
                if (skuList != null) {
                    fee = this.getOfficialChargesFeeItem(skuList.skuPrice, "申请费", "sqf");
                    if (fee != null) {
                        officialCharges.push(fee);
                    }

                    fee = this.getOfficialChargesFeeItem(skuList.skuPrice, "文件印刷费", "wyf");
                    if (fee != null) {
                        officialCharges.push(fee);
                    }

                    fee = this.getOfficialChargesFeeItem(skuList.skuPrice, "审查费", "scf");
                    if (fee != null) {
                        officialCharges.push(fee);
                    }
                } else {
                    // console.log(productInfo_bz.codeName);
                    //特殊情况，自缴没有官费数据，所以在JS里写死
                    if (productInfo_bz.codeName == "FMZLSQZJGF") {
                        //发明自缴
                        fee = {};
                        fee.codeName = "sqf";
                        fee.name = "申请费";
                        fee.value = 900;
                        officialCharges.push(fee);
                        fee = {};
                        fee.codeName = "wyf";
                        fee.name = "文件印刷费";
                        fee.value = 50;
                        officialCharges.push(fee);
                        fee = {};
                        fee.codeName = "scf";
                        fee.name = "审查费";
                        fee.value = 2500;
                        officialCharges.push(fee);
                    } else if (productInfo_bz.codeName == "SYXXSQZJGF" || productInfo_bz.codeName == "WGSJSQZJGF") {
                        //实用新型自缴
                        fee = {};
                        fee.codeName = "sqf";
                        fee.name = "申请费";
                        fee.value = 500;
                        officialCharges.push(fee);
                    }
                }
            }

            charge.serviceCharge = serviceCharge;
            charge.officialCharges = officialCharges;
            return charge;
        },

        getOfficialChargesFeeItem: function (skuPrice, type, code) {
            var feeObj = this.getJSONObjectFromJSONArray(skuPrice, "second", type);
            if (feeObj == null) {
                return null;
            }
            var fee = {};
            fee.name = feeObj.second;
            fee.value = feeObj.third;
            fee.codeName = code;
            return fee;
        },

        /**
         * 产品ID
         * @returns {string}
         */
        getProductIds: function () {
            var pids = "";
            var addedService = this.getSelectedAddedService();
            if (addedService == 1) {
                pids = this.productId + "," + this.strengthenProtectionProductId;
            } else if (addedService == 2) {
                pids = this.productId + "," + this.nonAuthorizedRefundProductId;
            } else if (addedService == 3) {
                pids = this.productId + "," + this.strengthenProtectionProductId + "," + this.nonAuthorizedRefundProductId;
            } else {
                pids = this.productId;
            }
            // console.log(pids);
            return pids;
        },

        /**
         * 从JSONArray中获取指定JSONObject
         * @param jsonArray json数组
         * @param attribute 属性
         * @param value 属性值
         * @returns {*}
         */
        getJSONObjectFromJSONArray: function (jsonArray, attribute, value) {
            for (var i = 0; i < jsonArray.length; i++) {
                if (attribute == "name" && jsonArray[i].name == value) {
                    return jsonArray[i];
                } else if (attribute == "codeName" && jsonArray[i].codeName == value) {
                    return jsonArray[i];
                } else if (attribute == "second" && jsonArray[i].second == value) {
                    return jsonArray[i];
                }
            }
            return null;
        },

        /**
         * 添加购物车
         */
        addShoppingCart: function () {
            //改正jdFile undefined错误
            var $jdFile = $("#Js_upload_hiddens1");
            //专利名称
            var pn = $("#patentName").val();
            //技术领域
            var tf = $("#technicalField").val();
            //技术交底文件
            var tFile = $jdFile.val()
            //商品
            var gid = this.goodsId;
            //产品
            var pids = this.getProductIds();
            if (gid == "" || pids == "") {
                console.log("页面未加载完~");
                return false;
            }
            var fj = this.getCostMitigation();
            $.ajax({
                url: "/case_shoppingcart/add",
                type: "POST",
                data: {
                    pn: pn,
                    tf: tf,
                    tFile: tFile,
                    gid: gid,
                    pids: pids,
                    pt: this.patentType,
                    fj: fj
                },
                dataType: "text",
                success: function (data) {
                    if (data == "true") {
                        $(".scrollTop").click();
                        var shoppingCartCount = $.trim($("#Js_shoppingCart").html()) / 1;
                        shoppingCartCount++;
                        var scBtn_left = $("#Js_shoppingCart").offset().left;
                        var scBtn_top = $("#Js_shoppingCart").offset().top;
                        $(".shoppingCartAdd").css({"left": "50%", "top": "50%", "opacity": "1"}).fadeIn();
                        setTimeout(function () {
                            $(".shoppingCartAdd").animate({
                                "left": scBtn_left,
                                "top": scBtn_top,
                                "opacity": "0"
                            }, 500, function () {
                                $(".shoppingCartAdd").hide();
                                $("#Js_shoppingCart").html(shoppingCartCount);
                            });
                        }, 1000);
                    } else {
                        $(".shoppingCartMax").css({"left": "50%", "top": "50%", "opacity": "1"}).fadeIn();
                        setTimeout(function () {
                            $(".shoppingCartMax").animate({
                                "opacity": "0"
                            }, 500, function () {
                                $(".shoppingCartMax").hide();
                            });
                        }, 1000);
                    }
                },
                error: function (xhr) {
                    if (xhr.status == 401) {
                        window.location.href = window.CONFIG.LOGIN_BASEURL + "?service=" + window.CONFIG.APPLY_BASEURL + "/wxoauth/cas";
                    }
                }
            });
        },

        upload: function () {
            var jdUpload = new Upload({
                element: "#Js_perfectInfo_file_container",
                trigger: "#Js_complete_upload",
                preview: true,
                previewElement: "#Js_perfectInfo_file_container",
                data: {
                    'key': 'case',
                    c_name: "1",
                    file_name: ""
                },
                file_label:"JSJDS",
                done: function () {
                    var self = this;
                    var $jdFile = $("#Js_upload_hiddens1");
                    $jdFile.val(Upload.parseFile.call(self, jdUpload.cache))

                    new filePopup({
                        trigger: "#Js_perfectInfo_file_container .Js_patent_file_logo",
                        zIndex: 999,
                        role: "JSJDS",
                        events: {
                            "click [data-role=del]": function (event) {
                                var id = this.activeTrigger.data("id");
                                this.activeTrigger.parent().remove();
                                jdUpload.cache.splice(id, 1);
                                $jdFile.val(Upload.parseFile.call(self, jdUpload.cache))
                                this.hide();
                            }
                        }
                    })
                }
            })
        },

        technologyDomain: function () {
            var technologyDomain = $("#technicalField");
            new Select({
                trigger: "#Js_complete_technologyDomain",
                model: TypeEnum.TechnologyDomain,
                selected: "",
                zIndex: 1100,
                selected: technologyDomain.val()
            }).render().on("change", function (target) {
                    technologyDomain.val(target.data("value"));
                })
        }
    }

    module.exports = apply_index;
});