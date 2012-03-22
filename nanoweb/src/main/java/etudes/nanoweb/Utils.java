package etudes.nanoweb;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class Utils {

    private Utils() {}

    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String DEFAULT_CONTENT_TYPE = "text/html";
    public static final String SERVER = "Server";
    public static final String CRLF = "\r\n";

    public static void closeSocket(ServerSocket socket) {
        if (socket == null) {
            return;
        }
        try {
            socket.close();
        } catch (IOException ioe) {
            // do nothing
        }
    }
}
