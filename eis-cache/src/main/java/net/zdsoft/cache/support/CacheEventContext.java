package net.zdsoft.cache.support;

import net.zdsoft.cache.annotation.Cacheabel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shenke
 * @since 17-9-1上午12:37
 */
public class CacheEventContext {

    private Map<Class<? extends CacheEvent>, List<CacheEvent>> cacheEvents = new LinkedHashMap<>();

    public CacheEventContext(Object target, Method method, Object[] args) {
        Cacheabel cacheabel = method.getAnnotation(Cacheabel.class);
        if( cacheabel != null ) {
            CacheEventMetaData eventMetaData = new CacheEventMetaData(method, target, args);
            CacheEvent cachePutEvent = new CachePutEvent(cacheabel.key(), cacheabel.condition(), cacheabel.region(), eventMetaData ,cacheabel.expire());
            List<CacheEvent> cps = new ArrayList<CacheEvent>(){{add(cachePutEvent);}};
            cacheEvents.put(CachePutEvent.class, cps);
        }
    }

    class CachePutEvent extends CacheEvent {
        private long expire;

        public CachePutEvent(String key, String condition, String region, CacheEventMetaData eventMetaData,long expire) {
            super(key, condition, region, eventMetaData);
            this.expire = expire;
        }

        public long getExpire() {
            return expire;
        }
    }
}
