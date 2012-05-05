package com.wiley.cache.impl;

import com.wiley.cache.FileStorage;
import com.wiley.cache.SerializationException;
import com.wiley.cache.Serializer;
import com.wiley.cache.StorageTraverser;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.wiley.cache.impl.Util.checkSerializable;

/**
 * Cache implementation that use file system as storage. Both key and value classes MUST
 * be serializable. It is also requires memory proportional to cache size.
 * The cache survives recreation (with the same storage root) and even jvm restart.
 * This cache implementation is not thread-safe and should be synchronized manually.
 */
public class FileCache extends AbstractCache {

    private static final Logger LOG = Logger.getLogger(FileCache.class.toString());

    final Serializer serializer;
    final FileStorage fileStorage;
    final Map<String, Long> expirationCache = new HashMap<String, Long>();
    
    private class ExpirationCacheBuilder implements StorageTraverser {

        public void processFile(File file) {
            try {
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                Entry entry = (Entry) serializer.deserialize(inputStream);
                if (isExpire(entry)) {
                    if (LOG.isLoggable(Level.FINE)) {
                        LOG.fine("Deleting file with expired entry " + file.getPath());
                    }
                    if (!file.delete()) {
                        LOG.severe("Unable to delete file with expired entry " + file.getPath());
                    }
                } else {
                    expirationCache.put(entry.key(), entry.expiresAt());
                }
                expirationCache.put(entry.key(), entry.expiresAt());
            } catch (IOException ioe) {
                LOG.severe("Unable to load cache entry from file " + file.getPath());
            } catch (ClassCastException cce) {
                LOG.severe("File " + file.getPath() + " contains invalid cache entry");
            } catch (SerializationException se) {
                LOG.severe("Error occurred during cache entry deserialization from file " + file.getPath());
            }
        }
    }

    protected FileCache(CacheBuilder cacheBuilder) {
        super(cacheBuilder);
        this.fileStorage = SimpleFileStorage.onPath(cacheBuilder.getFsCacheRootDir());
        this.serializer = cacheBuilder.getSerializer();
        setMaxSize(cacheBuilder.getFileCacheMaxSize());
        setDefaultExpiration(cacheBuilder.getDefaultExpirationInMillis());
        loadDataFromStorage();
    }

    @Override
    protected void saveEntry(Entry entry) {
        checkSerializable(entry.value());
        expirationCache.put(entry.key(), entry.expiresAt());
        serializer.serialize(entry, fileStorage.openFileToStore(entry.key()));
    }

    @Override
    protected Entry loadEntry(String key) {
        if (!expirationCache.containsKey(key)) {
            return null;
        }
        return (Entry) serializer.deserialize(fileStorage.openFile(key));
    }

    public void invalidate(String key) {
        fileStorage.delete(key);
        expirationCache.remove(key);
    }

    public void invalidateAll() {
        fileStorage.clear();
        expirationCache.clear();
    }

    public int size() {
        return expirationCache.size();
    }

    public void cleanUp() {
        // suppose clean up would not take long time
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<String, Long>> it = expirationCache.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Long> entry = it.next();
            if (entry.getValue() < now) {
                invalidate(entry.getKey());
                it.remove();

                if (LOG.isLoggable(Level.FINER)) {
                    LOG.finer(String.format("Removing entry %s expired at %s", entry.getKey(), entry.getValue()));
                }
            }
        }
    }
    
    private void loadDataFromStorage() {
        fileStorage.traverse(new ExpirationCacheBuilder());
    }

}
