package com.sunesoft.seera.fr.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;


public class JsonHelper {

    private static Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PROTECTED).create();

    /**
     * 将对象转换为JSON字符串
     *
     * @param obj 对象
     * @return JSON字符串
     */
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }


    /**
     * 将JSON字符串转换为对象
     * <p style="color:red;">注意:不能对列表进行转换</p>
     *
     * @param jsonStr   JSON字符串
     * @param className 类名称
     * @return 对象。
     */
    public static Object toObject(String jsonStr, Class className) {
        return gson.fromJson(jsonStr, className);

    }

    /**
     * 将JSON字符串转换为对象
     * <p style="color:red;">注意:列表转换请使用此方法</p>
     *
     * @param jsonStr JSON字符串
     * @param token   类类型
     * @return 对象
     * @<p style="color:green;"> 使用示例:
     * List<Person> ps = toObject(str, new TypeToken<List<Person>>(){}.getType());
     * </p>
     */
    public static Object toObject(String jsonStr, TypeToken token) {
        return gson.fromJson(jsonStr, token.getType());
    }

    /**
     * 将JSON字符串转换为对象（列表类对象使用）
     *
     * @param jsonStr JSON字符串
     * @param type    类类型
     * @return 对象
     * @<p style="color:green;"> 使用示例:
     * List<Person> ps = toObject(str, new TypeToken<List<Person>>(){}.getType());
     * </p>
     */
    public static Object toObject(String jsonStr, Type type) {
        return gson.fromJson(jsonStr, type);
    }
}
