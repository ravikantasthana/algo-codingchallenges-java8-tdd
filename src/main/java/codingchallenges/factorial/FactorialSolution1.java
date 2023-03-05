package codingchallenges.factorial;

public class FactorialSolution1 {

    public static int factorial(int n){
        return factorial(1, n);
    }

    private static int factorial(int accumulator, int n){
        if(n == 1) return accumulator;
        else return n * factorial(accumulator, n - 1);
    }
}
