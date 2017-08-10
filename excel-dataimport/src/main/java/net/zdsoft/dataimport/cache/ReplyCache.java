package net.zdsoft.dataimport.cache;

import com.google.common.collect.Maps;
import net.zdsoft.dataimport.ImportState;

import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.Future;

/**
 * @author shenke
 * @since 2017.08.03
 */
public class ReplyCache {

    private static Map<String, Future> cache = Maps.newConcurrentMap();

    static {
        new TimerTask(){
            @Override
            public void run() {

            }
        };
    }

    public static Future put(String key, Future future) {
        return cache.put(key, future);
    }

    public static Future get(String key) {
        return cache.get(key);
    }

    public static ImportState getState(String key) {
        Future future;
        if ( (future = get(key)) == null ) {
            return ImportState.WAIT;
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
        return ImportState.ING;
    }

    public static void remove(String key) {
        cache.remove(key);
    }
}
