package codingchallenges.factorial;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class FactorialSolution1Test {

    @Test
    void ifFactorialOf5Returns120_thenCorrect(){
        Assertions.assertThat(FactorialSolution1.factorial(5)).isEqualTo(120);
    }

    @Test
    void ifFactorialOf4Returns24_thenCorrect(){
        Assertions.assertThat(FactorialSolution1.factorial(4)).isEqualTo(24);
    }

}