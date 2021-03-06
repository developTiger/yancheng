define(function (require, exports, model) {
    require("template");
    template.helper('dateFormat', function (date, format) {
        if(!date)return;
        if (!format) {
            format = "yyyy-MM-dd hh:mm:ss";
        }
        date = new Date(date);
        var map = {
            "M": date.getMonth() + 1, //月份
            "d": date.getDate(), //日
            "h": date.getHours(), //小时
            "m": date.getMinutes(), //分
            "s": date.getSeconds(), //秒
            "q": Math.floor((date.getMonth() + 3) / 3), //季度
            "S": date.getMilliseconds() //毫秒
        };

        format = format.replace(/([yMdhmsqS])+/g, function (all, t) {
            var v = map[t];
            if (v !== undefined) {
                if (all.length > 1) {
                    v = '0' + v;
                    v = v.substr(v.length - 2);
                }
                return v;
            }
            else if (t === 'y') {
                return (date.getFullYear() + '').substr(4 - all.length);
            }
            return all;
        });
        return format;
    });


    var data = {
        time: (new Date).toString()
    };

    template.helper('OrderStatus', function (data) {
        if(data==null ||data=="")return;
        var map = {
            waitPay:"待付款",
            payCheck:"付款待确认",
            payed:"已付款",
            expired:"已超时",
            canceled:"已取消",
            waitComment:"等待完成待评价",
            end:"已完成已评价"
        };
        return map[data];
    });


});

