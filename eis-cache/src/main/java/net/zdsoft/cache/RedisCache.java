package net.zdsoft.cache;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

/**
 * @author shenke
 * @since 2017.08.31
 */
public class RedisCache implements Cache {

    private RedisTemplate<String, Object> redisTemplate;

    public RedisCache(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Object doGet(String region, Object key) {
        return redisTemplate.opsForValue().get(buildKey(region, key));
    }

    @Override
    public <T> T doGet(String region, Object key, Class<T> type) {

        return null;
    }

    @Override
    public Object doPut(String region, Object key, Object value, long expire) {
        String serialization = JSON.toJSONString(value);
        redisTemplate.opsForValue().set(buildKey(region, key), serialization, expire);
        return serialization;
    }

    @Override
    public Object doSync(String region, Object key, Object value, long expire) {
        return doPut(region, key, value, expire);
    }

    @Override
    public void doDelete(String region, Object key) {
        redisTemplate.delete(buildKey(region, key));
    }

    @Override
    public void clear(Set<String> region) {

    }

    private String buildKey(String region, Object key) {
        return region.isEmpty() ? key.toString() : region + "_" + key.toString();
    }
}
