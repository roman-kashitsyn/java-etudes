package com.wiley.cache.impl;

import com.wiley.cache.CacheExhaustedException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for {@link InMemoryCache}.
 * Sleep time is increased to 50ms since system clock is not precise enough.
 */
public class InMemoryCacheTest extends CommonCacheTest<InMemoryCache> {

    @Before
    public void setUp() {
        setCache(new CacheBuilder().newInMemoryCache());
    }

    @Test
    public void testDefaultExpiration() throws Exception {
        InMemoryCache memoryCache = new CacheBuilder().setDefaultExpiration("2sec").newInMemoryCache();
        assertEquals(2000, memoryCache.getDefaultExpiration());
        memoryCache.put("Rick", "Hickey");
        assertTrue(memoryCache.isPresent("Rick"));
        Thread.sleep(2050);
        assertFalse(memoryCache.isPresent("Rick"));
    }

    @Test(expected = CacheExhaustedException.class)
    public void testCacheExhausted() {
        InMemoryCache memoryCache = new CacheBuilder().setInMemoryCacheMaxSize(2).newInMemoryCache();
        memoryCache.put("A", 1);
        memoryCache.put("B", 2);
        memoryCache.put("C", 3);
    }

    @Test
    public void testNoAutomaticCleanUp() throws Exception {
        InMemoryCache memoryCache = new CacheBuilder()
                .setDefaultExpiration("50ms")
                .newInMemoryCache();

        memoryCache.put("Donald", "Knuth");
        memoryCache.put("James", "Gosling");

        assertEquals(2, memoryCache.size());

        Thread.sleep(500);

        assertEquals(2, memoryCache.size());
        memoryCache.cleanUp();
        assertEquals(0, memoryCache.size());
    }
}
