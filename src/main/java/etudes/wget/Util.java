package etudes.wget;

import java.io.Closeable;
import java.io.IOException;

/**
 * Utilities.
 */
public class Util {

    private Util() {}

    public static void closeQuiet(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
