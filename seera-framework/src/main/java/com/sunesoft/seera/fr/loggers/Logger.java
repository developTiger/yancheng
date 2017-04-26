package com.sunesoft.seera.fr.loggers;

/**
 * Created by zhouzh on 2015/6/29.
 */

public interface Logger {
    Boolean isEnabled(LogLevel level);
    Boolean isDebugEnabled();
    Boolean isErrorEnabled();
    Boolean isFatalEnabled();
    Boolean isInfoEnabled();
    Boolean isWarnEnabled();

    void log(LogLevel level, Exception exception, String format, Object[] args);

    void debug(String message);
    void debug(String message, Exception exception);

    void error(String message);
    void error(String message, Exception exception);

    void fatal(String message);
    void fatal(String message, Exception exception);

    void info(String message);
    void info(String message, Exception exception);

    void warn(String message);
    void warn(String message, Exception exception);

}
