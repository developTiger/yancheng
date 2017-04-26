/**
 * Created by wanggy on 2016/3/11.
 */
define(function(require,exports,module){

    exports.submitTreeVal=function(options){
        var postTreeAdd = $.fn.zTree.getZTreeObj(options.treeId);
        var nodesPost = postTreeAdd.getCheckedNodes(true);
        var attrPost=[];
        for(var i=0;i<nodesPost.length;i++)
        {
            attrPost.push(nodesPost[i].id);
        }
        $("#"+options.inputId).val(attrPost.join(","));

    }

    exports.submitTreeValWithOutHalf=function(options){
        var postTreeAdd = $.fn.zTree.getZTreeObj(options.treeId);
        var nodesPost = postTreeAdd.getCheckedNodes(true);
        for(var i=0;i< nodesPost.length;i++){
            if(nodesPost[i].getCheckStatus().half==true){
                nodesPost.splice($.inArray(nodesPost[i],nodesPost),1);
            }
        }
        var attrPost=[];
        for(var i=0;i<nodesPost.length;i++){
            attrPost.push(nodesPost[i].id);
        }
        $("#"+options.inputId).val(attrPost.join(","));

    }

    //exports.ajaxSubmit=function(form){
    //    var _data = form.serializeObject();
    //    $.post(form.attr('action'),_data,function(data){
    //        if(data.status==true){
    //            window.location.reload();
    //        }
    //    })
    //}
    //exports.del=function (e) {
    //    var id = ArrayExtend.getArrayData($("input[name='dataId']:checked"), "data-id").toString(),
    //        url = $(e.target).attr("data-url");
    //    var mes;
    //    var isRefresh=false;
    //    $.post(url + "?t=" + new Date().getMilliseconds(), {id: id}, function (data) {
    //
    //        if (data.result>0) {
    //            mes = "删除成功！";
    //            isRefresh = true;
    //        } else if (data.result < 0) {
    //            mes = data.message;
    //        }
    //        confirmBox.alert(mes, function () {
    //            if (isRefresh) {
    //                window.location.reload();
    //            }
    //        });
    //    })
    //}
    //exports.del=function (e) {
    //    var id = ArrayExtend.getArrayData($("input[name='dataId']:checked"), "data-id").toString(),
    //        url = $(e.target).attr("data-url");
    //
    //    $.post(url + "?t=" + new Date().getMilliseconds(), {id: id}, function (data) {
    //
    //        confirmBox.alert(data.message, function () {
    //            if (data.result) {
    //                window.location.reload();
    //            }
    //        });
    //    })
    //}
    //exports.delete=function(url,id){
    //    $.post(url + "?t=" + new Date().getMilliseconds(), {id: id}, function (data) {
    //        confirmBox.alert(data.message, function () {
    //            if (data.id>0) {
    //                window.location.reload();
    //            }
    //        });
    //    })
    //}

    /**
     * ajax提交
     * @param form
     */
    //exports.ajaxSubmit=function(form,callback){
    //    var _data = form.serialize();
    //    $.ajax({
    //        url:form.attr('action'),
    //        type:"post",
    //        //contentType:"application/json",
    //        data:_data
    //    }).done(function(data){
    //        //if(data.status == "200"){
    //        //window.location.reload();
    //        //}
    //        if( (typeof data)=="object"){
    //            if(data.id!=undefined){
    //                confirmBox.alert(data.message, function () {
    //                    if (data.id>0) {
    //                        window.location.reload();
    //                    }
    //                })
    //            }else{
    //                confirmBox.alert(data.errorMessage||"保存成功", function () {
    //                    if (data.returnValue>0) {
    //                        window.location.reload();
    //                    }
    //                })
    //            }
    //
    //        }
    //        else
    //        {
    //            window.location.reload();
    //        }
    //
    //    })
    //    callback&& callback();
    //
    //}
})