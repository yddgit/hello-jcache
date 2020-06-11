package com.my.project.jcache;

import javax.cache.integration.CacheLoader;
import javax.cache.integration.CacheLoaderException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SimpleCacheLoader implements CacheLoader<String, String>, Serializable {
    @Override
    public String load(String key) throws CacheLoaderException {
        return "fromCache" + key;
    }

    @Override
    public Map<String, String> loadAll(Iterable<? extends String> keys) throws CacheLoaderException {
        Map<String, String> data = new HashMap<String, String>();
        for (String key : keys) {
            data.put(key, load(key));
        }
        return data;
    }
}
