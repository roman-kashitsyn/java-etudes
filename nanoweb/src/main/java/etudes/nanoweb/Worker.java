package etudes.nanoweb;

import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

import java.io.*;
import java.net.Socket;

/**
 * Simple HTTP worker.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class Worker implements Runnable {

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
            handler.serve(request, response);
        } catch (Exception e) {
            if (response != null) {
                response.setStatusCode(HttpResponse.ResponseStatus.INTERNAL_SERVER_ERROR);
            }
            e.printStackTrace();
        }
    }

    private HttpRequest makeRequest() throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(clientSocket.getInputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(bufferedInputStream));
        StringBuilder requestBuilder = new StringBuilder();
        while (bufferedInputStream.available() > 0) {
            requestBuilder.append(in.readLine());
        }
        return new HttpRequestParser(requestBuilder.toString());
    }

    private HttpResponse makeResponse() throws IOException {
        return new HttpResponseImpl(clientSocket.getOutputStream());
    }
}
