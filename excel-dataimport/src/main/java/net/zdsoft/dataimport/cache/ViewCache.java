package net.zdsoft.dataimport.cache;

import com.alibaba.fastjson.JSON;
import net.zdsoft.dataimport.ImportRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shenke
 * @since 17-8-5 下午11:07
 */
@Component
public class ViewCache {

    @Autowired private RedisTemplate redisTemplate;

    static final String KEY = "key_import_record_";

    public List<ImportRecord> getFromCache(String userId) {
        List<Object> object = redisTemplate.opsForList().range(KEY + userId, 0, 20);

        return JSON.parseArray(object.toString(), ImportRecord.class);
    }

    public List<ImportRecord> add(String userId, ImportRecord importRecord) {
        //redisTemplate.opsForList().
        return null;
    }
}
