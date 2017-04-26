package com.sunesoft.seera.fr.exceptions;

/**
 * Created by zhouz on 2016/5/11.
 */
public class ArgumentException extends Error {
    public ArgumentException(String message) {
        super(message);
    }

    public ArgumentException(Throwable cause) {
        super(cause);
    }

    public ArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
