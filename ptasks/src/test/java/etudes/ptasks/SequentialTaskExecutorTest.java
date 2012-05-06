package etudes.ptasks;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link SequentialTaskExecutor}.
 * @author Roman Kashitsyn
 */
public class SequentialTaskExecutorTest {

    @Test
    public void sequentialExecutorShouldExecuteTasksAccordingToDependencies() {
        TaskBuilder builder = new TaskBuilder();
        Runnables.TimedRunnable t1 = Runnables.timedRunnable(10);
        Runnables.TimedRunnable t2 = Runnables.timedRunnable(10);
        Runnables.TimedRunnable t3 = Runnables.timedRunnable(10);

        Task a = builder.justRun(t1);
        Task b = builder.run(t2).after(a);
        Task c = builder.run(t3).after(a);
        Task seq = builder.sequence(a, b, c);

        new SequentialTaskExecutor().execute(seq);
        assertTrue(t1.getTime() < t2.getTime());
        assertTrue(t2.getTime() < t3.getTime());
        for (Runnables.TimedRunnable tr : Arrays.asList(t1, t2, t3)) {
            assertEquals(1, tr.callCount());
        }
    }
}
