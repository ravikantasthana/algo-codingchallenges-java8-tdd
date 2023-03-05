package codingchallenges.palindrome;

public class PalindromeSolution2 {

    public static boolean isPalindrome(String str){
        if(str == null || str.length() == 0) return false;
        boolean isPalindrome = true;

        for(int i = 0; i < str.length()/2; i ++){
            if(str.charAt(i) != str.charAt(str.length() - i -1)) isPalindrome = false;
        }

        return  isPalindrome;
    }
}
