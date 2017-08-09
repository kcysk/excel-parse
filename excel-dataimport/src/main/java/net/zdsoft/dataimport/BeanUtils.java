package net.zdsoft.dataimport;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2017.07.31
 */
public class BeanUtils {

    public static  <O,E> Class<O> getGenericityType(Class<E> eClass, int number) {
        Type type = eClass.getGenericSuperclass();
        Type[] types = ((ParameterizedType)type).getActualTypeArguments();
        if ( ArrayUtils.isEmpty(types) ) {
            return null;
        }
        if ( number > types.length + 1 ) {
            return (Class<O>) types[types.length-1];
        }
        return (Class<O>) types[number-1];
    }

    public static <O,E> Class<O> getFirstGenericityType(Class<E> eClass) {
        return getGenericityType(eClass, 1);
    }

    public static <O,E> Class<O> getLastGenericityType(Class<E> eClass) {
        return getGenericityType(eClass,Integer.MAX_VALUE);
    }

    public static boolean setProperty(Object obj, String filedName, Object value ) {
        try {
            org.apache.commons.beanutils.BeanUtils.setProperty(obj, filedName, value);
        } catch (IllegalAccessException e) {
            return Boolean.FALSE;
        } catch (InvocationTargetException e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public static <T> T newBean(Class<T> tClass) {
        try {
            return tClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getProperty(Object obj, Field field) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T[] toArray(List<T> os, Class<T> tClass) {
        T[] array = null;
        if ( os == null ) {
            array = (T[])Array.newInstance(tClass, 0);
            return array;
        }
        array = (T[])Array.newInstance(tClass, os.size());
        return os.toArray(array);
    }

    public static <T> boolean isEmpty(T[] ts) {
        return ts == null ? Boolean.TRUE : ts.length == 0;
    }
    public static <T> boolean isNotEmpty(T[] ts) {
        return ts == null ? Boolean.FALSE : ts.length > 0;
    }

    public static List<Field> getAnnotationWithAnnotation(Class clazz, Class<? extends Annotation> annotation) {
        Field[] fields = clazz.getDeclaredFields();
        return Arrays.stream(fields).filter(e->e.getAnnotationsByType(annotation)!=null).collect(Collectors.toList());
    }

    public static Object getFiledValue( Object obj, Field field ) {
        try {
            field.setAccessible(Boolean.TRUE);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static boolean isNumer(Class type) {
        return newBean(type) instanceof Number;
    }
}
