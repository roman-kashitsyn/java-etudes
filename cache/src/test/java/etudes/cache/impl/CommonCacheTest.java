package com.wiley.cache.impl;

import com.wiley.cache.Cache;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Common tests for cache.
 * @param <T> concrete cache type parameter
 */
public abstract class CommonCacheTest<T extends Cache> {
    protected T cache;

    @Test
    public void testSimpleAddition() throws Exception {
        cache.put("Hello", "World", "1sec");
        assertTrue(cache.isPresent("Hello"));
        assertEquals("World", cache.load("Hello"));
        Thread.sleep(1050);
        assertFalse(cache.isPresent("Hello"));
    }

    @Test
    public void testEntryInvalidation() {
        cache.put("Hello", "World");
        cache.invalidate("Hello");
        assertFalse(cache.isPresent("Hello"));
    }
    
    @Test
    public void testInvalidateAll() {
        cache.put("A", 1);
        cache.put("B", 2);
        assertTrue(cache.size() > 0);
        cache.invalidateAll();
        assertEquals(0, cache.size());
    }
    
    @Test
    public void testSize() {
        cache.invalidateAll();
        cache.put("One", 1);
        cache.put("Two", 2);
        cache.put("Three", 3);
        assertEquals(3, cache.size());
    }

    protected void setCache(T cache) {
        this.cache = cache;
    }
}
