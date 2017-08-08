package net.zdsoft.dataimport.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shenke
 * @since 2017.08.08
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Exporter {

    String example() default "";

    String mcode() default "";

    int displayOrder() default 0;

    String[] selectItems() default "";

    boolean defaultChecked() default false;
}
