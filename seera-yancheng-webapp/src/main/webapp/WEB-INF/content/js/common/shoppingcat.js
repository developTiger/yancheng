define(function (require, exports, module) {
    $.fn.onlyNum = function () {
        $(this).keypress(function (event) {
            var eventObj = event || e;
            var keyCode = eventObj.keyCode || eventObj.which;
            if ((keyCode >= 48 && keyCode <= 57))
                return true;
            else
                return false;
        }).keyup(function (event) {
//            this.value=this.value.replace(/\D/g,'')
        }).bind("paste", function () {
//            //获取剪切板的内容
            return false;
        }).change(function(){
            var r = /^[0-9]*[1-9][0-9]*$/;
            if(!r.test($(this).val())){
                $(this).val(1)
                $(this).change();
            }
        });
    };

    function shoppingcat(config) {
        this.config = {
            reduceBtn: ".reduceBtn",
            addBtn: ".addBtn",
            txtNum: ".txtNum"
        }
        this.config = $.extend({}, this.config, config)
    }

    shoppingcat.prototype.init = function () {
        this.oNum();
        this.reduce();
        this.add();
    }
    shoppingcat.prototype.reduce = function () {
        var self = this;
        $("body").on("click", self.config.reduceBtn, function () {
            var txt = $(this).closest("tr").find(self.config.txtNum);
            if (txt.val() == 1) {
                txt.val(1);
                $(this).attr("disabled", "disabled");
            } else {
                txt.val(Number(txt.val()) - 1)
                if (txt.val() == 1) {
                    $(this).attr("disabled", "disabled");
                }
            }
            $(txt).change();
        })
    }
    shoppingcat.prototype.add = function () {
        var self = this;
        $("body").on("click", self.config.addBtn, function () {
            var txt = $(this).closest("tr").find(self.config.txtNum);
            var reduce = $(this).closest("tr").find(self.config.reduceBtn);
            txt.val(Number(txt.val()) + 1)
            if (reduce.attr("disabled") == "disabled") {
                $(reduce).attr("disabled",false);
            }
            $(txt).change();
        })
    }
    shoppingcat.prototype.oNum=function(){
        $(this.config.txtNum).onlyNum();
    }

    module.exports = shoppingcat;
})