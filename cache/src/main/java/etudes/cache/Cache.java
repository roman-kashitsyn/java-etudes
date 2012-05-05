package com.wiley.cache;

import java.io.Serializable;

/**
 * Cache abstraction.
 */
public interface Cache {

    public interface Entry extends Serializable {
        long expiresAt();
        void updateLastAccessTime();
        long accessCount();
        long lastAccessed();
        String key();
        Object value();
    }

    /**
     * Returns true if object is present in cache and not expired. Invocation of
     * the method does not affect cache statistic.
     * @param key key
     * @return true if object is present in cache and not expired
     */
    boolean isPresent(String key);

    /**
     * Puts object to cache with given key and default expiration time.
     * @param key key
     * @param object value to store
     * @throws IllegalArgumentException if either key or value is null
     */
    void put(String key, Object object);

    /**
     * Puts object to cache with given key and expiration time.
     * @param key key
     * @param object value to store
     * @param duration cached object expiration time in format accepted by {@link com.wiley.cache.impl.TimeDuration}.
     * @throws IllegalArgumentException if either key or value is null
     */
    void put(String key, Object object, String duration);

    /**
     * Puts object to cache with given key and expiration time.
     * @param key key
     * @param object value to store
     * @param expirationInMillis cached object expiration time in milliseconds
     * @throws IllegalArgumentException if either key or value is null
     */
    void put(String key, Object object, long expirationInMillis);

    /**
     * Loads object by key or returns null if no object present.
     * @param key key
     * @return loaded instance or null
     */
    Object load(String key);

    /**
     * Loads object of given type by key or null if object with given type does not present.
     * @param key key
     * @param clazz required object class
     * @param <T> type
     * @return loaded instance or null
     * @throws ClassCastException if loaded object has incompatible type
     */
    <T> T load(String key, Class<T> clazz);

    /**
     * Removes object from cache.
     * @param key key
     */
    void invalidate(String key);

    /**
     * Clear cache.
     */
    void invalidateAll();

    /**
     * Returns approximately number of objects in cache.
     * @return cache size
     */
    int size();

    /**
     * Performs maintenance operations.
     */
    void cleanUp();
}
