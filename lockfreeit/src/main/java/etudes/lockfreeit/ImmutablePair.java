package etudes.lockfreeit;

/**
 * Immutable pair of objects.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class ImmutablePair<F, S> {
    private final F first;
    private final S second;

    private ImmutablePair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F first() {
        return first;
    }
    
    public S second() {
        return second;
    }
    
    public static <F, S> ImmutablePair<F, S> of(F first, S second) {
        return new ImmutablePair<F, S>(first, second);
    }
}
