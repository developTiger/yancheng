/**
 * Created by liulin on 2016/7/16.
 */

define(function(require,exports,module){

    $("body").delegate(".immediate_authentication","click",function(){
        $(".real-name-hint").css({"display":"block"});
        $(".box-shadow").css({"display":"block"});
    })

    $("body").delegate(".cross","click",function(){
        $(".real-name-hint").css({"display":"none"});
        $(".box-shadow").css({"display":"none"});
    });

    $("body").delegate(".hint-immediately-verify","click",function(){
        $(".real-name-hint").css({"display":"none"});
        $(".box-shadow").css({"display":"none"});
    });

    $("body").delegate(".hint-canel","click",function(){
        $(".real-name-hint").css({"display":"none"});
        $(".box-shadow").css({"display":"none"});
    });
})
