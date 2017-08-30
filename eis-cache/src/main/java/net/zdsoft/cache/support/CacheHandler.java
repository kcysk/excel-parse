package net.zdsoft.cache.support;

import java.lang.reflect.Method;

/**
 * 缓存处理接口
 * @author shenke
 * @since 2017.08.30
 */
public interface CacheHandler {

    Object handle(CacheTargetInvoker invoker, Object target, Method method, Object[] args);

}
