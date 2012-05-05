package com.wiley.cache;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Abstraction for file storage.
 */
public interface FileStorage {

    /**
     * Traverses the file storage and calls traverser for each file.
     * @param traverser file processor
     */
    void traverse(StorageTraverser traverser);

    /**
     * Opens file associated with the key for write.
     * @param key key for the file
     * @return output stream
     * @throws FileStorageException on IO error
     */
    OutputStream openFileToStore(String key);

    /**
     * Opens file associated with the key for read.
     * @param key key for the file
     * @return input stream
     * @throws FileStorageException on IO error
     */
    InputStream openFile(String key);

    /**
     * Checks file associated with the key exists.
     * @param key key
     * @return true if file associated with the key exists
     */
    boolean exists(String key);

    /**
     * Deletes file associated with the key.
     * @param key key for the file
     * @throws FileStorageException on IO error
     */
    void delete(String key);

    /**
     * Deletes all files from storage.
     */
    void clear();
    
}
