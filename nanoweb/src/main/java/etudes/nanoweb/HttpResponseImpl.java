package etudes.nanoweb;

import com.google.common.io.Closeables;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP response implementation.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
class HttpResponseImpl implements HttpResponse {
    
    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private Map<String, String> headers = new HashMap<String, String>();
    private final OutputStream realOutput;
    private ResponseStatus statusCode = ResponseStatus.OK;
    private boolean closed = false;

    public HttpResponseImpl(OutputStream realOutput) {
        this.realOutput = realOutput;
    }

    public void setStatus(ResponseStatus status) {
        this.statusCode = status;
    }

    public ResponseStatus getStatus() {
        return this.statusCode;
    }

    public void setContentType(String mime) {
        setHeader("Content-Type", mime);
    }

    public void setLastModified(Date lastModified) {
        // TODO: GMT format
        setHeader("Last-Modified", String.valueOf(lastModified.getTime()));
    }
    
    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public OutputStream getOutputStream() {
        return buffer;
    }
    
    public void done() {
        if (closed) {
            throw new IllegalStateException("Http response has already been rendered!");
        }
        try {
            OutputStream bufferedOutput = new BufferedOutputStream(realOutput);
            PrintWriter printWriter = new PrintWriter(bufferedOutput);
            writeResponseLine(printWriter);
            writeHeaders(printWriter);
            printWriter.flush();
            bufferedOutput.write(buffer.toByteArray());
            bufferedOutput.flush();
            printWriter.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            closed = true;
            Closeables.closeQuietly(realOutput);
        }
    }

    private void writeResponseLine(PrintWriter writer) {
        writer.write("HTTP/1.0 ");
        writer.write(statusCode.getCode());
        writer.write(" ");
        writer.write(statusCode.getName());
        writer.write(Utils.CRLF);
    }

    private void writeHeaders(PrintWriter writer) {
        putHeaderIfAbsent(Utils.CONTENT_LENGTH, String.valueOf(buffer.size()));
        putHeaderIfAbsent(Utils.CONTENT_TYPE, Utils.DEFAULT_CONTENT_TYPE);
        for (Map.Entry<String, String> header : headers.entrySet()) {
            writer.write(header.getKey());
            writer.write(": ");
            writer.write(header.getValue());
            writer.write(Utils.CRLF);
        }
        writer.write(Utils.CRLF);
    }
    
    private void putHeaderIfAbsent(String header, String value) {
        if (!headers.containsKey(header)) {
            headers.put(header, value);
        }
    }
}
