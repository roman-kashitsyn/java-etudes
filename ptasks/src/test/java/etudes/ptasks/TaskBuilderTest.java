package etudes.ptasks;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link TaskBuilder}.
 * @author Roman Kashitsyn
 */
public class TaskBuilderTest {

    @Test
    public void builderShouldComposeValidConfigurationForTaskWithoutDependencies() {
        Task task = new TaskBuilder().justRun(Runnables.noOp());
        assertTrue(task.getDependencies().isEmpty());
    }

    @Test
    public void builderShouldComposeValidConfigurationForTaskWithDependencies() {
        TaskBuilder builder = new TaskBuilder();
        Task a = builder.justRun(Runnables.noOp());
        Task b = builder.run(Runnables.noOp()).after(a);

        assertTrue(a.getDependencies().isEmpty());
        assertTrue(b.getDependencies().contains(a));
    }
}
