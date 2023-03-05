package codingchallenges.palindrome;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PalindromeSolution1Test {

    @Test
    void whenStringIsPalindrome_thenTrue(){
        Assertions.assertThat(PalindromeSolution1.isPalindrome("ab ba")).isTrue();
    }

    @Test
    void whenStringIsNotPalindrome_thenFalse(){
        Assertions.assertThat(PalindromeSolution1.isPalindrome("abcd")).isFalse();
    }

}