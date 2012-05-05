package com.wiley.cache;

/**
 * Handles replacement in {@link com.wiley.cache.impl.MultiLevelCache} implementation.
 */
public interface ReplacementListener {
    void onReplacement(Cache.Entry replacedEntry);
}
