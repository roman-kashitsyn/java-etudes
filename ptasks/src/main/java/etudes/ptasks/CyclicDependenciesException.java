package etudes.ptasks;

import com.google.common.base.Joiner;

import java.util.Collection;
import java.util.Collections;

/**
 * Exception that signaling about cycle in the dependency graph.
 *
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class CyclicDependenciesException extends RuntimeException {

    private Collection<Task> cycledTasks = Collections.emptyList();

    public CyclicDependenciesException() {
        super();
    }

    public CyclicDependenciesException(Collection<Task> cycledTasks) {
        super(makeMessage(cycledTasks));
        this.cycledTasks = cycledTasks;
    }

    public CyclicDependenciesException(String message) {
        super(message);
    }

    public CyclicDependenciesException(String message, Throwable cause) {
        super(message, cause);
    }

    public Collection<Task> getCycledTasks() {
        return cycledTasks;
    }

    private static String makeMessage(Collection<Task> cycledTasks) {
        StringBuilder builder = new StringBuilder("Circular dependencies have been found between tasks: ");
        Joiner.on(", ").appendTo(builder, cycledTasks);
        return builder.toString();
    }
}
