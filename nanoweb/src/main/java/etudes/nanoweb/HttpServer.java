package etudes.nanoweb;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * HTTP server implementation.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class HttpServer implements Runnable {

    private static final Logger LOG = Logger.getLogger("etudes.nanoweb.server");
    private static final Properties props = new Properties();

    static {
        try{
            props.load(HttpServer.class.getResourceAsStream("/server.properties"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    private final Handler handler;
    private final int port;
    private final ExecutorService executorService;
    private volatile boolean running;
    
    public HttpServer(Handler handler, int port) {
        this.handler = handler;
        this.port = port;
        executorService = Executors.newCachedThreadPool();
    }

    public void run() {
        ServerSocket serverSocket = null;
        if (running) {
            return;
        }
        running = true;
        try {
            serverSocket = new ServerSocket(port);
            while (running) {
                handleRequests(serverSocket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utils.closeSocket(serverSocket);
        }
    }
    
    public void start() {
        if (!running) {
            executorService.submit(this);
        }
    }

    public void stop() {
        running = false;
    }

    private void handleRequests(ServerSocket serverSocket) {
        try {
            Socket clientSocket = serverSocket.accept();
            executorService.submit(new Worker(clientSocket, handler));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String getName() {
        return props.getProperty("server.name");
    }
    
    public static String getVersion() {
        return props.getProperty("server.version");
    }
}
