package codingchallenges.anagram;

import java.util.Arrays;

public class AnagramSolution2 {

    public static boolean isAnagram(String str1, String str2){

        if(str1 == null || str2 == null || str1.length() != str2.length()) return false;

        char[] c1 = str1.toCharArray();
        char[] c2 = str2.toCharArray();

        Arrays.sort(c1);
        Arrays.sort(c2);

        return Arrays.equals(c1,c2);
    }
}
