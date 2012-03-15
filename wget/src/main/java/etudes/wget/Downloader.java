package etudes.wget;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.concurrent.BlockingQueue;

/**
 * Performs main download job.
 *  @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class Downloader implements Runnable {

    private final String fileName;
    private final URLConnection connection;
    private final BlockingQueue<ProgressInfo> progressQueue;

    public Downloader(String fileName,
                      URLConnection connection,
                      BlockingQueue<ProgressInfo> progressQueue) {
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
            long total = connection.getContentLength();
            long readSoFar = 0;
            inputStream = connection.getInputStream();
            while (true) {
                int read = inputStream.read(buffer);

                if (read > 0) {
                    fileOut.write(buffer, 0, read);
                    readSoFar += read;
                    progressQueue.offer(
                            new ProgressInfo(fileName, ProgressInfo.Status.SUCCESS, (int) (readSoFar * 100 / total)));
                } else {
                    progressQueue.offer(new ProgressInfo(fileName, ProgressInfo.Status.DONE, 100));
                    break;
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            progressQueue.offer(new ProgressInfo(fileName, ProgressInfo.Status.FAIL, 0));
        } finally {
            Util.closeQuietly(fileOut);
            Util.closeQuietly(inputStream);
        }
    }
    
}
