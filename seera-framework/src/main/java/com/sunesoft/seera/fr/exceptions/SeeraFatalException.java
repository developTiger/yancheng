package com.sunesoft.seera.fr.exceptions;

/**
 * Created by zhouz on 2016/5/11.
 */
public class SeeraFatalException extends Exception {

    public SeeraFatalException(String message) {
        super(message);
    }

    public SeeraFatalException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeeraFatalException(Throwable cause) {
        super(cause);
    }
}
