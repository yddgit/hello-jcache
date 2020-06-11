package com.my.project.jcache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableCacheEntryListenerConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.event.CacheEntryListener;
import javax.cache.expiry.Duration;
import javax.cache.expiry.ExpiryPolicy;
import javax.cache.expiry.ModifiedExpiryPolicy;
import javax.cache.spi.CachingProvider;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Unit test
 */
public class HelloJCacheTest {

    private MutableConfiguration<String, String> config;
    private CacheManager cacheManager;

    @Before
    public void setUp() throws Exception {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        config = new MutableConfiguration<>();
        cacheManager = cachingProvider.getCacheManager();
    }

    @After
    public void tearDown() throws Exception {
        cacheManager.close();
    }

    @Test
    public void whenModifyValue() {
        Cache<String, String> cache = cacheManager.createCache("simpleCache", config);

        cache.put("key", "value");
        cache.invoke("key", new SimpleEntryProcessor());
        assertEquals("value - modified", cache.get("key"));
    }

    @Test
    public void whenRunEvent() throws InterruptedException {
        int expireTimeInSeconds = 3;
        config.setExpiryPolicyFactory(ModifiedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, expireTimeInSeconds)));
        Cache<String, String> cache = cacheManager.createCache("simpleCache", config);

        SimpleCacheEntryListener listener = new SimpleCacheEntryListener();
        CacheEntryListenerConfiguration<String, String> listenerConfiguration = new MutableCacheEntryListenerConfiguration<String, String>(
            FactoryBuilder.factoryOf(listener), null, false, true
        );
        cache.registerCacheEntryListener(listenerConfiguration);

        assertFalse(listener.getCreated());
        cache.put("key", "value");
        assertTrue(listener.getCreated());

        assertFalse(listener.getUpdated());
        cache.put("key", "newValue");
        assertTrue(listener.getUpdated());

        assertFalse(listener.getExpired());
        TimeUnit.SECONDS.sleep(expireTimeInSeconds);
        cache.get("key");
        assertTrue(listener.getExpired());

        assertFalse(listener.getRemoved());
        cache.put("key", "valueAgain");
        cache.remove("key");
        assertTrue(listener.getRemoved());
    }

    @Test
    public void whenReadingFromStorage() {
        config.setReadThrough(true);
        config.setCacheLoaderFactory(FactoryBuilder.factoryOf(new SimpleCacheLoader()));
        Cache<String, String> cache = cacheManager.createCache("simpleCache", config);

        for (int i = 0; i < 4; i++) {
            assertEquals("fromCache" + i,  cache.get(String.valueOf(i)));
        }
    }
}
