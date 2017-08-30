package net.zdsoft.cache.ap;

import net.zdsoft.cache.annotation.CacheClear;
import net.zdsoft.cache.annotation.Cacheabel;
import org.springframework.stereotype.Component;

/**
 * @author shenke
 * @since 2017.08.30
 */
@Component
public class UDao {

    @Cacheabel(key = "#root.args[0]")
    public String getUById(String id) {

        return "a";
    }

    @CacheClear(key = "$$")
    public String findUByName() {
        return "t";
    }
}
