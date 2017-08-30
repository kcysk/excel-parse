package net.zdsoft.cache.aspectj;

import net.zdsoft.cache.support.AbstractCacheHandler;
import net.zdsoft.cache.support.CacheTargetInvoker;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.DisposableBean;

import java.lang.reflect.Method;

/**
 * cache aspectj
 * @author shenke
 * @since 2017.08.30
 */
@Aspect
class CacheAspectj extends AbstractCacheHandler implements DisposableBean{

    @Override
    public void destroy() throws Exception {

    }

    @Pointcut(value = "execution(@net.zdsoft.cache.annotation.Cacheabel public * *(..))")
    public void executionAnyPublicMethodWithCacheable() {

    }

    @Pointcut(value = "execution(@net.zdsoft.cache.annotation.CacheClear public * *(..))")
    public void executionAnyPublicMethodWithCacheClear() {

    }

    @Around(value = "executionAnyPublicMethodWithCacheable() || executionAnyPublicMethodWithCacheClear()")
    public Object executeCacheAround(ProceedingJoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        CacheTargetInvoker invoker = () -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                throw new CacheTargetInvoker.CacheTargetThrowableWrapper(throwable);
            }
        };
        try {
            return handle(invoker, joinPoint.getTarget(), method, joinPoint.getArgs());
        } catch (CacheTargetInvoker.CacheTargetThrowableWrapper e) {
            ThrowAny.throwUnchecked(e.getOriginal());
            return null; //never
        }
    }
}
