package etudes.nanoweb.routes;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import etudes.nanoweb.Handler;
import etudes.nanoweb.HttpMethod;
import etudes.nanoweb.HttpRequest;
import etudes.nanoweb.ResourceHandler;
import etudes.nanoweb.annotation.WebHandler;
import org.reflections.Reflections;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;

/**
 * Routes module.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class Routes {

    private Routes() {}
    
    public static Route any(final Handler handler) {
        return new AbstractRoute(handler) {
            public boolean apply(HttpRequest httpRequest) {
                return true;
            }
        };
    }
    
    public static Route staticRoute(final String prefix, final String documentRoot) {
        return new AbstractRoute(ResourceHandler.onDocumentRoot(prefix, documentRoot)) {
            public boolean apply(HttpRequest httpRequest) {
                return httpRequest.getPath().startsWith(prefix);
            }
        };
    }
    
    public static Route exactPath(final HttpMethod method, final String path, final Handler handler) {
        return new AbstractRoute(handler) {
            public boolean apply(HttpRequest httpRequest) {
                return httpRequest.getMethod() == method && path.equalsIgnoreCase(httpRequest.getPath());
            }
        };
    }
    
    public static Route byPredicate(final Predicate<HttpRequest> predicate, final Handler handler) {
        return new AbstractRoute(handler) {
            public boolean apply(HttpRequest input) {
                return predicate.apply(input);
            }
        };
    }
    
    public static Router scan(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> declaredHandlers = reflections.getTypesAnnotatedWith(WebHandler.class);
        Collection<Route> routes = Lists.newArrayListWithExpectedSize(declaredHandlers.size());
        for (Class<?> clazz : declaredHandlers) {
            WebHandler annotation = clazz.getAnnotation(WebHandler.class);
            Handler handler = makeHandler(clazz);
            routes.add(Routes.exactPath(annotation.method(), annotation.path(), handler));
        }
        return Router.useRoutes(routes);
    }
    
    private static Handler makeHandler(Class<?> clazz) {
        if (!Handler.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(
                    clazz.getName() + " does not implements interface " + Handler.class.getName());
        }
        try {
            return (Handler) clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unable to create web handler", e);
        }
    }
}
