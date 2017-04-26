package com.sunesoft.seera.fr.exceptions;

/**
 * Created by zhouz on 2016/5/11.
 */
public class Fatal {

    public static <T extends Exception> Boolean  isFatal(T  ex){
        return ex instanceof NullPointerException ||
                ex instanceof NoSuchMethodException;//……

    }
}
