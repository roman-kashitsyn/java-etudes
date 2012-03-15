package etudes.nanoweb;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public interface HttpRequest {
    
    HttpMethod getMethod();

    URI getRequestURI();

    String getPath();
    
    void putParameter(String name, String value);
    
    String getFragment();
    
    String getParameter(String name);
    
    Map<String, String> getParameters();
    
    Map<String, String> getHeaders();
    
    String getBody();
}
