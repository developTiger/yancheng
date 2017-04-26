define(function (require, exports, model) {

    require("jquery");
    exports.niceCheck = function (target) {
        $(target).iCheck({
            checkboxClass: 'icheckbox_minimal',
            radioClass: 'iradio_minimal',
            increaseArea: '10%' // optional
        });
    }


    /**
     * 联动check事件
     * @param target
     */
    exports.relCheck=function(target){
        var tar=$(target)
        $(target).on('ifClicked',".Js_relCheck", function(event){
            _setChildCheckBoxValDef($(this),target);
        });
    }
    /**
     * 遍历循环更新子节点checked属性
     * @param event
     * @private
     */
    var _setChildCheckBoxVal=function(event,target){
        var $nextCheckbox=event.closest("tr").next("tr").find(".Js_relCheck");
        //var $nextTr=event.closest("tr").next("tr");
        //if($nextTr.length>0&&$nextTr.attr("data-tt-parent-id")==event.closest("tr").attr("data-tt-id")){
        //    $nextCheckbox.iCheck($(event).prop("checked")==true?"check":"uncheck")
        //    // $nextCheckbox.prop("checked",$(event).prop("checked"));
        //    arguments.callee($nextCheckbox);
        //}
        var ar=arguments;
        $(target+" tr[data-tt-parent-id="+event.closest("tr").attr("data-tt-id")+"]").each(function(){
            $(this).find(".Js_relCheck").iCheck($(event).find(".Js_relCheck").prop("checked")==true?"check":"uncheck")
            // $nextCheckbox.prop("checked",$(event).prop("checked"));
            ar.callee($(this),target);
        })
    }

    var _setChildCheckBoxValDef=function(event,target){
        $(target+" tr[data-tt-parent-id="+event.closest("tr").attr("data-tt-id")+"]").each(function(){
                $(this).find(".Js_relCheck").iCheck($(event).prop("checked")==true?"uncheck":"check")
                // $nextCheckbox.prop("checked",$(event).prop("checked"));
                _setChildCheckBoxVal($(this),target);
        })

    }
    /**
     * 全选。取消 <input type="checkbox" />
     * @param context : checkbox所在上下文
     * @param checkAllBtn : 全选按钮
     * @param checkBtn ： checkbox按钮
     */
    exports.checkAll = function (context, checkAllBtn, checkBtn) {
        $(context).delegate(checkAllBtn,"ifChecked",function(event){
            $("input").iCheck('check');
        }) ;
        $(context).delegate(checkAllBtn,"ifUnchecked",function(event){
            $("input").iCheck('uncheck');
        });
    }
    //    var flag = false;
    //    $(context).delegate(checkAllBtn, "click", function () {
    //        flag = !$(this).is(":checked");
    //        if (!flag) {
    //            //未选中 - 选中
    //            $(checkBtn + ":not(:checked)").prop("checked", "true");
    //            flag = true;
    //        } else {
    //            //选中 - 取消
    //            $(checkBtn + ":checked").prop("checked", "");
    //            flag = false;
    //        }
    //    })
    //}
});

