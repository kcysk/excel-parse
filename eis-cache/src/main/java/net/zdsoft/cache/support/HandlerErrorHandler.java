package net.zdsoft.cache.support;

/**
 * @author shenke
 * @since 2017.08.31
 */
public interface HandlerErrorHandler {

    Object doGetError(Object key, Object value);

    Object doPutError(Object key, Object value);

    Object doSyncError(Object key, Object result);

    void doClearError(Object key);
}
