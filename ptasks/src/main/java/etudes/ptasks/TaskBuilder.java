package etudes.ptasks;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Builder for tasks.
 * @author Roman Kashitsyn
 */
public class TaskBuilder {

    /**
     * Simple task implementation.
     */
    private static class SimpleTask implements Task {

        private final Runnable work;
        private final Collection<Task> dependencies;

        public SimpleTask(Runnable work, Collection<Task> dependencies) {
            this.work = work;
            this.dependencies = dependencies;
        }

        public void run() {
            work.run();
        }

        public Collection<? extends Task> getDependencies() {
            return dependencies;
        }
    }

    public static final class OngoingDependencyConstruction {
        private final Runnable work;

        private OngoingDependencyConstruction(Runnable work) {
            this.work = work;
        }

        public Task after(Task... tasks) {
            return new SimpleTask(work, Arrays.asList(tasks));
        }
    }

    /**
     * Constructs task from runnable with empty dependency list.
     * @param work work to execute within task
     * @return task that executes given runnable
     */
    public Task justRun(Runnable work) {
        return new SimpleTask(work, Collections.<Task>emptyList());
    }

    /**
     * Starts construction of task with dependencies.
     * @param work work to execute within task
     * @return builder to allow dependency specification
     */
    public OngoingDependencyConstruction run(Runnable work) {
        return new OngoingDependencyConstruction(work);
    }

    /**
     * Constructs virtual task as container for several other tasks.
     * @param dependencies dependent tasks
     * @return task that contains given sequence of tasks as dependencies
     */
    public Task sequence(Task... dependencies) {
        return new SimpleTask(Runnables.noOp(), Arrays.asList(dependencies));
    }
}
