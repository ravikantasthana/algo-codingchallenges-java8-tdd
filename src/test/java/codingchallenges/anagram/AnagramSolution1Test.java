package codingchallenges.anagram;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
class AnagramSolution1Test {

    @Test
    void whenStringsAreAnagram_thenTrue(){
        Assertions.assertThat(AnagramSolution1.isAnagram("stree", "trees")).isTrue();
    }

}