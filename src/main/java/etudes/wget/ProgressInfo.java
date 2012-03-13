package etudes.wget;

/**
 * Download status.
 *  @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class ProgressInfo {

    public enum Status {
        SUCCESS, FAIL, DONE
    }

    private final String fileName;
    private final Status status;
    private final int percentsDownloaded;
    
    public ProgressInfo(String fileName, Status status, int percentsDownloaded) {
        this.fileName = fileName;
        this.status = status;
        this.percentsDownloaded = percentsDownloaded;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public int getPercentsDownloaded() {
        return percentsDownloaded;
    }

    public Status getStatus() {
        return status;
    }
}
