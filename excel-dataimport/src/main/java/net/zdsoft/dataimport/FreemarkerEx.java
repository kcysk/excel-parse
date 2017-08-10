package net.zdsoft.dataimport;

import com.alibaba.fastjson.JSON;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateModelException;
import net.zdsoft.dataimport.freemarker.ViewDataFreemarkerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shenke
 * @since 2017.08.10
 */
@Configuration
public class FreemarkerEx {

    private Logger logger = LoggerFactory.getLogger(FreemarkerEx.class);
    @Resource
    private freemarker.template.Configuration freeMarkerConfiguration;

    @PostConstruct
    public void addVariables() {
        Map<String,Object> map = new HashMap();
        map.put("ViewDataFreemarkerUtils", new ViewDataFreemarkerUtils());
        try {
            this.freeMarkerConfiguration.setAllSharedVariables(new SimpleHash(map));
        } catch (TemplateModelException e) {
            logger.error("ViewDataFreemarkerUtils freemarker共享失败");
        }
    }

    @Bean
    public CacheManager registerCacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        return redisCacheManager;
    }

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        RedisSerializer redisSerializer = new SpringCacheSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(redisSerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    static class SpringCacheSerializer implements RedisSerializer<Object> {

        @Override
        public byte[] serialize(Object o) throws SerializationException {
            return JSON.toJSONString(o).getBytes();
        }

        @Override
        public Object deserialize(byte[] bytes) throws SerializationException {
            Object obj = null;
            try {
                obj = JSON.parseObject(new String(bytes));
            } catch (ClassCastException e){
                obj = new String(bytes);
            }
            return obj;
        }
    }
}
