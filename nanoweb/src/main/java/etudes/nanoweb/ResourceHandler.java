package etudes.nanoweb;

import com.google.common.io.Closeables;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Simple handler for static resources.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class ResourceHandler implements Handler {
    
    private final File wwwRoot;
    private String prefix;

    private final String defaultFile = "index.html";
    
    private ResourceHandler(String prefix, File wwwRoot) {
        this.wwwRoot = wwwRoot;
        this.prefix = prefix;
    }

    public void serve(HttpRequest httpRequest, HttpResponse httpResponse) {
        String requestPath = httpRequest.getPath();
        if (prefix != null && requestPath.startsWith(prefix)) {
            requestPath = requestPath.substring(prefix.length());
        }
        File destination = new File(wwwRoot.getAbsolutePath() + "/" + requestPath);
        if (destination.exists() && destination.isDirectory()) {
            tryServe(new File(destination.getAbsolutePath() + "/" + defaultFile), httpResponse);
        } else {
            tryServe(destination, httpResponse);
        }
    }
    
    public void tryServe(File f, HttpResponse response) {
        if (!f.exists()) {
            response.setStatus(HttpResponse.ResponseStatus.NOT_FOUND);
        } else {
            OutputStream outputStream = null;
            try {
                outputStream = response.getOutputStream();
                Files.copy(f, outputStream);
            } catch (IOException ioe) {
                response.setStatus(HttpResponse.ResponseStatus.INTERNAL_SERVER_ERROR);
            } finally {
                Closeables.closeQuietly(outputStream);
            }
        }
    }
    
    public static Handler onDocumentRoot(String path) {
        return onDocumentRoot(null, path);
    }
    
    public static Handler onDocumentRoot(String prefix, String path) {
        File rootDir = new File(path);
        if (!rootDir.exists()) {
            throw new IllegalArgumentException("WWW directory " + path + " does not exist");
        }
        if (!rootDir.isDirectory()) {
            throw new IllegalArgumentException("Path " + path + " is not a directory");
        }
        return new ResourceHandler(prefix, rootDir);
    }
}
