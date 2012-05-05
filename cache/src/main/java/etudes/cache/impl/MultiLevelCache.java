package com.wiley.cache.impl;

import com.wiley.cache.Cache;
import com.wiley.cache.CacheExhaustedException;
import com.wiley.cache.ReplacementListener;

import java.util.ArrayList;
import java.util.List;

import static com.wiley.cache.impl.Util.checkArgument;
import static com.wiley.cache.impl.Util.checkNotNull;
import static com.wiley.cache.impl.Util.checkSerializable;

/**
 * 2-level cache implementation. All stored keys and objects must be serializable.
 * Not thread-safe.
 */
public class MultiLevelCache implements Cache {

    private final InMemoryCache memoryCache;
    private final FileCache fileCache;
    private List<ReplacementListener> replacementListeners = new ArrayList<ReplacementListener>();

    protected MultiLevelCache(CacheBuilder cacheBuilder) {
        memoryCache = new InMemoryCache(cacheBuilder);
        fileCache = new FileCache(cacheBuilder);
    }

    public boolean isPresent(String key) {
        return memoryCache.isPresent(key) || fileCache.isPresent(key);
    }

    public void put(String key, Object object) {
        put(key, object, memoryCache.getDefaultExpiration());
    }

    public void put(String key, Object object, String duration) {
        put(key, object, TimeDuration.parse(duration).toMilliseconds());
    }

    public void put(String key, Object object, long expirationInMillis) {
        checkNotNull(key);
        checkNotNull(object);
        checkArgument(expirationInMillis > 0, "Expiration time must be positive");
        checkSerializable(object);

        if (memoryCache.hasSpace()) {
            memoryCache.put(key, object, expirationInMillis);
        } else if (fileCache.hasSpace()) {
            Entry entryToReplace = memoryCache.entryToReplace();
            memoryCache.invalidate(entryToReplace.key());
            memoryCache.put(key, object, expirationInMillis);
            fileCache.saveEntry(entryToReplace);
            fireReplacement(entryToReplace);
        } else {
            throw new CacheExhaustedException(
                    String.format("Both in memory cache (maxSize=%d) and file cache (maxSize=%d) are exhausted",
                            memoryCache.getMaxSize(), fileCache.getMaxSize()));
        }
    }

    public Object load(String key) {
        Object result = memoryCache.load(key);
        return result != null ? result :  fileCache.load(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T load(String key, Class<T> clazz) {
        return (T) load(key);
    }

    public void invalidate(String key) {
        memoryCache.invalidate(key);
        if (fileCache.isPresent(key)) {
            fileCache.invalidate(key);
        }
    }

    public void invalidateAll() {
        memoryCache.invalidateAll();
        fileCache.invalidateAll();
    }

    public int size() {
        return memoryCache.size() + fileCache.size();
    }

    public void cleanUp() {
        memoryCache.cleanUp();
        fileCache.cleanUp();
    }

    private void fireReplacement(Entry entryToReplace) {
        for (ReplacementListener listener : replacementListeners) {
            listener.onReplacement(entryToReplace);
        }
    }

    public void addReplacementListener(ReplacementListener listener) {
        checkNotNull(listener);
        replacementListeners.add(listener);
    }

    public void removeReplacementListener(ReplacementListener listener) {
        checkNotNull(listener);
        replacementListeners.remove(listener);
    }
}
