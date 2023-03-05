package codingchallenges.anagram;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AnagramSolution2Test {

    @Test
    void ifStringsAreAnagrams_thenTrue(){
        Assertions.assertThat(AnagramSolution2.isAnagram("stree", "trees")).isTrue();
    }

    @Test
    void isStringAreNotAnagrams_thenFalse(){
        Assertions.assertThat(AnagramSolution2.isAnagram("astring","trees")).isFalse();
    }

}