package etudes.nanoweb.logging;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Buffered log handler for server log.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class BufferedLogHandler extends Handler {
    
    private final NavigableMap<Long, LogRecord> logMap = new ConcurrentSkipListMap<Long, LogRecord>();

    public Collection<LogRecord> recordsAfter(long millisFromEpoch) {
        return logMap.tailMap(millisFromEpoch).values();
    }

    public Collection<LogRecord> allRecords() {
        return logMap.values();
    }

    @Override
    public void publish(LogRecord record) {
        logMap.put(record.getMillis(), record);
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
        logMap.clear();
    }
}
