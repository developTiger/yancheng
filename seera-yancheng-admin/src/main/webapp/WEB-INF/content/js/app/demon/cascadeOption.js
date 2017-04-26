/**
 * Created by liulin on 2016/7/6.
 */

define(function(require,exports,module){



    /*$('#myModal_1').on('hide.bs.modal', function () {

    });*/
    $(function(){
        $('[data-type="checkBox"]').click(function(){
            //checkBox点击事件，两种可能：1.全部选中，全选；2.有一个没选，不为全选
            if(!$(this).prop("checked") && $('[data-type="checkAll"]').prop("checked")){
                $('[data-type="checkAll"]').prop("checked",false);
            }
            var checkedNum = 0;
            $('[data-type="checkBox"]').each(function(i){
                if($('[data-type="checkBox"]:eq('+i+')').prop("checked"))
                    checkedNum++;
            });
            //全部选中即为全选
            if($('[data-type="checkBox"]').length == checkedNum){
                $('[data-type="checkAll"]').prop("checked",true);
            }


            var data_value = $(this).attr('data-value'),
                text_value = $.trim($(".txt").val()),
                id_value = $(this).val();

            if($(this).prop("checked")){
                if(text_value.length > 0){
                    if(text_value.indexOf(data_value+",") != -1){
                        return;
                    }else{
                        text_value += data_value+',';
                    }
                }else{
                    text_value = data_value + ',';
                }
            }else{
                if(text_value.indexOf(data_value+',') != -1){
                    text_value = text_value.replace(data_value+',','');
                }
            }
            $(".txt").val(text_value);

            /*var data_value = $(this).attr('data-value'),
                txtalso = $.trim($(".txtValue").val()),
                id_value = $(this).val()
            ids = $.trim($(".txtValue").attr("data-value"));
            //alert(ids);
            //alert(txtalso);
            if($(this).prop("checked")) {
                if(txtalso.length > 0) {
                    if(txtalso.indexOf(data_value+',') != -1) {
                        return ;
                    } else {
                        txtalso += data_value + ',';
                    }
                } else {
                    txtalso = data_value+',';
                }
                if(ids.length > 0) {
                    if(ids.indexOf(id_value+',') != -1) {
                        return ;
                    } else {
                        ids += id_value + ',';
                    }
                } else {
                    ids = id_value + ',';
                }
            } else {
                if(txtalso.indexOf(data_value+',') != -1) {
                    txtalso = txtalso.replace(data_value+',', '');
                }
                if(ids.indexOf(id_value+',') != -1) {
                    ids = ids.replace(id_value+',', '');
                }
            }
            $(".txtValue").val(txtalso);
            $(".txtValue").att  r("data-value",ids);*/
            //alert(ids);
        });




        //checkAll点击事件，点击全选，全部选中
        $('[data-type="checkAll"]').click(function(){
            var str = '';

            if($(this).prop("checked")) {
                $.each($('[data-type="checkbox"]'), function(i){
                    str += $(this).attr('data-value') + ',';
                });
                    $('[data-type="checkBox"]').prop('checked', true);
            } else {
                $('[data-type="checkBox"]').prop('checked', false);
            }

            //$(".txtValue").val(str);
            //$(".txtValue").attr("data-value",ids);
            $(".txt").val(str);
        });


    });

})
