package net.zdsoft.dataimport.cache;

import com.google.common.collect.Maps;

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

    public static void remove(String key) {
        cache.remove(key);
    }
}
