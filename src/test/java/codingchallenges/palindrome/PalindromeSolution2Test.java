package codingchallenges.palindrome;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PalindromeSolution2Test {

    @Test
    void ifStringIsPalindrome_thenTrue(){
        Assertions.assertThat(PalindromeSolution2.isPalindrome("abcba")).isTrue();
    }

    @Test
    void ifStringIsNotPalindrome_thenFalse(){
        Assertions.assertThat(PalindromeSolution2.isPalindrome("abcde")).isFalse();
    }
}