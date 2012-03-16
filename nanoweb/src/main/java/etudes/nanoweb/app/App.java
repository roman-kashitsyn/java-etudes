package etudes.nanoweb.app;

import etudes.nanoweb.Handler;
import etudes.nanoweb.HttpServer;
import etudes.nanoweb.ResourceHandler;

/**
 * Sample app with our server.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class App {
    
    public static void main(String... args) {
        Handler handler = ResourceHandler.onDocumentRoot("/home/roman/Documents/www");
        HttpServer server = new HttpServer(handler, 10000);
        server.start();
    }
}
