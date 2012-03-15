package etudes.nanoweb.routes;

import etudes.nanoweb.Handler;
import etudes.nanoweb.HttpRequest;
import etudes.nanoweb.HttpResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * HTTP Router.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class Router implements Handler {
    private static final Logger LOG = Logger.getLogger("Router");
    private final Collection<Route> routes = new ArrayList<Route>();
    
    public static Router useRoutes(Route... routes) {
        Router router = new Router();
        router.routes.addAll(Arrays.asList(routes));
        return router;
    }
    
    public void serve(HttpRequest httpRequest, HttpResponse httpResponse) {
        for (Route route : routes) {
            if (route.apply(httpRequest)) {
                if (LOG.isLoggable(Level.INFO)) {
                    LOG.info("Routing request " + httpRequest + " to route " + route);
                }
                route.getHandler().serve(httpRequest, httpResponse);
            }
        }
    }
    
    public void addRoute(Route route) {
        routes.add(route);
    }
}
