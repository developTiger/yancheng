package com.sunesoft.seera.fr.utils;

import com.sunesoft.seera.fr.results.PagedResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowy on 2016/7/14.
 */
public class Factory {
    /**
     * convert from dto
     *
     * @param source
     * @param
     * @return
     */
    public static <S, T> T convert(S source, Class<T> target) {

        T t = null;
        try {
            t = target.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        BeanUtils.copyProperties(source, t);
        return t;
    }

    public static <S, T> List<T> convert(List<S> source, Class<T> target) {
        List<T> list = new ArrayList<>();
        if (null != source)
            source.stream().forEach(s -> list.add(convert(s, target)));
        return list;
    }

    public static <S, T> PagedResult<T> convert(PagedResult<S> source, Class<T> target) {
        List<T> list = new ArrayList<>();
        if (source.getTotalItemsCount() > 0)
            list = convert(source.getItems(), target);
        return new PagedResult<>(
                list, source.getPageNumber(), source.getPageSize(), source.getTotalItemsCount());
    }
}