package etudes.wget;

/**
 * Download status.
 */
public class ProgressStatus {
    
    private final String fileName;
    private final int percentsDownloaded;
    
    public ProgressStatus(String fileName, int percentsDownloaded) {
        this.fileName = fileName;
        this.percentsDownloaded = percentsDownloaded;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public int getPercentsDownloaded() {
        return percentsDownloaded;
    }
}
