package etudes.ptasks;

import java.util.Collection;

/**
 * Task abstraction. Each task can have dependent tasks.
 * @author Roman Kashitsyn
 */
public interface Task extends Runnable {

    /**
     * Returns (almost certainly immutable) collection of dependent tasks.
     * @return dependent tasks
     */
    public Collection<? extends Task> getDependencies();
}
