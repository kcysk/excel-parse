package net.zdsoft.cache.support;

import net.zdsoft.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author shenke
 * @since 2017.08.31
 */
public abstract class AbstractCacheHandler {

    private static final Logger logger = LoggerFactory.getLogger("CacheHandler");

    private HandlerErrorHandler handlerErrorHandler;
    private Cache cache;

    public CacheValueWrapper doGet(String region, Object key, TargetInvoker invoker) {
        try {
            Object cacheValue = cache.doGet(region, key);
            return cacheValue == null ? null : () -> cacheValue;
        } catch (RuntimeException e){
            logger.error("");
            return () -> getErrorHandler().doGetError(key, invoker.invoke());
        }
    }

    public Object doSync(Object key, Object result) {
        return null;
    }

    protected CacheValueWrapper doPut(String region, Object key, Object value, long expire) {
        try {
            return () -> cache.doPut(region, key, value, expire);
        } catch (RuntimeException e) {
            logger.error("");
            return () -> getErrorHandler().doPutError(key, value);
        }
    }

    public void doClear(Object key) {

    }

    private HandlerErrorHandler getErrorHandler() {
        this.handlerErrorHandler = new CacheErrorHandler();
        return handlerErrorHandler;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    interface CacheValueWrapper {
        Object get();
    }

    interface TargetInvoker {
        Object invoke();
    }
}
