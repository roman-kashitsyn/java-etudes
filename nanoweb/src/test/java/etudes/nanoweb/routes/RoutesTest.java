package etudes.nanoweb.routes;

import etudes.nanoweb.Handler;
import etudes.nanoweb.app.handlers.HelloHandler;

import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link Routes}.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class RoutesTest {
    @Test
    public void testRouterScan() {
        Router router = Routes.scan("etudes.nanoweb.app.handlers");
        assertNotNull(getRouteByClass(router.getRoutes(), HelloHandler.class));
    }
    
    protected Handler getRouteByClass(Collection<Route> routes, Class<? extends Handler> handlerClass) {
        for (Route route : routes) {
            Handler handler = route.getHandler();
            if (handlerClass.isAssignableFrom(handler.getClass())) {
                return handler;
            }
        }
        return null;
    }
}
