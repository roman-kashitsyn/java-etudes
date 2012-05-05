package com.wiley.cache.impl;

import com.wiley.cache.Cache;
import com.wiley.cache.Caches;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link SelfCleaningCache}.
 */
public class SelfCleaningCacheTest extends CommonCacheTest<SelfCleaningCache> {
    
    @Before
    public void setUp() {
        setCache(new SelfCleaningCache(new CacheBuilder().newInMemoryCache(), "30sec"));
    }

    @Test
    public void testCleanUp() throws Exception {
        Cache selfCleaningCache = Caches.selfCleaningCache(
                new CacheBuilder().newInMemoryCache(), "1sec"
        );
        selfCleaningCache.put("Donald", "Knuth", "500ms");
        selfCleaningCache.put("Joshua", "Bloch", "500ms");
        assertEquals(2, selfCleaningCache.size());

        Thread.sleep(1100);

        assertEquals(0, selfCleaningCache.size());
    }

    @Test
    public void testMoreComplicatedCleanUp() throws Exception {
        Cache selfCleaningCache = Caches.selfCleaningCache(
                new CacheBuilder().newInMemoryCache(), "1sec"
        );
        selfCleaningCache.put("Donald", "Knuth", "5sec");
        selfCleaningCache.put("Joshua", "Bloch", "500ms");
        selfCleaningCache.put("Rick", "Hickey", "300ms");
        selfCleaningCache.put("Rick", "Hickey", "2sec");
        selfCleaningCache.put("Richard", "Stallman", "10ms");
        selfCleaningCache.put("Richard", "Stallman", "3sec");
        selfCleaningCache.put("James", "Gosling", "200ms");

        assertEquals(5, selfCleaningCache.size());

        Thread.sleep(1100);

        assertEquals(3, selfCleaningCache.size());
        assertTrue(selfCleaningCache.isPresent("Donald"));
        assertTrue(selfCleaningCache.isPresent("Rick"));
        assertTrue(selfCleaningCache.isPresent("Richard"));
    }
}
