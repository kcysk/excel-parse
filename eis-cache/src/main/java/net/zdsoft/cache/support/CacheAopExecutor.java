package net.zdsoft.cache.support;

import com.alibaba.fastjson.JSON;
import net.zdsoft.cache.RedisCache;
import net.zdsoft.cache.annotation.CacheClear;
import net.zdsoft.cache.annotation.Cacheabel;
import net.zdsoft.cache.expression.CacheExpressionEvaluator;
import org.assertj.core.util.Lists;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author shenke
 * @since 2017.08.30
 */
public abstract class CacheAopExecutor extends AbstractCacheHandler implements InitializingBean, BeanFactoryAware,SmartInitializingSingleton{

    private boolean initialized = false;
    private BeanFactory beanFactory;
    private CacheExpressionEvaluator evaluator;

    public Object execute(CacheTargetInvoker invoker, Object target, Method method, Object[] args, Class<?> returnType) {
        if ( this.initialized ) {

            //
            CacheClear cacheClear = method.getAnnotation(CacheClear.class);

            if ( cacheClear!= null && cacheClear.beforeInvocation() ) {
                List<Object> syncKeys = Lists.newArrayList();
                for (String syncKey : cacheClear.syncKey()) {
                    Object realKey = evaluator.getValue(syncKey, evaluator.createEvaluationContext(target, method, args, target.getClass(),null, beanFactory));
                    if ( realKey != null && realKey.getClass().isArray() ) {
                        syncKeys.addAll(Arrays.asList((Object[])realKey));
                    } else {
                        syncKeys.add(realKey);
                    }
                }
                syncKeys.add(cacheClear.key());
                syncKeys.stream().filter(""::equals).forEach(this::doClear);
            }

            CacheValueWrapper result = null;
            Cacheabel cacheabel = method.getAnnotation(Cacheabel.class);
            if ( cacheabel != null ) {
                Object key = evaluator.getValue(cacheabel.key(), evaluator.createEvaluationContext(target, method, args, target.getClass(),null, beanFactory));
                result = doGet(cacheabel.region(), key, invoker::invoke);
            }

            CacheValueWrapper targetResult = null;
            //执行target method
            if ( result == null ) {
                targetResult = result = invoker::invoke;
            }

            //cache 存入
            if ( cacheabel != null && targetResult != null ) {

                result = doPut(cacheabel.region(), cacheabel.key(), targetResult.get() ,cacheabel.expire());
            }
            //转换
            return isNativeType(returnType) ? result.get() :
                    (targetResult == null ? JSON.parseObject(result.get().toString(), returnType) : targetResult.get());

        }
        return invoker.invoke();
    }

    private boolean isNativeType(Class<?> type) {
        return CharSequence.class.equals(type)
                || Number.class.equals(type)
                || int.class.equals(type)
                || float.class.equals(type)
                || double.class.equals(type)
                || short.class.equals(type);
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void afterSingletonsInstantiated() {
        RedisTemplate redisTemplate = (RedisTemplate) beanFactory.getBean("redisTemplate");
        setCache(new RedisCache(redisTemplate));
        this.initialized = true;
    }
}
