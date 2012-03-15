package etudes.wget;

import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Main class for file download utility.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class WGet {
    
    private static final int PROGRESS_BAR_WIDTH = 50;
    
    public static void main(String... args) throws Exception {
        if (args.length < 1 || args.length > 2) {
            showHelp();
        }
        URL url = new URL(args[0]);
        String fileName = Util.shortUrlFileName(url.getFile());
        if (args.length == 2) {
            fileName = args[1];
        }

        BlockingQueue<ProgressInfo> statuses = new ArrayBlockingQueue<ProgressInfo>(100);
        Thread downloadThread = new Thread(new Downloader(fileName, url.openConnection(), statuses));
        downloadThread.start();
        
        ProgressInfo info;
        ProgressBar progressBar = ProgressBar.ofWidth(PROGRESS_BAR_WIDTH);
        do {
            info = statuses.take();
            System.err.print('\r');
            int percentsDownloaded = info.getPercentsDownloaded();
            System.err.print(progressBar.render(percentsDownloaded));
            System.err.print(String.format(" %3d%%", percentsDownloaded));

        } while (info.getStatus() == ProgressInfo.Status.SUCCESS);
        System.err.println();
        downloadThread.join();
        System.err.println(info.getStatus().toString());
    }
    
    public static void showHelp() {
        System.err.println("wget URL [path]");
        System.err.println("URL  - download from");
        System.err.println("path - download to");
    }
}
