package com.wiley.cache.impl;

import com.wiley.cache.Cache;
import com.wiley.cache.CacheExhaustedException;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import static com.wiley.cache.impl.Util.checkNotNull;
import static com.wiley.cache.impl.Util.checkArgument;

public abstract class AbstractCache implements Cache {

    private static final Logger LOG = Logger.getLogger(AbstractCache.class.getName());

    private long defaultExpiration;
    private int maxSize;

    static protected class CacheEntry implements Entry, Comparable<Entry> {
        private static final long serialVersionUID = 1L;
        private final Object value;
        private final String key;
        private final long expiresAt;
        private final AtomicLong accessCount;
        private final AtomicLong lastAccess;

        
        private CacheEntry(String key, Object value, long lastAccess, long expiresAt) {
            this.value = value;
            this.key =  key;
            this.lastAccess = new AtomicLong(lastAccess);
            this.expiresAt = expiresAt;
            this.accessCount = new AtomicLong(1);
        }
        
        public static CacheEntry of(String key, Object value) {
            return new CacheEntry(key, value, System.currentTimeMillis(), Long.MAX_VALUE);
        }
        
        public static CacheEntry of(String key, Object value, long expirationInMillis) {
            long currentTime = System.currentTimeMillis();
            return new CacheEntry(key, value, currentTime, currentTime + expirationInMillis);
        }
        
        public String key() {
            return key;
        }

        public Object value() {
            return value;
        }

        public long expiresAt() {
            return expiresAt;
        }
        
        public long accessCount() {
            return accessCount.get();
        }

        public long lastAccessed() {
            return lastAccess.longValue();
        }

        public void updateLastAccessTime() {
            accessCount.incrementAndGet();
            lastAccess.set(System.currentTimeMillis());
        }
        
        public int compareTo(Entry entry) {
            return (int)(expiresAt - entry.expiresAt());
        }
    }

    protected AbstractCache(CacheBuilder builder) {
        setDefaultExpiration(builder.getDefaultExpirationInMillis());
    }

    public boolean isPresent(String key) {
        Entry entry = loadEntry(key);
        return entry != null && !isExpire(entry);
    }

    public void put(String key, Object object) {
        put(key, object, defaultExpiration);
    }
    
    public void put(String key, Object object, String duration) {
        put(key, object, TimeDuration.parse(duration).toMilliseconds());
    }
    
    public void put(String key, Object object, long expirationInMillis) {
        checkNotNull(key);
        checkNotNull(object);
        checkArgument(expirationInMillis > 0, "Expiration time must be positive");
        if (!hasSpace()) {
            LOG.severe("Cache is exhausted, max size is " + maxSize);
            throw new CacheExhaustedException("Cache space is exhausted, size limit is " + maxSize);
        }
        CacheEntry newEntry = CacheEntry.of(key, object, expirationInMillis);
        saveEntry(newEntry);
    }
    
    public Object load(String key) {
        return getObjectIfNotExpired(loadEntry(key));
    }

    protected abstract Entry loadEntry(String key);
    
    protected abstract void saveEntry(Entry entry);

    @SuppressWarnings("unchecked")
    public <T> T load(String key, Class<T> clazz) {
        return (T) load(key);
    }
    
    protected void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    protected void setDefaultExpiration(long expiration) {
        this.defaultExpiration = expiration;
    }
    
    public int getMaxSize() {
        return maxSize;
    }

    protected boolean hasSpace() {
        return size() < maxSize;
    }

    public long getDefaultExpiration() {
        return defaultExpiration;
    }

    protected static Object getObjectIfNotExpired(Entry entry) {
        if (entry != null && entry.expiresAt() >= System.currentTimeMillis()) {
            entry.updateLastAccessTime();
            return entry.value();
        }
        return null;
    }

    protected static boolean isExpire(Entry entry) {
        return entry.expiresAt() < System.currentTimeMillis();
    }

}
