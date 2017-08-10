package org.webjars;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * 这种扩展有些问题
 * @author shenke
 * @since 17-8-7 下午10:18
 */
public class WebJarAssetLocatorEx extends WebJarAssetLocator {

    private Logger logger = LoggerFactory.getLogger(WebJarAssetLocatorEx.class);

    protected SortedMap<String, String> fullPathIndex;

    public WebJarAssetLocatorEx() {
        fullPathIndex = getFullPathIndex(Pattern.compile(".*"), WebJarAssetLocator.class.getClassLoader());
    }

    @Override
    public String getFullPath(String partialPath) {
        return getFullPath(this.fullPathIndex, partialPath);
    }

    @Override
    public String getFullPath(String webjar, String partialPath) {
        return getFullPath(filterPathIndexByPrefix(fullPathIndex, WEBJARS_PATH_PREFIX + "/" + webjar + "/"), partialPath);
    }

    protected String getFullPath(SortedMap<String, String> pathIndex, String partialPath){
        if (partialPath.charAt(0) == '/') {
            partialPath = partialPath.substring(1);
        }

        final String reversePartialPath = reversePath(partialPath);

        final SortedMap<String, String> fullPathTail = pathIndex.tailMap(reversePartialPath);

        if (fullPathTail.size() == 0) {
            logger.error("{} not found", partialPath);
            throwNotFoundException(partialPath);
        }

        final Iterator<Map.Entry<String, String>> fullPathTailIter = fullPathTail
                .entrySet().iterator();
        final Map.Entry<String, String> fullPathEntry = fullPathTailIter.next();
        if (!fullPathEntry.getKey().startsWith(reversePartialPath)) {
            throwNotFoundException(partialPath);
        }
        final String fullPath = fullPathEntry.getValue();

        if (fullPathTailIter.hasNext()) {
            List<String> matches = null;

            while (fullPathTailIter.hasNext()) {
                Map.Entry<String, String> next = fullPathTailIter.next();
                if (next.getKey().startsWith(reversePartialPath)) {
                    if (matches == null) {
                        matches = new ArrayList<String>();
                    }
                    matches.add(next.getValue());
                } else {
                    break;
                }
            }

            if (matches != null) {
                matches.add(fullPath);
                logger.warn("Multiple matches found for {} use {}", partialPath, fullPath);
            }
        }

        return fullPath;
    }

    protected String throwNotFoundException(String partialPath) {
        throw new IllegalArgumentException(partialPath + " could not be found. Make sure you've added the corresponding WebJar and please check for typos.");
    }

    private static String reversePath(String assetPath) {
        String[] assetPathComponents = assetPath.split("/");
        StringBuilder reversedAssetPath = new StringBuilder();

        for(int i = assetPathComponents.length - 1; i >= 0; --i) {
            reversedAssetPath.append(assetPathComponents[i]);
            reversedAssetPath.append('/');
        }

        return reversedAssetPath.toString();
    }

    private SortedMap<String, String> filterPathIndexByPrefix(SortedMap<String, String> pathIndex, String prefix) {
        SortedMap<String, String> filteredPathIndex = new TreeMap<String, String>();
        for (String key : pathIndex.keySet()) {
            String value = pathIndex.get(key);
            if (value.startsWith(prefix)) {
                filteredPathIndex.put(key, value);
            }
        }
        return filteredPathIndex;
    }
}
