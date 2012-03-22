package etudes.nanoweb.app.handlers;

import etudes.nanoweb.Handler;
import etudes.nanoweb.HttpRequest;
import etudes.nanoweb.HttpResponse;
import etudes.nanoweb.annotation.WebHandler;

import java.io.PrintWriter;

/**
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
@WebHandler(path="/hello")
public class HelloHandler implements Handler {

    public void serve(HttpRequest httpRequest, HttpResponse httpResponse) {
        String userName = httpRequest.getParameter("user");
        String message;
        if (userName == null) {
            message = "Hello, World!";
        } else {
            message = "Hello, " + userName;
        }
        PrintWriter writer = new PrintWriter(httpResponse.getOutputStream());
        writer.write("<h1>" + message + "</h1>");
        writer.close();
    }
}
