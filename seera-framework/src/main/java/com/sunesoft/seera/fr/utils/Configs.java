package com.sunesoft.seera.fr.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 快速配置工具。
 */
public class Configs {

    private static Properties properties;

    private static Object locker = new Object();

    private static Logger logger = LoggerFactory.getLogger(Configs.class);

    static {
        synchronized (locker) {
            properties = new Properties();
            try {
                Enumeration<URL> ps = Thread.currentThread().getContextClassLoader().getResources("env.properties");
                while (ps.hasMoreElements()) {
                    InputStream inputStream = null;
                    try {
                        inputStream = ps.nextElement().openStream();
                        Properties p = new Properties();
                        p.load(inputStream);
                        for (Object key : p.keySet()) {
                            if (properties.containsKey(key))
                                continue;
                            properties.setProperty(key.toString(), p.getProperty(key.toString()));
                        }
                    } finally {
                        if (inputStream != null)
                            inputStream.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage(), e);
            }
        }
    }

    public static String getProperty(String key) {

        if (properties != null) {
            return (String) properties.get(key);
        }
        return "";
    }

    public static String getProperty(String key, String defaultValue) {
        if (properties != null) {
            if (properties.containsKey(key)) {
                return properties.get(key).toString();
            }
        }
        return defaultValue;
    }
}
