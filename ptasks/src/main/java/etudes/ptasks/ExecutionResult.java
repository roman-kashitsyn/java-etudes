package etudes.ptasks;

/**
 * Represents execution result of a task.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class ExecutionResult {

    public static enum Status {
        SUCCESS, FAILED
    }

    private final Status status;
    private final long timeSpent;
    private Exception exception;

    public ExecutionResult(long timeSpent, Exception exception) {
        this.status = exception == null ? Status.SUCCESS : Status.FAILED;
        this.timeSpent = timeSpent;
        this.exception = exception;
    }

    public Status getStatus() {
        return status;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public Exception getException() {
        return exception;
    }
}
