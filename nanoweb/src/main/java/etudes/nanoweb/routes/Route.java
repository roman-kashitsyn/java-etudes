package etudes.nanoweb.routes;

import etudes.nanoweb.Handler;
import etudes.nanoweb.HttpRequest;
import com.google.common.base.Predicate;

/**
 * Web route.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public interface Route extends Predicate<HttpRequest> {
    Handler getHandler();
}
