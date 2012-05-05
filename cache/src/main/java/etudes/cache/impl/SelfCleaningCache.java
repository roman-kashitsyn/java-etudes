package com.wiley.cache.impl;

import com.wiley.cache.Cache;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

/**
 * Cache implementation that performs auto-cleanup actions periodically
 * and delegates cache calls to other implementation. Underlying cache
 * implementation must be thread-safe.
 */
public class SelfCleaningCache extends ForwardingCache {
    
    private static final Logger LOG = Logger.getLogger(SelfCleaningCache.class.getName());
    private final ScheduledExecutorService executorService;
    
    public SelfCleaningCache(final Cache cache, String duration) {
        super(cache);
        TimeDuration timeDuration = TimeDuration.parse(duration);
        executorService = Executors.newScheduledThreadPool(1);
        LOG.info("Scheduling cache cleanUp to every " + duration);
        executorService.scheduleWithFixedDelay(
                new Runnable() {
                    public void run() {
                        cache.cleanUp();
                    }
                },
                0,
                (long) timeDuration.duration(),
                timeDuration.unit()
        );
    }

    protected void finalize() throws Throwable {
        super.finalize();
        LOG.info("Stopping cache cleanUp");
        executorService.shutdown();
    }
}
