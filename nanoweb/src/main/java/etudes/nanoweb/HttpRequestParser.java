package etudes.nanoweb;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Parser for HTTP requests.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class HttpRequestParser implements HttpRequest {
    
    private HttpMethod method;
    private URI requestURI;
    private String version;
    private String body;
    private Map<String, String> headers = new HashMap<String, String>();
    private Map<String, String> parameters = new HashMap<String, String>();

    public HttpRequestParser(String requestAsString) {
        String[] lines = requestAsString.split("\r?\n");
        parseFirstLine(lines[0]);
        if (lines.length > 1) {
            int indexAfterLastHeader = parseHeaders(lines);
            makeBody(lines, indexAfterLastHeader);
        }
    }
    
    public HttpMethod getMethod() {
        return method;
    }
    
    public String getPath() {
        return requestURI.getPath();
    }

    public String getFragment() {
        return requestURI.getFragment();
    }
    
    public URI getRequestURI() {
        return requestURI;
    }
    
    public String getVersion() {
        return version;
    }
    
    public Map<String, String> getHeaders() {
        return headers;
    }
    
    public Map<String, String> getParameters() {
        return parameters;
    }
    
    public String getParameter(String name) {
        return name;
    }
    
    public void putParameter(String name, String value) {
        parameters.put(name, value);
    }
    
    public String getBody() {
        return body;
    }

    private void parseFirstLine(String firstLine) {
        String[] parts = firstLine.split("\\s");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid http request first line");
        }
        this.method = HttpMethod.valueOf(parts[0]);

        try {
            this.requestURI = new URI(parts[1]);
            String queryString = requestURI.getQuery();
            if (queryString != null) {
                parseParams(requestURI.getQuery());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid request URI string: " + parts[1], e);
        }
        this.version = parts[2];
    }
    
    private int parseHeaders(String[] lines) {
        int i = 1;
        while (i < lines.length) {
            String currentLine = lines[i];
            if (Strings.isNullOrEmpty(currentLine)) {
                break;
            }
            int indexOfColon = lines[i].indexOf(':');
            if (indexOfColon < 0) {
                throw new IllegalArgumentException("Invalid HTTP header: " + lines[i]);
            }
            headers.put(
                    currentLine.substring(0, indexOfColon),
                    currentLine.substring(indexOfColon + 1).trim());
            ++i;
        }
        return i + 1;
    }
    
    private void parseParams(String paramsLine) {
        parameters.putAll(Splitter.on('&').withKeyValueSeparator("=").split(paramsLine));
    }
    
    private void makeBody(String[] lines, int startWith) {
        if (startWith >= lines.length) {
            body = "";
        } else {
            StringBuilder bodyBuilder = new StringBuilder();
            for (int i = startWith; i < lines.length; ++i) {
                bodyBuilder.append(lines[i]);
                bodyBuilder.append(Constants.CRLF);
            }
            body = bodyBuilder.toString();
        }
    }
}
