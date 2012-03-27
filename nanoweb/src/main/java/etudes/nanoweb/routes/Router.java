package etudes.nanoweb.routes;

import com.google.common.collect.Lists;
import etudes.nanoweb.Handler;
import etudes.nanoweb.HttpRequest;
import etudes.nanoweb.HttpResponse;

import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * HTTP Router.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class Router implements Handler {
    private static final Logger LOG = Logger.getLogger("etudes.nanoweb.server.router");
    private final Collection<Route> routes;
    
    private Router(Collection<Route> routes) {
        this.routes = routes;
    }
    
    public static Router useRoutes(Route... routes) {
        Collection<Route> newRoutes = Lists.newArrayListWithCapacity(routes.length);
        newRoutes.addAll(Arrays.asList(routes));
        return new Router(newRoutes);
    }
    
    public static Router useRoutes(Collection<Route> routes) {
        return new Router(routes);
    }
    
    public void serve(HttpRequest httpRequest, HttpResponse httpResponse) {
        for (Route route : routes) {
            if (route.apply(httpRequest)) {
                if (LOG.isLoggable(Level.INFO)) {
                    LOG.info("ROUTE " + httpRequest.getPath() + " -> " + route.getHandler().getClass());
                }
                route.getHandler().serve(httpRequest, httpResponse);
                return;
            }
        }
        httpResponse.setStatus(HttpResponse.ResponseStatus.NOT_FOUND);
    }

    protected Collection<Route> getRoutes() {
        return routes;
    }
    
    public void addRoute(Route route) {
        routes.add(route);
    }
}
