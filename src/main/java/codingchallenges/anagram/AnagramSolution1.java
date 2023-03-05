package codingchallenges.anagram;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnagramSolution1 {

    public static boolean isAnagram(String str1, String str2){
        if(str1 == null || str2 == null || (str1.length() != str2.length())) return false;

        return Stream.of(str1.split("")).sorted().collect(Collectors.joining())
                .equals(Stream.of(str2.split("")).sorted().collect(Collectors.joining()));
    }
}
