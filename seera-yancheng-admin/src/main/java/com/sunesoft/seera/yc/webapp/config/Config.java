package com.sunesoft.seera.yc.webapp.config;

import org.apache.velocity.texen.util.PropertiesUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取配置文件参数
 *
 * @author zhouz
 */
public class Config {

    private static Properties properties = new Properties();

    /**
     * 初始化properties，即载入数据
     */
    private static void initProperties(String filePath) {
        try {
            InputStream ips = PropertiesUtil.class.getClassLoader()
                    .getResourceAsStream(filePath);
            properties.load(ips);
            ips.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将数据载入properties，并返回"key"的值
     *
     * @return
     */
    private static String getPrimaryKey(String key, String filePath) {
        if (properties.isEmpty())// 如果properties为空，则初始化一次。
            initProperties(filePath);
        return properties.getProperty(key); // properties返回的值为String，转为整数
    }

    /**
     * 获取配置文件参数
     *
     * @return
     */
    public static String getConfigParameter(String key, String filePath) {
        return getPrimaryKey(key, filePath);
    }


}
