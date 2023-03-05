package codingchallenges.palindrome;

import java.util.stream.IntStream;

public class PalindromeSolution1 {

    public static boolean isPalindrome(String str){
        if(str == null || str.length() == 0) return false;

        String newStr = str.replaceAll("\\s+", "");

        return IntStream.range(0, newStr.length())
                .noneMatch(i -> newStr.charAt(i) != newStr.charAt(newStr.length() - i - 1));
    }
}
