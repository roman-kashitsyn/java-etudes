package etudes.wget;

import java.io.Closeable;
import java.io.IOException;

/**
 * Utilities.
 *  @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class Util {

    private Util() {}

    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public static void checkArgument(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
    
    public static String shortUrlFileName(String fullPath) {
        if (fullPath.contains("/")) {
            return fullPath.substring(fullPath.lastIndexOf('/') + 1);
        } else {
            return fullPath;
        }
    }
}
