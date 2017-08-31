package net.zdsoft.cache.support;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.io.Serializable;

/**
 * @author shenke
 * @since 17-8-29下午10:29
 */
public class CacheInterceptor implements MethodInterceptor, Serializable {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        CacheTargetInvoker invokerWrapper = () -> {
            try {
                return methodInvocation.proceed();
            } catch (Throwable throwable) {
                throw new CacheTargetInvoker.CacheTargetThrowableWrapper(throwable);
            }
        };
        try {
            //cache逻辑处理
            //return cacheHandler.e(invokerWrapper, methodInvocation.getThis(), methodInvocation.getMethod(), methodInvocation.getArguments());
            return null;
        } catch (CacheTargetInvoker.CacheTargetThrowableWrapper throwableWrapper) {
            throw throwableWrapper.getOriginal();
        }
    }
}
