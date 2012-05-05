package com.wiley.cache.impl;

import com.wiley.cache.Cache;
import com.wiley.cache.Caches;
import com.wiley.cache.Serializer;

import java.io.File;
import java.util.Comparator;
import java.util.concurrent.ScheduledExecutorService;

import static com.wiley.cache.impl.Util.checkArgument;
import static com.wiley.cache.impl.Util.checkNotNull;

/**
 * Configures and constructs instances of cache.
 */
public class CacheBuilder {

    private long defaultExpirationInMillis = 1000 * 60 * 30; // 30min
    private int inMemoryCacheMaxSize = 10000;
    private int fileCacheMaxSize = 10000;
    private String fsCacheRootDir = System.getProperty("java.io.tmpdir") + File.separator + "fsCache";
    private Serializer serializer = new BinarySerializer();
    private Comparator<Cache.Entry> replacementStrategy = Caches.leastRecentlyUsedStrategy();

    public CacheBuilder setDefaultExpiration(String expiration) {
        this.defaultExpirationInMillis = toMillis(expiration);
        return this;
    }

    public CacheBuilder setReplacementStrategy(Comparator<Cache.Entry> replacementStrategy) {
        checkNotNull(replacementStrategy);
        this.replacementStrategy = replacementStrategy;
        return this;
    }

    public CacheBuilder setInMemoryCacheMaxSize(int maxSize) {
        checkArgument(maxSize > 1, "Max cache size must be greater than 1, have: " + maxSize);
        this.inMemoryCacheMaxSize = maxSize;
        return this;
    }

    public CacheBuilder setSerializer(Serializer serializer) {
        checkNotNull(serializer);
        this.serializer = serializer;
        return this;
    }

    public CacheBuilder setFileCacheMaxSize(int maxSize) {
        checkArgument(maxSize > 1, "Max cache size must be greater than 1, have: " + maxSize);
        this.fileCacheMaxSize = maxSize;
        return this;
    }
    
    public CacheBuilder setFsCacheRootDir(String fsCacheRootDir) {
        checkNotNull(fsCacheRootDir);
        this.fsCacheRootDir = fsCacheRootDir;
        return this;
    }

    public Comparator<Cache.Entry> getReplacementStrategy() {
        return replacementStrategy;
    }

    public long getDefaultExpirationInMillis() {
        return defaultExpirationInMillis;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public int getInMemoryCacheMaxSize() {
        return inMemoryCacheMaxSize;
    }

    public String getFsCacheRootDir() {
        return this.fsCacheRootDir;
    }

    public int getFileCacheMaxSize() {
        return fileCacheMaxSize;
    }

    public InMemoryCache newInMemoryCache() {
        return new InMemoryCache(this);
    }
    
    public MultiLevelCache newMultiLevelCache() {
        return new MultiLevelCache(this);
    }

    private static long toMillis(String duration) {
        return TimeDuration.parse(duration).toMilliseconds();
    }
}
