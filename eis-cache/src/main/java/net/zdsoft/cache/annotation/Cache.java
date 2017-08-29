package net.zdsoft.cache.annotation;

import java.lang.annotation.*;

/**
 *
 * @author shenke
 */
@Documented
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {

    /**
     * expire time
     */
    long expire() default 0L;

    /**
     * support springEL
     */
    String key();

    String region() default "";

    String condition() default "";
}
