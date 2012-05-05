package com.wiley.cache;

import java.io.File;

public interface StorageTraverser {

    /**
     * Processes file from storage.
     * @param file file from storage to process
     */
    void processFile(File file);
}
