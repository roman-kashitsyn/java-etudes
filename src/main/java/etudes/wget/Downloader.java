package etudes.wget;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.concurrent.BlockingQueue;

/**
 * Performs main download job.
 */
public class Downloader implements Runnable {

    private final String fileName;
    private final URLConnection connection;
    private final BlockingQueue<ProgressStatus> progressQueue;

    public Downloader(String fileName,
                      URLConnection connection,
                      BlockingQueue<ProgressStatus> progressQueue) {
        this.fileName = fileName;
        this.connection = connection;
        this.progressQueue = progressQueue;
    }

    public void run() {
        FileOutputStream fileOut = null;
        InputStream inputStream = null;
        try {
            byte[] buffer = new byte[4096];
            fileOut = new FileOutputStream(fileName);
            int total = connection.getContentLength();
            int readSoFar = 0;
            inputStream = connection.getInputStream();
            while (true) {
                int read = inputStream.read(buffer);

                if (read > 0) {
                    fileOut.write(buffer, 0, read);
                    readSoFar += read;
                    progressQueue.offer(new ProgressStatus(fileName, readSoFar * 100 / total));
                } else {
                    break;
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            Util.closeQuiet(fileOut);
            Util.closeQuiet(inputStream);
        }
    }
    
}
