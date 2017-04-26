/**
 * Created by wangguangyang on 16/8/18.
 */
define(function(require, exports, module){
  var uri=require("uri");
    require("paging")
    function paging(config){
        this.config={
            trigger:'#paging',
            param:{
                items:100,
                itemsOnPage:10,
                cssStyle: 'light-theme',
                onPageClick:this.changePage,
                currentPage:uri.getParams(window.location.href,true).pageNumber||1
            }
        }
        var self=this;
        this.config=$.extend(true,{},this.config,config)
        $(this.config.trigger).pagination(self.config.param)
    }
    paging.prototype.changePage=function(pageNumber, event){
        var url=window.location.href;
        var param={pageNumber:pageNumber}
        window.location.href=uri.setParams(url,param)
    }
    module.exports=paging;


})