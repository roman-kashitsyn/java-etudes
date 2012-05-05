package com.wiley.cache.impl;

import com.wiley.cache.FileStorage;
import com.wiley.cache.FileStorageException;
import com.wiley.cache.StorageTraverser;

import java.io.*;

/**
 * Simple implementation for FileStorage abstraction.
 * Hashes keys with sha-1 algorithm and holds objects in git-like way to avoid max
 * files in directory limit and to speed-up files lookup.
 */
public class SimpleFileStorage implements FileStorage {
    
    private File root;
    
    private SimpleFileStorage(File root) {
        this.root = root;
    }
    
    public void traverse(StorageTraverser traverser) {
        File[] files = root.listFiles();
        for (File fileInRoot: files) {
            if (fileInRoot.isDirectory()) {
                traverseDir(fileInRoot, traverser);
            }
        }
    }
    
    private void traverseDir(File dir, StorageTraverser traverser) {
        for (File serializedEntry : dir.listFiles()) {
            if (serializedEntry.isFile()) {
                traverser.processFile(serializedEntry);
            }
        }
    }

    public OutputStream openFileToStore(String key) {
        File f = realFile(key);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            return new BufferedOutputStream(new FileOutputStream(f));
        } catch (Exception e) {
            throw new FileStorageException("Unable to open file to write key: " + key, e);
        }
        
    }

    public InputStream openFile(String key) {
        File f = realFile(key);
        if (!f.exists()) {
            throw new IllegalStateException("Trying to open non-existing file, key: " + key);
        }
        try {
            return new BufferedInputStream(new FileInputStream(f));
        } catch (Exception e) {
            throw new FileStorageException("Unable to open file for reading, key: " + key, e);
        }
    }

    public boolean exists(String key) {
        return realFile(key).exists();
    }

    public void delete(String key) {
        File file = realFile(key);
        if (file.exists() && !file.delete()) {
            throw new FileStorageException("Unable to delete file for key:" + key);
        }
    }

    public void clear() {
        Util.deleteDirectoryContents(root);
    }
    
    public static SimpleFileStorage onPath(String rootDirPath) {
        File rootDir = new File(rootDirPath);
        createDirectory(rootDir);
        return new SimpleFileStorage(rootDir);
    }

    protected File createDirectoryIfNeeded(String hash) {
        File dir = new File(root.getPath() + File.separator + hash.substring(0, 2));
        createDirectory(dir);
        return dir;
    }
    
    protected static void createDirectory(File dir) {
        if (!dir.exists() && !dir.mkdir()) {
            throw new FileStorageException("Unable to create folder " + dir.getPath());
        } else if (!dir.isDirectory()) {
            throw new FileStorageException(dir.getPath() + " exists and is not a directory");
        }
    }
    
        
    protected File realFile(String key) {
        String hash = Util.sha1(key); 
        File dir = createDirectoryIfNeeded(hash);
        return new File(dir.getPath() + File.separator + hash.substring(2));
    }
    
}
