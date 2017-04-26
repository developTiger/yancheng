/**
 * Created by zwork on 2016/8/24.
 */
define(function (require, exports, module) {
    var _re_path = /^(http(s)?:\/\/)?[a-zA-Z0-9.]*((\/)?[^?#]*)/,
        _re_search = /\?([^#]*)(#.*)?/,
        _re_hostptc = /(https|http)\:\/\/((\w+|\.)+)/,
        _re_hostnoptc = /(\w+|\.)+/,
        _re_portnoptc = /^https|^http\:\/\/(\w+|\.)+(\:\d+)/,
        _re_portptc = /(\w+|\.)+(\:\d+)/;

    var Uri = function () {
    };

    module.exports = Uri;

    /**
     * 获取path
     * @param {string} url url路径
     * @example
     * var url  = 'http://www.zlbaba.com/user/getUser.json';
     * return /user/getUser.json
     */
    Uri.getPath = function (url) {
        if (_re_path.test(url)) {
            var path = /^(http(s)?:\/\/)?[a-zA-Z0-9.]*(:\d*)?((\/)?[^?#]*)/.exec(url)[4];
            if (path) {
                return path;
            }
            return "/";
        } else {
            return null;
        }
    };

    /**
     * 获取端口
     * @param
     * @example
     * var url  = 'http://www.zlbaba.com:8080';
     * @return {string}
     */
    Uri.getPort = function (url) {
        if (/\:(\d+)/.test(url)) {
            return /\:(\d+)/.exec(url)[1];
        }
        return "80";
    };

    /**
     * 获取host+port
     * @param url
     * @param nonedefaultport
     * @returns {string}
     */
    Uri.getHost = function (url, nonedefaultport) {
        var hostname = Uri.getHostName(url);
        var port = Uri.getPort(url);
        if (nonedefaultport && port == "80") {
            return hostname;
        } else {
            return hostname + ":" + port;
        }
    };
    /**
     * 获取hostName
     * @param {string}
     * @example
     * @return {string}
     */
    Uri.getHostName = function (url) {
        if (_re_hostptc.test(url)) {
            return _re_hostptc.exec(url)[2];
        }
        /* if (_re_hostnoptc.test(url)) {
         return _re_hostnoptc.exec(url)[0];
         }*/
        return "";
    };

    /**
     * 获取协议
     * @param {string} url
     * @param {boolean} [nonedefaultport]
     * @example
     * @return {string}
     */
    Uri.getProtocol = function (url) {
        var reg1 = /^http|^https/, reg2 = /^http\:|^https\:/;
        if (reg1.test(url)) {
            return reg2.exec(url)[0].replace(":", "");
        }
        return null;
    };
    /**
     * 获取参数
     * @param {string}
     * @param {boolean} [isobject]
     * @example
     * var url  = 'http://www.zlbaba.com?name=fackweb&live=hangzhou';
     * uri.getParams(url); // return 'name=fackweb&live=hangzhou'
     * uri.getParams(url); // return {name:'fackweb', live:'hangzhou'}
     * @return {string | Object}
     */
    Uri.getParams = function (url, isobject) {
        var result = {}, params = _re_search.exec(url);
        if (params && params.length && params.length >= 2) {
            params = params[1].split("&");
            for (var p; p = params.shift();) {
                if (p.split("=").length > 1) {
                    result[p.split("=")[0]] = decodeURIComponent(p.split("=")[1]);
                }
            }
            if (isobject) {
                return result;
            } else {
                return Uri.toQueryString(result);
            }
        }
        if (isobject) {
            return {};
        } else {
            return null;
        }
    };

    /**
     * 参数反序列化数组 方便dom操作
     * @param url
     * @returns {Array}
     */
    Uri.deserialize = function (url) {
        var result = [], params = _re_search.exec(url);
        if (params && params.length && params.length >= 2) {
            params = params[1].split("&");
            for (var p; p = params.shift();) {
                if (p.split("=").length > 1) {
                    var obj = {};
                    obj[p.split("=")[0]] = p.split("=")[1];
                    result.push(obj);
                }
            }


        }
        return result;
    }

    /**
     * 获取hash值
     * @param
     * @example
     *'http://www.zlbaba.com?name=fackweb&live=hangzhou#abcd';
     * uri.getHash(); // return 'abcd'
     * @return {string}
     */
    Uri.getHash = function (url) {
        var h = url || window.location.hash;
        if (h.charAt(0) == "#") {
            h = h.substring(1);
        } else if (h.lastIndexOf("#") > -1) {
            h = h.substring(h.lastIndexOf("#") + 1);
        }
        return $.browser.mozilla ? h : decodeURIComponent(h);
    };
    /**
     * 设置url参数
     * @param {string} url
     * @param {object} data
     * @example
     * var url = 'http://www.zlbaba.com?buyid=123&product_id=321';
     * var params = {'pname':'shoues','pice':334};
     * url = uri.setParams(url,params);
     * @return {string}
     */
    Uri.setParams = function (url, data) {
        var params = Uri.getParams(url, true), o, params_array = [];
        if (typeof data == "object") {
            for (o in data) {
                params[o] = data[o];
            }
        }
        for (o in params) {
            params_array.push(o + "=" + encodeURIComponent(params[o]));
        }
        if (Uri.getProtocol(url)) {
            var protocol = Uri.getProtocol(url) + "://";
        } else {
            var protocol = "";
        }
        return protocol + Uri.getHost(url, true) + Uri.getPath(url) + "?" + params_array.join("&");
    };

    Uri.toQueryString = function (options) {
        var queryString = [];
        $.each(options, function (key, value) {
            queryString.push(key + "=" + value);
        });
        return queryString.join("&");
    };
    module.exports=Uri;
})