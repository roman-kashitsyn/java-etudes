package etudes.lockfreeit;

import com.google.common.base.Function;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Natural numbers.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class Sequences {

    private Sequences() {}

    private static class EndlessIterator extends ReadOnlyLockFreeIterator<BigInteger> {

        EndlessIterator() {
            super(BigInteger.ONE);
        }

        protected BigInteger advance(BigInteger current) {
            return current.add(BigInteger.ONE);
        }

        public boolean hasNext() {
            return true;
        }
    }

    private enum StatelessIterables implements Iterable<BigInteger> {
        NaturalNumbers {
            public Iterator<BigInteger> iterator() {
                return new EndlessIterator();
            }
        },
        
        FibonacciNumbers {
            public Iterator<BigInteger> iterator() {
                return new Iterator<BigInteger>() {
                    private final Iterator<ImmutablePair<BigInteger, BigInteger>> pairIterator =
                            Sequences.iterate(
                                    Functions.fibonacciTransform(),
                                    ImmutablePair.of(BigInteger.ONE, BigInteger.ONE)
                            ).iterator();

                    public BigInteger next() {
                        return pairIterator.next().first();
                    }

                    public void remove() {
                        pairIterator.remove();
                    }

                    public boolean hasNext() {
                        return pairIterator.hasNext();
                    }
                };
            }
        }
    }

    /**
     * Returns lazy infinite sequence of natural numbers.
     * @return lazy sequence of natural numbers
     */
    public static Iterable<BigInteger> naturalNumbers() {
        return StatelessIterables.NaturalNumbers;
    }

    /**
     * Returns lazy range of integers.
     * @param fromInclusive number to start from, inclusive
     * @param toExclusive number iterate to, exclusive
     * @return lazy iterable range of integers
     */
    public static Iterable<Integer> range(final int fromInclusive, final int toExclusive) {
        return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return new Iterator<Integer>() {
                    AtomicInteger currentValue = new AtomicInteger(fromInclusive);

                    public boolean hasNext() {
                        return currentValue.get() < toExclusive;
                    }

                    public Integer next() {
                        return currentValue.getAndIncrement();
                    }

                    public void remove() {
                        throw new UnsupportedOperationException("Can't remove number from range.");
                    }
                };
            }
        };
    }

    /**
     * Returns lazy infinite sequence of x, f(x), f(f(x)), ...
     * @param f function free of side effects
     * @param initValue initial value
     * @param <T> sequence element type
     * @return lazy sequence
     */
    public static <T> Iterable<T> iterate(final Function<T,T> f, final T initValue) {
        return new Iterable<T>() {
            public Iterator<T> iterator() {
                return new ReadOnlyLockFreeIterator<T>(initValue) {
                    public boolean hasNext() {
                        return true;
                    }

                    protected T advance(T current) {
                        return f.apply(current);
                    }
                };
            }
        };
    }

    /**
     * Returns lazy infinite fibonacci sequence.
     * @return lazy fibonacci sequence
     */
    public static Iterable<BigInteger> fibonacci() {
        return StatelessIterables.FibonacciNumbers;
    }
}
