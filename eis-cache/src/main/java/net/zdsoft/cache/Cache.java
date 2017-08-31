package net.zdsoft.cache;

import java.util.Set;

/**
 * @author shenke
 * @since 2017.08.31
 */
public interface Cache {


    Object doGet(String region, Object key);

    <T> T doGet(String region, Object key, Class<T> type);

    Object doPut(String region, Object key, Object value, long expire);

    Object doSync(String region, Object key, Object value, long expire);

    void doDelete(String region, Object key);

    void clear(Set<String> region);
}
