package com.sunesoft.seera.yc.core.order.domain;

/**
 * Created by zwork on 2016/10/31.
 */
public enum  ItemStatus {

    //尚为创建门票 0
    notCreate,

    //取消失败 1
    cancelFailed,

    //已取消 2
    canceled,

    //门票创建失败 3
    createError,

    //门票已创建 4
    created,

    //门票重新创建失败 5
    reCreateError,

    //门票已重新创建 6
    reCreated,

    //已使用 7
    checked,


    //二维码错误 8
    imgError,


    //门票创建成功 9
    allSuccess,

    //管家婆操作失败 10
    gjpError
}
