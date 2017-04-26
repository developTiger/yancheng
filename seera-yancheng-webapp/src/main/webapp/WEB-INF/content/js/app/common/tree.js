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
                    chkboxType: { "Y" : "", "N" : ""  }
                },
                data: {
                    simpleData: {
                        enable: true,
                        idKey: "id",
                        pIdKey: "parentId",
                        rootPId: 0
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