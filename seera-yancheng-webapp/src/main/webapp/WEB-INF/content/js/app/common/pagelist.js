/**
 * Created by zhouz on 2016/5/19.
 */

define(function (require, exports, module) {
    var $= require("jquery");
    require("../common/templeteHelper");
    require("bsPage");
    var check = require("../common/checkbox");
    return function (target, tpl, url, data,pageIndex,pageSize) {
        var extendPage = 5;
        var container = $(target);
        $.ajax({
            type: "get",
            url: encodeURI(url + "?t=" + new Date().getMilliseconds() + "&" + data),
            async: false,
            success: function (result) {
                if (result) {
                    //var jsonResult = JSON.parse(result);
                    var html = template.compile(tpl)(result);
                    container.html(html);
                    check.niceCheck(target+" input")
                    if (result.pagesCount > 0) {
                        var options = {
                            numberOfPages: extendPage,
                            bootstrapMajorVersion: 3,
                            currentPage: result.pageNumber, //当前页数
                            totalPages: result.pagesCount, //总页数
                            itemTexts: function (type, page, current) {
                                switch (type) {
                                    case "first":
                                        return "首页";
                                    case "prev":
                                        return "上一页";
                                    case "next":
                                        return "下一页";
                                    case "last":
                                        return "末页";
                                    case "page":
                                        return page;
                                }
                            },
                            onPageClicked: function (event, originalEvent, type, page) {
                                $.ajax({
                                    url:encodeURI( url + "?t=" + new Date().getMilliseconds() + "&" + data + "&pageNumber=" + page + "&pageSize=" + pageSize),
                                    type: "Get",
                                    async: false,
                                    success: function (data1) {
                                        // var jsonResult = JSON.parse(data1);
                                        var html = template.compile(tpl)(data1);
                                        container.html(html);
                                        check.niceCheck(target+" input")
                                    }
                                })
                            }
                        }
                        container.next().find(".pagination").bootstrapPaginator(options);
                    }
                    else{
                        container.next().find(".pagination").html("");
                    }
                }
            }
        });

    };
})
