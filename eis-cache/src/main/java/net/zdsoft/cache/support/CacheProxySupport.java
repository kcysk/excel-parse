package net.zdsoft.cache.support;

import java.lang.reflect.Method;

/**
 * @author shenke
 * @since 17-8-29下午10:28
 */
public abstract class CacheProxySupport {


    protected Object execute(CacheTargetInvokerWrapper invokerWrapper, Object target, Method method, Object[] args) {

        return invokerWrapper.invoker();
    }
}
