package etudes.nanoweb.routes;

import com.google.common.base.Predicate;
import etudes.nanoweb.Handler;
import etudes.nanoweb.HttpMethod;
import etudes.nanoweb.HttpRequest;

/**
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
    
    public static Route exactPath(final HttpMethod method, final String path, final Handler handler) {
        return new AbstractRoute(handler) {
            public boolean apply(HttpRequest httpRequest) {
                return httpRequest.getMethod() == method && path.equalsIgnoreCase(httpRequest.getPath());
            }
        };
    }
    
    public static Route byPredicate(final Predicate<HttpRequest> predicate, final Handler handler) {
        return new AbstractRoute(handler) {
            @Override
            public boolean apply(HttpRequest input) {
                return predicate.apply(input);
            }
        };
    }
}
