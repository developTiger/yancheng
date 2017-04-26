package com.sunesoft.seera.fr.loggers;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.stereotype.Service;

/**
 * Created by zhouzh on 2015/6/29.
 */
@Service("logger")
public class Log4jLoggerImpl implements Logger {

    private org.apache.log4j.Logger jLogger;


    public Log4jLoggerImpl(){
        String filePath = this.getClass().getResource("/").getPath();
        // 找到log4j.properties配置文件所在的目录(已经创建好)
        //filePath = filePath.substring(1).replace("bin", "src");
        // loger所需的配置文件路径
        PropertyConfigurator.configure(filePath + "log4j.properties");
        jLogger=org.apache.log4j.Logger.getLogger(this.getClass());
    }
    @Override
    public Boolean isEnabled(LogLevel level) {
        switch (level) {
            case Debug:
                return jLogger.isDebugEnabled();
            case Information:
                return jLogger.isInfoEnabled();
            case Warning:
                return jLogger.isInfoEnabled();
            case Error:
                return jLogger.isInfoEnabled();
            case Fatal:
                return jLogger.isInfoEnabled();
        }
        return false;
    }


    @Override
    public void log(LogLevel level, Exception exception, String format, Object[] args) {
        if (args != null) {
            format = String.format(format, args);
        }
        switch (level) {
            case Debug:
                jLogger.debug(format, exception);
                break;
            case Information:
                jLogger.info(format, exception);
                break;
            case Warning:
                jLogger.warn(format, exception);
                break;
            case Error:
                jLogger.error(format, exception);
                break;
            case Fatal:
                jLogger.fatal(format, exception);
                break;
        }
    }


    @Override
    public void info(String message) {
        log(LogLevel.Information, null, message, null);
    }

    @Override
    public void info(String message, Exception exception) {
        log(LogLevel.Information, exception, message, null);
    }




    @Override
    public Boolean isDebugEnabled() {
        return isEnabled(LogLevel.Debug);
    }

    @Override
    public Boolean isErrorEnabled() {
        return isEnabled(LogLevel.Error);
    }

    @Override
    public Boolean isFatalEnabled() {
        return isEnabled(LogLevel.Fatal);
    }

    @Override
    public Boolean isInfoEnabled() {
        return isEnabled(LogLevel.Information);
    }

    @Override
    public Boolean isWarnEnabled() {
        return isEnabled(LogLevel.Warning);
    }




    @Override
    public void debug(String message) {
        log(LogLevel.Debug, null, message, null);
    }

    @Override
    public void debug(String message, Exception exception) {

            log(LogLevel.Debug, exception, message, null);
    }



    @Override
    public void error(String message) {
        log(LogLevel.Error, null, message, null);
    }

    @Override
    public void error(String message, Exception exception) {

            log(LogLevel.Error, exception, message, null);
    }



    @Override
    public void fatal(String message) {
        log(LogLevel.Fatal, null, message, null);
    }

    @Override
    public void fatal(String message, Exception exception) {
        log(LogLevel.Fatal, exception, message, null);
    }



    @Override
    public void warn(String message) {
        log(LogLevel.Warning, null, message, null);
    }

    @Override
    public void warn(String message, Exception exception) {
        log(LogLevel.Warning, exception, message, null);
    }


}
