package etudes.nanoweb;

import java.io.OutputStream;
import java.util.Date;

/**
 * Http response.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public interface HttpResponse {
    
    enum ResponseStatus {

        OK(200, "OK"),
        NOT_FOUND(404, "Not Found"),
        INTERNAL_SERVER_ERROR(500, "Internal Server Error");

        private int code;
        private String name;

        private ResponseStatus(int code, String name) {
            this.code = code;
            this.name = name;
        }
        
        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    public void setStatusCode(ResponseStatus status);

    public void setContentType(String mime);
    
    public void setLastModified(Date lastModified);
    
    public OutputStream getOutputStream();

    public void done();
}
