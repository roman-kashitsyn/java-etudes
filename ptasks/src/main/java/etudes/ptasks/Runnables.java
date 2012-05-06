package etudes.ptasks;

import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Set of runnable implementations.
 * @see java.lang.Runnable
 * @author Roman Kashitsyn
 */
public class Runnables {

    private Runnables() {}

    public static class TimedRunnable implements Runnable {

        private long time;
        private long delay;
        private AtomicInteger callCount = new AtomicInteger(0);

        protected TimedRunnable(long delay) {
            this.delay = delay;
        }

        public void run() {
            if (delay > 0) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ie) {
                    throw new RuntimeException(ie);
                }
            }
            time = System.currentTimeMillis();
            callCount.incrementAndGet();
        }

        public long getTime() {
            return time;
        }

        public int callCount() {
            return callCount.get();
        }
    }

    private enum StatelessRunnables implements Runnable {
        NoOp {
            public void run() {}
        }
    }

    public static Runnable noOp() {
        return StatelessRunnables.NoOp;
    }

    public static Runnable print(final PrintWriter writer, final String message) {
        return new Runnable() {
            public void run() {
                writer.println(message);
            }
        };
    }

    public static TimedRunnable timedRunnable(long delay) {
        return new TimedRunnable(delay);
    }

}
