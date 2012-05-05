package etudes.ptasks;

/**
 * Exception that signaling about cycle in the dependency graph.
 *
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class CyclicDependenciesException extends RuntimeException {

    public CyclicDependenciesException() {
        super();
    }

    public CyclicDependenciesException(String message) {
        super(message);
    }

    public CyclicDependenciesException(String message, Throwable cause) {
        super(message, cause);
    }
}
