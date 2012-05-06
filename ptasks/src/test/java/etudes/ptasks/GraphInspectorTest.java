package etudes.ptasks;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static etudes.ptasks.Runnables.noOp;

/**
 * Tests for {@link GraphInspector}.
 */
public class GraphInspectorTest {

    @Test(expected = CyclicDependenciesException.class)
    public void inspectorShouldFindSelfReference() {
        new GraphInspector().checkForCycles(new Task() {
            public Collection<? extends Task> getDependencies() {
                return Collections.singleton(this);
            }
            public void run() {}
        });
    }

    @Test(expected = CyclicDependenciesException.class)
    public void inspectorShouldFindCircularDependencyWithTwoNodes() {
        TaskBuilder builder = new TaskBuilder();
        Task[] aDeps = new Task[1];
        Task[] bDeps = new Task[1];
        Task a = builder.run(noOp()).after(aDeps);
        Task b = builder.run(noOp()).after(bDeps);
        aDeps[0] = b; bDeps[0] = a;
        new GraphInspector().checkForCycles(a);
    }

    @Test(expected = CyclicDependenciesException.class)
    public void inspectorShouldFindCircularDependenciesWithThreeNodes() {
        TaskBuilder builder = new TaskBuilder();
        Task[] aDeps = new Task[1];
        Task[] bDeps = new Task[1];
        Task[] cDeps = new Task[1];
        Task a = builder.run(noOp()).after(aDeps);
        Task b = builder.run(noOp()).after(bDeps);
        Task c = builder.run(noOp()).after(cDeps);
        aDeps[0] = b; bDeps[0] = c; cDeps[0] = a;
        new GraphInspector().checkForCycles(a);
    }

    @Test
    public void inspectorShouldPassValidConfiguration() {
        TaskBuilder taskBuilder = new TaskBuilder();
        Task a = taskBuilder.justRun(noOp());
        Task b = taskBuilder.run(noOp()).after(a);
        new GraphInspector().checkForCycles(b);
    }

    @Test
    public void testMultipleTasksDependsOnASingleTask() {
        TaskBuilder builder = new TaskBuilder();
        Task a = builder.justRun(noOp());
        Task b = builder.run(noOp()).after(a);
        Task c = builder.run(noOp()).after(b);
        Task seq = builder.sequence(a, b, c);
        new GraphInspector().checkForCycles(seq);
    }
}
