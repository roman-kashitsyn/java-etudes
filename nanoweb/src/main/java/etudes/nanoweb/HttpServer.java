package etudes.nanoweb;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * HTTP server implementation.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class HttpServer {
    
    private final Handler handler;
    private final int port;
    private final ExecutorService executorService;
    
    public HttpServer(Handler handler, int port) {
        this.handler = handler;
        this.port = port;
        executorService = Executors.newCachedThreadPool();
    }
    
    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(new Worker(clientSocket, handler));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
