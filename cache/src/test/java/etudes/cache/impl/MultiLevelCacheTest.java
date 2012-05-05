package com.wiley.cache.impl;

import com.wiley.cache.Cache;
import com.wiley.cache.CacheExhaustedException;
import com.wiley.cache.Caches;
import com.wiley.cache.ReplacementListener;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link MultiLevelCache}.
 */
public class MultiLevelCacheTest extends CommonCacheTest<MultiLevelCache> {

    @Before
    public void setUp() {
        setCache(new CacheBuilder().newMultiLevelCache());
    }

    @Test(expected = CacheExhaustedException.class)
    public void testMaxSize() {
        MultiLevelCache multiLevelCache = new CacheBuilder()
                .setInMemoryCacheMaxSize(2)
                .setFileCacheMaxSize(2)
                .newMultiLevelCache();

        multiLevelCache.put("A", 1);
        multiLevelCache.put("B", 2);
        multiLevelCache.put("C", 3);
        multiLevelCache.put("D", 4);
        multiLevelCache.put("E", 5);
    }

    @Test
    public void testLruReplacement() throws Exception {
        MultiLevelCache multiLevelCache = new CacheBuilder()
                .setReplacementStrategy(Caches.leastRecentlyUsedStrategy())
                .setInMemoryCacheMaxSize(2)
                .newMultiLevelCache();
        RecordingReplacementListener listener = new RecordingReplacementListener();
        multiLevelCache.addReplacementListener(listener);

        multiLevelCache.put("Donald", "Knuth");
        Thread.sleep(100);
        multiLevelCache.put("Denis", "Richie");

        multiLevelCache.put("Rick", "Hickey");

        assertEquals(1, listener.replacedEntries().size());
        assertTrue(listener.replacedEntries().containsKey("Donald"));
    }

    @Test
    public void testMruReplacement() throws Exception {
        MultiLevelCache multiLevelCache = new CacheBuilder()
                .setReplacementStrategy(Caches.mostRecentlyUsedStrategy())
                .setInMemoryCacheMaxSize(2)
                .newMultiLevelCache();
        RecordingReplacementListener listener = new RecordingReplacementListener();
        multiLevelCache.addReplacementListener(listener);

        multiLevelCache.put("Donald", "Knuth");
        Thread.sleep(100);
        multiLevelCache.put("Denis", "Richie");

        multiLevelCache.put("Rick", "Hickey");

        assertEquals(1, listener.replacedEntries().size());
        assertTrue(listener.replacedEntries().containsKey("Denis"));
    }
    
    @Test
    public void testLfuReplacement() {
        MultiLevelCache multiLevelCache = new CacheBuilder()
                .setReplacementStrategy(Caches.leastFrequentlyUsedStrategy())
                .setInMemoryCacheMaxSize(2)
                .newMultiLevelCache();
        RecordingReplacementListener listener = new RecordingReplacementListener();
        multiLevelCache.addReplacementListener(listener);

        multiLevelCache.put("Donald", "Knuth");
        multiLevelCache.load("Donald");
        multiLevelCache.put("Denis", "Richie");

        multiLevelCache.put("Rick", "Hickey");

        assertEquals(1, listener.replacedEntries().size());
        assertTrue(listener.replacedEntries().containsKey("Denis"));
    }
    
    @Test
    public void stressTest() {
        MultiLevelCache multiLevelCache = new CacheBuilder()
                .setInMemoryCacheMaxSize(5000)
                .setFileCacheMaxSize(5000)
                .newMultiLevelCache();

        // clear data possibly stored in file cache
        multiLevelCache.invalidateAll();
        for (int i = 0; i < 10000; i++) {
            multiLevelCache.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        }
        assertEquals(10000, multiLevelCache.size());
        multiLevelCache.invalidateAll();
        assertEquals(0, multiLevelCache.size());
    }

    private static class RecordingReplacementListener implements ReplacementListener {

        private Map<String, Cache.Entry> entries = new HashMap<String, Cache.Entry>();

        public void onReplacement(Cache.Entry entry) {
            entries.put(entry.key(), entry);
        }

        public Map<String, Cache.Entry> replacedEntries() {
            return entries;
        }
    }
}
