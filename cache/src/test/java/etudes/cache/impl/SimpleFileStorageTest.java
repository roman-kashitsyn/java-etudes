package com.wiley.cache.impl;

import com.wiley.cache.Serializer;
import com.wiley.cache.impl.BinarySerializer;
import com.wiley.cache.impl.SimpleFileStorage;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class SimpleFileStorageTest {
    
    private static final String testPath = System.getProperty("java.io.tmpdir") + File.separator + "storage";
    private SimpleFileStorage fileStorage = SimpleFileStorage.onPath(testPath);
    private Serializer serializer = new BinarySerializer();
    
    @Test
    public void testFileCreation() throws Exception {
        saveText("Hello", "World");
        assertTrue(fileStorage.exists("Hello"));
        assertEquals("World", loadText("Hello"));
    }

    @Test(expected = IllegalStateException.class)
    public void testNonExistingKey() {
        fileStorage.openFile("not-exists");
    }

    @Test
    public void testFileDeletion() throws Exception{
        saveText("Hello", "World");
        fileStorage.delete("Hello");
        assertFalse(fileStorage.exists("Hello"));
    }

    @Test
    public void testStorageClear() throws Exception{
        saveText("Lisp", "is a programmable programming language");
        fileStorage.clear();
        assertFalse(fileStorage.exists("Lisp"));
    }
    
    private void saveText(String key, String text) throws Exception {
        serializer.serialize(text, fileStorage.openFileToStore(key));
    }
    
    private String loadText(String key) throws Exception {
        return serializer.deserialize(fileStorage.openFile(key)).toString();
    }
}
