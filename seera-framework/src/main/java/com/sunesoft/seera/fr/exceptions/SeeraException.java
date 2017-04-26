package com.sunesoft.seera.fr.exceptions;

/**
 * Created by zhouz on 2016/5/11.
 */
public class SeeraException extends Exception {
    public SeeraException(String message) {
        super(message);
    }

    public SeeraException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeeraException(Throwable cause) {
        super(cause);
    }
}
