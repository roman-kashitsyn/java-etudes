package etudes.nanoweb;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link HttpRequestParser}.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class HttpRequestParserTest {

    @Test
    public void testOneLineRequest() {
        HttpRequestParser httpRequestParser = new HttpRequestParser("GET /some/path HTTP/1.0");
        assertEquals(HttpMethod.GET, httpRequestParser.getMethod());
        assertEquals("/some/path", httpRequestParser.getPath());
        assertEquals("HTTP/1.0", httpRequestParser.getVersion());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRequest() {
        HttpRequestParser httpRequestParser = new HttpRequestParser("GET /some/path");
    }

    @Test
    public void testSomeHeaders() {
        HttpRequestParser httpRequestParser =
                new HttpRequestParser("DELETE /some/path HTTP/1.0\n\rUser-Agent: Browser\n\rFrom: localhost");
        Map<String,String> headers = httpRequestParser.getHeaders();
        assertEquals(HttpMethod.DELETE, httpRequestParser.getMethod());
        assertEquals("Browser", headers.get("User-Agent"));
        assertEquals("localhost", headers.get("From"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequestWithIncompleteHeader() {
        HttpRequestParser httpRequestParser =
                new HttpRequestParser("GET /path HTTP/1.0\n\rUser-Agent");
    }

    @Test
    public void testRequestWithBody() {
        HttpRequestParser httpRequestParser =
                new HttpRequestParser("POST /some/path HTTP/1.0\n\rUser-Agent: firefox\n\r\n\rSome\n\rBody");
        assertEquals(HttpMethod.POST, httpRequestParser.getMethod());
        assertEquals("Some\n\rBody\n\r", httpRequestParser.getBody());
    }

    @Test
    public void testRequestWithParams() {
        HttpRequestParser httpRequestParser =
                new HttpRequestParser("GET /path?param1=value1&param2=value2 HTTP/1.1");
        Map<String, String> parameters = httpRequestParser.getParameters();
        assertEquals(2, parameters.size());
        assertEquals("value1", parameters.get("param1"));
        assertEquals("value2", parameters.get("param2"));
    }

    @Test
    public void testRequestWithFragment() {
        HttpRequestParser httpRequestParser =
                new HttpRequestParser("GET /path?param=value#fragment HTTP/1.1");
        Map<String, String> parameters = httpRequestParser.getParameters();
        assertEquals(1, parameters.size());
        assertEquals("value", parameters.get("param"));
        assertEquals("fragment", httpRequestParser.getFragment());
    }

}
