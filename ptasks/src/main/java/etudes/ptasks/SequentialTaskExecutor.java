package etudes.ptasks;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Executor that executes tasks sequentially in a single thread.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class SequentialTaskExecutor extends AbstractTaskExecutor {

    public ExecutionResult execute(Task task) {
        new GraphInspector().checkForCycles(task);
        List<Task> tasks = flatten(task);
        long totalExecutionTime = 0;
        ExecutionResult lastResult = null;
        try {
            for (Task t : tasks) {
                lastResult = taskAsCallable(t).call();
                totalExecutionTime += lastResult.getTimeSpent();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ExecutionResult(totalExecutionTime, lastResult.getException());
    }

    private List<Task> flatten(Task root) {
        List<Task> tasks = Lists.newArrayList();
        Set<Task> processed = Sets.newHashSet();
        flattenRecursive(root, processed, tasks);
        return tasks;
    }

    private void flattenRecursive(Task root, Set<Task> processedTasks, List<Task> result) {
        for (Task dep : root.getDependencies()) {
            if (!processedTasks.contains(dep)) {
                flattenRecursive(dep, processedTasks, result);
            }
        }
        if (!processedTasks.contains(root)) {
            result.add(root);
            processedTasks.add(root);
        }
    }
}
