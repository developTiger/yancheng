package com.sunesoft.seera.fr.matcher;

/**
 * Created by zhouz on 2016/5/17.
 */
public interface Matcher<T> {

    boolean matches(T var1);

    Matcher<T> and(Matcher<? super T> var1);

    Matcher<T> or(Matcher<? super T> var1);

}
