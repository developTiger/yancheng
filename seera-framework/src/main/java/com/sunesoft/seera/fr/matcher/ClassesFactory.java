package com.sunesoft.seera.fr.matcher;

import com.google.common.reflect.ClassPath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouz on 2016/5/17.
 */

public class ClassesFactory {

    private static ClassesFactory classesFactory;
    private  List<Class<?>> clazzes = new ArrayList<Class<?>>();


    static {
        try {
            classesFactory = new ClassesFactory();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            ClassPath cs = ClassPath.from(loader);
            for (ClassPath.ClassInfo classInfo : cs.getAllClasses()) {
               if(classInfo.getName().startsWith("com.sunesoft.lemon")) {
                   classesFactory.clazzes.add(classInfo.load());
               }
            }
        } catch (Exception ex) {
        }
    }

    public List<Class<?>> getAllClass(){
        return this.clazzes;
    }

    public static  ClassesFactory getInstance(){
        if(classesFactory==null){
            try {
                classesFactory = new ClassesFactory();
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                ClassPath cs = ClassPath.from(loader);
                for (ClassPath.ClassInfo classInfo : cs.getAllClasses()) {
                    if(classInfo.getName().startsWith("com.sunesoft.lemon"));
                    classesFactory.clazzes.add(classInfo.load());
                }
            } catch (Exception ex) {
            }
        }
        return classesFactory;
    }


}
