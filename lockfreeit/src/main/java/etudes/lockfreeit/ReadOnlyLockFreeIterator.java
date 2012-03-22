package etudes.lockfreeit;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Read only lock-free iterator template.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
abstract class ReadOnlyLockFreeIterator<T> implements Iterator<T> {
    
    private static final int MAX_TRIES = 10000;
    
    private final AtomicReference<T> atomicRef;
    
    protected ReadOnlyLockFreeIterator(T initialValue) {
        atomicRef = new AtomicReference<T>(initialValue);
    }
    
    public T next() {
        checkHasNext();
        int tries = 0;
        T current, next;
        do {
            checkTries(++tries);
            current = atomicRef.get();
            next = advance(current);
        } while (!atomicRef.compareAndSet(current, next));
        return current;
    }

    protected abstract T advance(T currentValue);

    public void remove() {
        throw new UnsupportedOperationException("Can not modify unmodifiable iterator");
    }

    private static void checkTries(int tries) {
        if (tries > MAX_TRIES) {
            throw new IllegalStateException("Too many tries: " + tries);
        }
    }

    private void checkHasNext() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
    }
}
