package etudes.nanoweb.routes;

import etudes.nanoweb.Handler;

/**
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public abstract class AbstractRoute implements Route {

    private final Handler handler;

    public AbstractRoute(Handler handler) {
        this.handler = handler;
    }

    public Handler getHandler() {
        return handler;
    }
}
