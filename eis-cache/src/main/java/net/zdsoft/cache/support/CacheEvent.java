package net.zdsoft.cache.support;

import java.lang.reflect.Method;

/**
 * @author shenke
 * @since 2017.08.31
 */
public class CacheEvent {

    private CacheEventMetaData eventMetaData;

    private String key;
    private String condition;
    private String region;

    public CacheEvent(String key, String condition, String region, CacheEventMetaData cacheEventMetaData) {
        this.key = key;
        this.condition = condition;
        this.region = region;
        this.eventMetaData = cacheEventMetaData;
    }

    public String getKey() {
        return key;
    }

    public String getCondition() {
        return condition;
    }

    public String getRegion() {
        return region;
    }

}
