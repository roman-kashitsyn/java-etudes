package etudes.ptasks;

import java.util.concurrent.Callable;

/**
 * Base class for task executors.
 * @author Roman Kashitsyn
 */
public abstract class AbstractTaskExecutor implements TaskExecutor {

    protected Callable<ExecutionResult> taskAsCallable(final Task task) {
        return new Callable<ExecutionResult>() {
            public ExecutionResult call() throws Exception {
                Exception exception = null;
                long startTime = System.currentTimeMillis();
                try {
                    task.run();
                } catch (Exception e) {
                    exception = e;
                }
                long endTime = System.currentTimeMillis();
                return new ExecutionResult(endTime - startTime, exception);
            }
        };
    }
}
