package net.zdsoft.cache.support;

import java.lang.reflect.Method;

/**
 * @author shenke
 * @since 17-9-1上午1:28
 */
public class CacheEventMetaData {

    private Method method;
    private Object target;
    private Object[] args;

    public CacheEventMetaData(Method method, Object target, Object[] args) {
        this.method = method;
        this.target = target;
        this.args = args;
    }

    public Method getMethod() {
        return method;
    }

    public Object getTarget() {
        return target;
    }

    public Object[] getArgs() {
        return args;
    }
}
