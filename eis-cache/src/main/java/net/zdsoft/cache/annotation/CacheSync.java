package net.zdsoft.cache.annotation;

import java.lang.annotation.*;

/**
 * @author shenke
 * @since 17-8-29下午10:25
 */
@Documented
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheSync {

    String region() default "";

    String key();

    String condition() default "";
}
