package etudes.nanoweb;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * HTTP server implementation.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class HttpServer implements Runnable {
    
    public static final String NAME = "NanoWebServer 0.1";
    
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
}
