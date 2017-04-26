/**
 * Created by wanggy on 2016/2/29.
 */
define(function(require,exports,module){
    var zTree=require("ztree");
    var zNodes;
    exports.init=function(options,callback){
        if(options.url){
            $.post(options.url,function(data){
                fuzhi(eval(data).items);
                if($("#menuIds").length>0) {
                    var ids = eval($("#menuIds").val());
                    var trees = $.fn.zTree.getZTreeObj("tree1");
                    //var ids = [your ids], i = 0, l = ids.length, node = null;
                    for (i = 0; i < ids.length; i++) {
                        trees.checkNode(trees.getNodeByParam("id", ids[i]), true);
                    }
                }
                callback && (typeof  callback == "function") &&callback()
            });

        }else{
            fuzhi(options.jsonData);
        }

        function fuzhi(data){
            var setting = {
                view: {
                    showIcon: true
                },
                check: {
                    enable: true,
                    chkboxType: { "Y": "ps", "N": "ps"  }
                },
                data: {
                    simpleData: {
                        enable: true,
                        idKey: "id",
                        pIdKey: "parentId",
                        rootPId: 0,
                        checked:true
                    }
                },
                callback: {
                    onClick: options.click && (typeof  options.click == "function") &&options.click,
                    onCheck: options.onCheck && (typeof  options.onCheck == "function") &&options.onCheck
                }
            };

            if(typeof(options.setting)!= "undefined")
            {
                setting=$.extend(true,{},setting,options.setting);
            }

            //if(typeof(options.setting)!= "undefined")
            //{
            //    setting.check=$.extend({},setting.check,options.setting.check);
            //}
            //if(typeof(options.setting.async)!= "undefined"){
            //    setting.async=$.extend({},setting.async,options.setting.async);
            //}
            zNodes=data;
            $.fn.zTree.init($(options.id), setting, zNodes);
        }
    }
    function zTreeOnClick(event, treeId, treeNode) {
        alert(treeNode.tId + ", " + treeNode.name);
    };
});