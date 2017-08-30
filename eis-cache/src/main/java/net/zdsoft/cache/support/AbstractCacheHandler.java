package net.zdsoft.cache.support;

import java.lang.reflect.Method;

/**
 * @author shenke
 * @since 2017.08.30
 */
public class AbstractCacheHandler implements CacheHandler {

    @Override
    public Object handle(CacheTargetInvoker invoker, Object target, Method method, Object[] args) {

        return invoker.invoke();
    }
}
