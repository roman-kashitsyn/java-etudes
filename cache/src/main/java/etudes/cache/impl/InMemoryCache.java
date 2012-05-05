package com.wiley.cache.impl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * In-memory cache implementation. Thread-safe (I hope).
 */
public class InMemoryCache extends AbstractCache {

    private final Map<String, Entry> cache = new ConcurrentHashMap<String, Entry>();
    private final SortedSet<Entry> replacementCandidates;
    private final PriorityQueue<Entry> expireCandidates = new PriorityQueue<Entry>();
    private final Object lock = new Object();

    protected InMemoryCache(CacheBuilder cacheBuilder) {
        super(cacheBuilder);
        setMaxSize(cacheBuilder.getInMemoryCacheMaxSize());
        replacementCandidates = new TreeSet<Entry>(cacheBuilder.getReplacementStrategy());
    }

    public Object load(String key) {
        return getObjectIfNotExpired(cache.get(key));
    }

    @Override
    protected Entry loadEntry(String key) {
        return cache.get(key);
    }

    @Override
    protected void saveEntry(Entry entry) {
        cache.put(entry.key(), entry);
        synchronized (lock) {
            expireCandidates.offer(entry);
            replacementCandidates.add(entry);
        }
    }

    protected Entry entryToReplace() {
        return replacementCandidates.isEmpty() ? null : replacementCandidates.first();
    }

    public void invalidate(String key) {
        Entry entry = cache.remove(key);
        synchronized (lock) {
            expireCandidates.remove(entry);
            replacementCandidates.remove(entry);
        }
    }

    public void invalidateAll() {
        cache.clear();
        synchronized (lock) {
            expireCandidates.clear();
            replacementCandidates.clear();
        }
    }
    
    public void cleanUp() {
        long now = System.currentTimeMillis();
        synchronized (lock) {
            Entry candidate = expireCandidates.peek();
            Entry actualEntry;
            while (candidate != null && candidate.expiresAt() <= now) {
                candidate = expireCandidates.poll();
                replacementCandidates.remove(candidate);

                // entry can be changed during cleanUp
                actualEntry = cache.get(candidate.key());
                if (actualEntry.expiresAt() <= now) {
                    cache.remove(candidate.key());
                }
                candidate = expireCandidates.peek();
            }
        }
    }

    public int size() {
        return cache.size();
    }
}
