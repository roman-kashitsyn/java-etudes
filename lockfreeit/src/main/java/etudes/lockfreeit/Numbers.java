package etudes.lockfreeit;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Natural numbers.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class Numbers {

    private Numbers() {}

    private static class EndlessIterator implements Iterator<BigInteger> {
        
        private static int MAX_TRIES = 1000; 
        private final AtomicReference<BigInteger> atomicRef = new AtomicReference<BigInteger>(BigInteger.ONE);

        public BigInteger next() {
            BigInteger currentValue;
            BigInteger newValue;
            int tries = 0;
            do {
                currentValue = atomicRef.get();
                newValue = currentValue.add(BigInteger.ONE);
                if (++tries > MAX_TRIES) {
                    throw new IllegalStateException("To many tries to update atomic reference: " + tries);
                }
            } while (!atomicRef.compareAndSet(currentValue, newValue));
            return currentValue;
        }

        public void remove() {
            throw new UnsupportedOperationException("I can't delete natural number. Think about the Universe!");
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
        }
    }

    public static Iterable<BigInteger> naturalNumbers() {
        return StatelessIterables.NaturalNumbers;
    }

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
}
