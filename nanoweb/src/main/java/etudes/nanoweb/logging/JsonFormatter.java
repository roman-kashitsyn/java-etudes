package etudes.nanoweb.logging;

import com.google.gson.Gson;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class JsonFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        Gson gson = new Gson();
        return gson.toJson(record);
    }
}
