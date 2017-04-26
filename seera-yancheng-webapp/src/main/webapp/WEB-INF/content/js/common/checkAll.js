/**
 * Created by 中浩 on 2016/8/18.
 */
define(function (require, exports, module) {
  function checkAll(table,btn,callback){

      this.table=table;
      this.btn=btn;
      this.check(callback);
  }
    checkAll.prototype.check=function(callback){
        var self=this;

        $(self.btn).click(function(){
            $(self.table).find("input[type=checkbox]").prop("checked",$(this).prop("checked"));
            callback&&typeof callback=="function"&&callback();
        })

    }
    module.exports=checkAll;
})