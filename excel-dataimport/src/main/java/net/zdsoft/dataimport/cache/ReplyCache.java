package net.zdsoft.dataimport.cache;

import com.google.common.collect.Maps;
import net.zdsoft.dataimport.ImportState;
import net.zdsoft.dataimport.JSONResponse;
import net.zdsoft.dataimport.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author shenke
 * @since 2017.08.03
 */
@Component
public class ReplyCache {

    private Logger logger = LoggerFactory.getLogger(ReplyCache.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private Map<String, Future> cache = Maps.newConcurrentMap();

    private Map<String, Future> WAIT_CACHE = Maps.newConcurrentMap();

    private volatile boolean running = Boolean.FALSE;

    @PostConstruct
    private void reply() {
        if ( running ) {
            return ;
        }
        new Thread(()->{
            running = true;
           while (!WAIT_CACHE.isEmpty()) {
               try {
                   Thread.sleep(5000);
                   Iterator<Map.Entry<String,Future>> iterator = WAIT_CACHE.entrySet().iterator();
                   while (iterator.hasNext()) {
                       Map.Entry<String,Future> entry = iterator.next();
                       Future<Reply> e = entry.getValue();
                       String key = entry.getKey();
                       JSONResponse jsonResponse = new JSONResponse().setBusinessValue(key).setSuccess(Boolean.TRUE);
                       if ( e.isDone() ) {
                           try {
                               Reply reply = (Reply) e.get();
                           } catch (Exception e2) {
                               logger.error("error",e2);
                               jsonResponse.setSuccess(Boolean.FALSE);
                           } finally {
                               iterator.remove();
                           }
                           simpMessagingTemplate.convertAndSend("/import/status", jsonResponse);
                       }
                   }
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
           running = false;
        }).start();
    }

    public Future put(String key, Future future) {
        WAIT_CACHE.put(key, future);
        reply();
        return cache.put(key, future);
    }

    public Future get(String key) {
        return cache.get(key);
    }

    public ImportState getState(String key) {
        Future future;
        if ( (future = get(key)) == null ) {
            return ImportState.ERROR;
        }
        if ( future.isCancelled() ) {
            return ImportState.CANCEL;
        }
        if ( future.isDone() ) {
            try {
                if (future.get() != null) {
                    return ImportState.DONE;
                }
            } catch (Exception e){
                return ImportState.ERROR;
            }
        }
        return ImportState.WAIT;
    }

    public void remove(String key) {
        cache.remove(key);
    }
}
