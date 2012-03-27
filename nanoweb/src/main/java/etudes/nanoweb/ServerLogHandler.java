package etudes.nanoweb;

import com.google.gson.Gson;
import etudes.nanoweb.logging.BufferedLogHandler;
import etudes.nanoweb.logging.JsonFormatter;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Web handler to work with server logs.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class ServerLogHandler implements Handler {

    private final BufferedLogHandler logHandler = new BufferedLogHandler();

    private ServerLogHandler(String pack) {
        Logger.getLogger(pack).addHandler(logHandler);
    }
    
    public void serve(HttpRequest httpRequest, HttpResponse httpResponse) {
        String from = httpRequest.getParameter("from");
        if (from != null) {
            long millisFromEpoch = Long.valueOf(from);
            handleRecords(logHandler.recordsAfter(millisFromEpoch), httpResponse);
        } else {
            handleRecords(logHandler.allRecords(), httpResponse);
        }
    }
    
    private void handleRecords(Collection<LogRecord> records, HttpResponse response) {
        PrintWriter writer = new PrintWriter(response.getOutputStream());
        Gson gson = new Gson();
        writer.println(gson.toJson(records));
        writer.flush();
    }
    
    public static Handler onPackage(String pack) {
        checkNotNull(pack);
        return new ServerLogHandler(pack);
    }
}
