package net.zdsoft.dataimport.annotation;

import org.assertj.core.util.Lists;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2017.08.10
 */
public class AnnotationUtils {

    public static <A extends Annotation> List<A> getAnnotations(Class<?> objClass, Class<A> aClass) {
        return Arrays.stream(objClass.getDeclaredFields())
                .filter(field -> field.getAnnotation(aClass)!=null)
                .map(field -> field.getAnnotation(aClass))
                .collect(Collectors.toList());
    }

}
