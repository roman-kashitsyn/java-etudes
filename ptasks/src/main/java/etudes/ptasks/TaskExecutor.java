package etudes.ptasks;

/**
 * Task executor interface.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public interface TaskExecutor {

    /**
     * Executes task with respect to its dependencies.
     * @param task task to execute
     * @return execution result
     */
    ExecutionResult execute(Task task);
}
