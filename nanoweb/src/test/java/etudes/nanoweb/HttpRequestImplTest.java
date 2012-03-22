package etudes.nanoweb;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link HttpRequestImpl}.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class HttpRequestImplTest {

    @Test
    public void testOneLineRequest() {
        HttpRequestImpl httpRequestImpl = new HttpRequestImpl("GET /some/path HTTP/1.0");
        assertEquals(HttpMethod.GET, httpRequestImpl.getMethod());
        assertEquals("/some/path", httpRequestImpl.getPath());
        assertEquals("HTTP/1.0", httpRequestImpl.getVersion());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRequest() {
        HttpRequestImpl httpRequestImpl = new HttpRequestImpl("GET /some/path");
    }

    @Test
    public void testSomeHeaders() {
        HttpRequestImpl httpRequestImpl =
                new HttpRequestImpl("DELETE /some/path HTTP/1.0\n\rUser-Agent: Browser\n\rFrom: localhost");
        Map<String,String> headers = httpRequestImpl.getHeaders();
        assertEquals(HttpMethod.DELETE, httpRequestImpl.getMethod());
        assertEquals("Browser", headers.get("User-Agent"));
        assertEquals("localhost", headers.get("From"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequestWithIncompleteHeader() {
        HttpRequestImpl httpRequestImpl =
                new HttpRequestImpl("GET /path HTTP/1.0\n\rUser-Agent");
    }

    @Test
    public void testRequestWithBody() {
        HttpRequestImpl httpRequestImpl =
                new HttpRequestImpl("POST /some/path HTTP/1.0\n\rUser-Agent: firefox\n\r\n\rSome\n\rBody");
        assertEquals(HttpMethod.POST, httpRequestImpl.getMethod());
        assertEquals("Some\n\rBody\n\r", httpRequestImpl.getBody());
    }

    @Test
    public void testRequestWithParams() {
        HttpRequestImpl httpRequestImpl =
                new HttpRequestImpl("GET /path?param1=value1&param2=value2 HTTP/1.1");
        Map<String, String> parameters = httpRequestImpl.getParameters();
        assertEquals(2, parameters.size());
        assertEquals("value1", parameters.get("param1"));
        assertEquals("value2", parameters.get("param2"));
    }

    @Test
    public void testRequestWithFragment() {
        HttpRequestImpl httpRequestImpl =
                new HttpRequestImpl("GET /path?param=value#fragment HTTP/1.1");
        Map<String, String> parameters = httpRequestImpl.getParameters();
        assertEquals(1, parameters.size());
        assertEquals("value", parameters.get("param"));
        assertEquals("fragment", httpRequestImpl.getFragment());
    }

}
