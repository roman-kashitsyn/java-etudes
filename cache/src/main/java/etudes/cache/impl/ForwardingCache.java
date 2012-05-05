package com.wiley.cache.impl;

import com.wiley.cache.Cache;

/**
 * Decorator base for Cache. Delegates all method invocations to underlying cache implementation.
 */
public class ForwardingCache implements Cache {

    private final Cache cache;

    public ForwardingCache(Cache cache) {
        this.cache = cache;
    }

    public boolean isPresent(String key) {
        return cache.isPresent(key);
    }

    public void put(String key, Object object) {
        cache.put(key, object);
    }

    public void put(String key, Object object, String duration) {
        cache.put(key, object, duration);
    }

    public void put(String key, Object object, long expirationInMillis) {
        cache.put(key, object, expirationInMillis);
    }

    public Object load(String key) {
        return cache.load(key);
    }

    public <T> T load(String key, Class<T> clazz) {
        return cache.load(key, clazz);
    }

    public void invalidate(String key) {
        cache.invalidate(key);
    }

    public void invalidateAll() {
        cache.invalidateAll();
    }

    public int size() {
        return cache.size();
    }

    public void cleanUp() {
        cache.cleanUp();
    }
}
