package net.zdsoft.cache.expression;

import java.lang.reflect.Method;

/**
 * @author shenke
 * @since 2017.08.31
 */
public class ExpressionRoot {

    private Object target;
    private Method method;
    private Object[] args;
    private Class<?> targetClass;

    public ExpressionRoot(Object target, Method method, Object[] args, Class<?> targetClass) {
        this.target = target;
        this.method = method;
        this.args = args;
        this.targetClass = targetClass;
    }

    public Object getTarget() {
        return target;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }
}
