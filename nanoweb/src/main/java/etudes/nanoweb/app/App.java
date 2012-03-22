package etudes.nanoweb.app;

import etudes.nanoweb.Handler;
import etudes.nanoweb.HttpServer;
import etudes.nanoweb.ResourceHandler;
import etudes.nanoweb.routes.Router;
import etudes.nanoweb.routes.Routes;

/**
 * Sample app with our server.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class App {
    
    public static void main(String... args) {
        Router appRouter = Routes.scan("etudes.nanoweb.app.handlers");
        appRouter.addRoute(Routes.staticRoute("/static", "/home/roman/Documents/www"));

        HttpServer server = new HttpServer(appRouter, 10000);
        server.start();
    }
}
