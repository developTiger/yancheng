package com.sunesoft.seera.fr.annotations;

import java.lang.annotation.*;

/**
 * Created by zhouz on 2016/5/17.
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventListener {
}
