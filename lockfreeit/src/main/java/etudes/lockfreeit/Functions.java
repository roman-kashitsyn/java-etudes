package etudes.lockfreeit;

import com.google.common.base.Function;

import java.math.BigInteger;

/**
 * Some functions.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class Functions {

    private Functions() {}

    private enum PairFunctions implements
            Function<ImmutablePair<BigInteger, BigInteger>, ImmutablePair<BigInteger, BigInteger>> {
        Fibonacci {
            public ImmutablePair<BigInteger, BigInteger> apply(ImmutablePair<BigInteger, BigInteger> x) {
                return ImmutablePair.of(x.second(), x.first().add(x.second()));
            }
        }
    }

    public static Function<ImmutablePair<BigInteger, BigInteger>,
            ImmutablePair<BigInteger, BigInteger>> fibonacciTransform() {
        return PairFunctions.Fibonacci;
    }
}
