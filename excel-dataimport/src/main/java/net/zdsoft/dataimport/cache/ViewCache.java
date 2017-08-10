package net.zdsoft.dataimport.cache;

import com.alibaba.fastjson.JSON;
import net.zdsoft.dataimport.ImportRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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
        List<Object> object = redisTemplate.opsForList().range(KEY + getCurrentAction() + userId, 0, 20);

        return JSON.parseArray(object.toString(), ImportRecord.class);
    }

    public void add(String userId, ImportRecord importRecord) {
        redisTemplate.opsForList().rightPush(KEY + getCurrentAction() + userId, importRecord);
    }

    public void delete(String userId,ImportRecord importRecord) {
        redisTemplate.opsForList().remove(KEY + getCurrentAction() + userId, 0 , importRecord);
    }

    private String getCurrentAction() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if ( requestAttributes != null ) {
            return requestAttributes.getRequest().getAttribute("actionName").toString();
        }
        return "";
    }
}
