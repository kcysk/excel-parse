package net.zdsoft.dataimport.cache;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @author shenke
 * @since 17-8-5 下午11:07
 */
public class ViewCache {

    Map<String, List<String>> headerCache = Maps.newConcurrentMap();

    public List<String> getHeaders(String tClassName) {
        return headerCache.get(tClassName);
    }
}
