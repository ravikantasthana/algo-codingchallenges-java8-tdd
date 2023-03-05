package codingchallenges.factorial;

import java.math.BigInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FactorialSolution3 {

    public static BigInteger factorial(int n){

        return IntStream.rangeClosed(1,n).mapToObj(BigInteger::valueOf).reduce(BigInteger.ONE, BigInteger::multiply);

    }
}
