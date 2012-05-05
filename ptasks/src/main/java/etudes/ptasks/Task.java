package etudes.ptasks;

import java.util.Collection;
import java.util.concurrent.Callable;

/**
 * Represents task that can be executed asynchronously.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public abstract class Task implements Runnable, Callable<ExecutionResult> {

    private final Collection<Task> dependencies;

    protected Task(Collection<Task> dependencies) {
        this.dependencies = dependencies;
    }

    public ExecutionResult call() throws Exception {
        Exception exception = null;
        long startTime = System.currentTimeMillis();
        try {
            run();
        } catch (Exception e) {
            exception = e;
        }
        long endTime = System.currentTimeMillis();
        return new ExecutionResult(endTime - startTime, exception);
    }

    public Collection<Task> getDependencies() {
        return dependencies;
    }
}
