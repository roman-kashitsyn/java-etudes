package com.wiley.cache.impl;

import java.io.File;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Various utility methods.
 */
public class Util {

    private Util() {}

    /**
     * Encodes given text with sha-1 algorithm.
     * @param text text to encode
     * @return hex string containing sha-1 encoded text
     */
    public static String sha1(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return byteArrayToHexString(md.digest(text.getBytes()));
        }
        catch(NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to encode string", e);
        }
    }

    /**
     * Deletes all files and subdirectories of given directory.
     * @param dir directory to delete contents from
     * @throws IllegalArgumentException if file is not a directory
     */
    public static void deleteDirectoryContents(File dir) {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Given file is not a directory");
        }
        for (File f : dir.listFiles()) {
            deleteRecursively(f);
        }
    }

    /**
     * Deletes file or directory with all contents recursively.
     * @param f file or directory to delete
     */
    public static void deleteRecursively(File f) {
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                deleteRecursively(c);
        }
        f.delete();
    }

    /**
     * Checks that given object is not null.
     * @param value object to check
     */
    public static void checkNotNull(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("The argument can not be null.");
        }
    }

    /**
     * Checks that given conditions is true.
     * @param condition condition to check
     * @param message message for exception
     * @throws IllegalArgumentException if given condition is false
     */
    public static void checkArgument(boolean condition, Object message) {
        if (!condition) {
            throw new IllegalArgumentException(message.toString());
        }
    }

    /**
     * Checks that given object implements Serializable interface.
     * @param object object to check
     * @throws IllegalArgumentException if given object is not serializable
     */
    public static void checkSerializable(Object object) {
        if (!(object instanceof Serializable)) {
            throw new IllegalArgumentException("Argument must be Serializable, but it is not. Actual class is "
                    + object.getClass().getCanonicalName());
        }
    }

    private static String byteArrayToHexString(byte[] bytes) {
        String result = "";
        for (byte b : bytes) {
            result += Integer.toString((b & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }
}
