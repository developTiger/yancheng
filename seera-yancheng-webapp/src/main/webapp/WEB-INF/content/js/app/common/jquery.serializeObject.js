/**
 * Created by liuxiang on 2015/9/7.
 */
define(function(require,exports,module){
    require("jquery");
    $.fn.serializeObject = function() {
        var o = {};
        var a = this.serializeArray();

        $.each(a, function() {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };
    module.exports = $;
})
