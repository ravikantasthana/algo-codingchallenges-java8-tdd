package codingchallenges.factorial;

import java.math.BigInteger;

public class FactorialSolution2 {

    public static BigInteger factorial(int n){
        BigInteger fact = BigInteger.ONE;

        for(int i = 1; i <= n; i++){
            fact = fact.multiply(BigInteger.valueOf(i));
        }

        return fact;
    }
}
