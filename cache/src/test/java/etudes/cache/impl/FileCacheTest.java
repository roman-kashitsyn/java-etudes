package com.wiley.cache.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileCacheTest extends CommonCacheTest<FileCache> {

    @Before
    public void setUp() {
        setCache(new FileCache(new CacheBuilder()));
    }

    @Test
    public void testCachePersistence() throws Exception {
        FileCache fCache = new FileCache(new CacheBuilder());
        fCache.put("Hello", "World", "2sec");
        fCache = new FileCache(new CacheBuilder());
        Thread.sleep(500);
        assertTrue(fCache.isPresent("Hello"));
        assertEquals("World", fCache.load("Hello", String.class));
        Thread.sleep(1500);
        assertFalse(fCache.isPresent("Hello"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnlySerializableObjectsAllowed() {
        cache.put("A", new Object());
    }
}
