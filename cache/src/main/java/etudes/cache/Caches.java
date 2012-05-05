package com.wiley.cache;

import com.wiley.cache.impl.SelfCleaningCache;

import java.util.Comparator;
import java.util.Random;

/**
 * Utility methods to work with caches.
 */
public class Caches {
    
    private enum Strategies implements Comparator<Cache.Entry> {
        LeastRecentlyUsed {
            public int compare(Cache.Entry e1, Cache.Entry e2) {
                return (int) (e1.lastAccessed() - e2.lastAccessed());
            }
        },
        MostRecentlyUsed {
            public int compare(Cache.Entry e1, Cache.Entry e2) {
                return (int) (e2.lastAccessed() - e1.lastAccessed());
            }
        },
        LeastFrequentlyUsed {
            public int compare(Cache.Entry e1, Cache.Entry e2) {
                return (int) (e1.accessCount() - e2.accessCount());
            }
        },
        Random {
            Random random = new Random();
            public int compare(Cache.Entry e1, Cache.Entry e2) {
                return random.nextInt(3) - 1;
            }
        }
    }

    /**
     * Returns replacement strategy implementation that evicts entries
     * with least access time.
     * @return Least Recently Used strategy implementation
     */
    public static Comparator<Cache.Entry> leastRecentlyUsedStrategy() {
        return Strategies.LeastRecentlyUsed;
    }

    /**
     * Returns replacement strategy implementation that evicts entries
     * with maximal access time.
     * @return Most Recently Used strategy implementation
     */
    public static Comparator<Cache.Entry> mostRecentlyUsedStrategy() {
        return Strategies.MostRecentlyUsed;
    }

    /**
     * Returns replacement strategy implementation that evicts entries
     * with least access count.
     * @return Least Frequently Used strategy implementation
     */
    public static Comparator<Cache.Entry> leastFrequentlyUsedStrategy() {
        return Strategies.LeastFrequentlyUsed;
    }

    /**
     * Returns replacement strategy implementation that evicts random entries.
     * @return Random strategy implementation
     */
    public static Comparator<Cache.Entry> randomStrategy() {
        return Strategies.Random;
    }

    /**
     * Returns synchronized version of cache. Delegates all calls to underlying cache implementation.
     * @param cache underlying cache implementation
     * @return thread-safe cache implementation
     */
    public Cache synchronizedCache(final Cache cache) {
        return new Cache() {

            public synchronized boolean isPresent(String key) {
                return cache.isPresent(key);
            }

            public synchronized void put(String key, Object object) {
                cache.put(key, object);
            }

            public synchronized void put(String key, Object object, String duration) {
                cache.put(key, object, duration);
            }
            
            public synchronized void put(String key, Object object, long expirationInMillis) {
                cache.put(key, object, expirationInMillis);
            }

            public synchronized Object load(String key) {
                return cache.load(key);
            }

            public synchronized <T> T load(String key, Class<T> clazz) {
                return cache.load(key, clazz);
            }

            public synchronized void invalidate(String key) {
                cache.invalidate(key);
            }

            public synchronized void invalidateAll() {
                cache.invalidateAll();
            }

            public synchronized int size() {
                return cache.size();
            }

            public synchronized void cleanUp() {
                cache.cleanUp();
            }
        };
    }

    /**
     * Creates new cache instance that performs auto clean up by timer. Input cache must be
     * thread-safe since it cleanUp method will be invoked from another thread. Delegates
     * all calls to underlying cache implementation.
     * @param cache thread-safe cache to clean up
     * @param cleanUpInterval time duration
     * @return new auto-cleanup cache
     */
    public static Cache selfCleaningCache(final Cache cache, String cleanUpInterval) {
        return new SelfCleaningCache(cache, cleanUpInterval);
    }
}
