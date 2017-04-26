/**
 * Created by zwork on 2016/8/23.
 */
define(function(require,exports,module){
    var template = require("template");

    module.exports=function(tableid,tpl,data){
        var html = template.compile(tpl)(data);
        $(tableid).append(html);

    }
})