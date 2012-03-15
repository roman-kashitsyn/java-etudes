package etudes.nanoweb;

/**
 * Handler for http request.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public interface Handler {

    void serve(HttpRequest httpRequest, HttpResponse httpResponse);

}
