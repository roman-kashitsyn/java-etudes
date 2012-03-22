package etudes.nanoweb;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Simple HTTP worker.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class Worker implements Runnable {
    
    private static final Logger LOG  = Logger.getLogger(Worker.class.getName());

    private final Socket clientSocket;
    private final Handler handler;

    public Worker(Socket socket, Handler handler) {
        this.clientSocket = socket;
        this.handler = handler;
    }

    public void run() {
        HttpRequest request = null;
        HttpResponse response = null;
        try {
            request = makeRequest();
            response = makeResponse();
            logConnection(request);
            handler.serve(request, response);
            logResponse(response);
        } catch (Exception e) {
            if (response != null) {
                response.setStatus(HttpResponse.ResponseStatus.INTERNAL_SERVER_ERROR);
            }
            e.printStackTrace();
        }
    }

    private HttpRequest makeRequest() throws IOException {
        InputStream is = clientSocket.getInputStream();
        int bufSize = 8192;
        byte[] buf = new byte[bufSize];
        int len = is.read(buf, 0, bufSize);
        ByteArrayInputStream baos = new ByteArrayInputStream(buf, 0, len);

        BufferedReader in = new BufferedReader(new InputStreamReader(baos));
        StringBuilder requestBuilder = new StringBuilder();
        String nextLine;
        while ((nextLine = in.readLine()) != null) {
            requestBuilder.append(nextLine).append(Utils.CRLF);
        }
        //clientSocket.shutdownInput();
        return new HttpRequestImpl(requestBuilder.toString());
    }

    private HttpResponse makeResponse() throws IOException {
        return new HttpResponseImpl(clientSocket.getOutputStream());
    }

    private void logConnection(HttpRequest request) {
        LOG.info("REQUEST FROM " + clientSocket.getRemoteSocketAddress() +
                " " + request.getMethod() + " " + request.getRequestURI());
    }

    private void logResponse(HttpResponse response) {
        LOG.info("RESPONSE TO " + clientSocket.getRemoteSocketAddress() + " STATUS " + response.getStatus());
    }
}
