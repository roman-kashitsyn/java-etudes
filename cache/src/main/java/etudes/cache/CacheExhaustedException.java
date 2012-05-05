package com.wiley.cache;

/**
 * CacheExhaustedException raises when client tries to put entry into full cache.
 */
public class CacheExhaustedException extends RuntimeException {
    
    public CacheExhaustedException() {}
    
    public CacheExhaustedException(String message) {
        super(message);
    }

    public CacheExhaustedException(String message, Throwable cause) {
        super(message, cause);
    }
}
