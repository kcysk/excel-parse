package net.zdsoft.cache.support;

/**
 * 处理因为缓存出现异常保证业务不受影响
 * @author shenke
 * @since 2017.08.31
 */
public class CacheErrorHandler implements HandlerErrorHandler {

    @Override
    public Object doGetError(Object key, Object value) {
        //try
        return value;
    }

    @Override
    public Object doSyncError(Object key, Object result) {
        return result;
    }

    @Override
    public void doClearError(Object key) {
        //do nothing
    }

    @Override
    public Object doPutError(Object key, Object value) {
        return value;
    }
}
