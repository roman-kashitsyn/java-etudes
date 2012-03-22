package etudes.lockfreeit;

import com.google.common.collect.Iterables;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link Sequences}.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class SequencesTest {

    private static final BigInteger FIB_151 = new BigInteger("16130531424904581415797907386349");
    private ExecutorService executorService = Executors.newFixedThreadPool(3);
    
    private class Skipper implements Runnable {
        private final Iterator<?> iterator;
        private final int elementsToSkip;
        
        public Skipper(Iterator<?> iterator, int elementsToSkip) {
            this.iterator = iterator;
            this.elementsToSkip = elementsToSkip;
        }
        
        public void run() {
            for (int i = 0; i < elementsToSkip; ++i) {
                iterator.next();
            }
        }
    }
    
    @Test
    public void testNaturalNumbersReadFrom3Threads() throws Exception {
        Iterator<BigInteger> naturals = Sequences.naturalNumbers().iterator();
        runInSeparateThreads(new Skipper(naturals, 500), 3);
        executorService.awaitTermination(1, TimeUnit.SECONDS);
        assertTrue(naturals.hasNext());
        assertEquals(BigInteger.valueOf(1501), naturals.next());
    }
    
    @Test
    public void testSimpleRangeIterator() throws Exception {
        Iterator<Integer> rangeIterator = Sequences.range(1, 1000).iterator();
        runInSeparateThreads(new Skipper(rangeIterator, 300), 3);
        executorService.awaitTermination(1, TimeUnit.SECONDS);
        assertTrue(rangeIterator.hasNext());
        assertEquals(Integer.valueOf(901), rangeIterator.next());
    }
    
    @Test
    public void testFibonacciSequence() {
        Iterable<BigInteger> fibs = Sequences.fibonacci();
        
        assertTrue(Iterables.elementsEqual(
                Iterables.limit(fibs, 5),
                Arrays.asList(
                        BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(2),
                        BigInteger.valueOf(3), BigInteger.valueOf(5)
                )
        ));
    }
    
    @Test
    public void testFibParallel() throws Exception {
        Iterator<BigInteger> fibs = Sequences.fibonacci().iterator();
        runInSeparateThreads(new Skipper(fibs, 50), 3);
        executorService.awaitTermination(2, TimeUnit.SECONDS);
        assertEquals(FIB_151, fibs.next());
    }
    
    private void runInSeparateThreads(Runnable task, int submitTimes) {
        for (int i = 0; i < submitTimes; ++i) {
            executorService.submit(task);
        }
    }
}
