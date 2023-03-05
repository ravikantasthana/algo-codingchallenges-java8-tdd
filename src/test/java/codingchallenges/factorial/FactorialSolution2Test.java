package codingchallenges.factorial;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

class FactorialSolution2Test {

    @Test
    void ifFactorialOf5Is120_thenCorrect(){
        Assertions.assertThat(FactorialSolution2.factorial(5)).isEqualTo(BigInteger.valueOf(120));
    }

    @Test
    void ifFactorialOf4Is24_thenCorrect(){
        Assertions.assertThat(FactorialSolution2.factorial(4)).isEqualTo(BigInteger.valueOf(24));
    }

}