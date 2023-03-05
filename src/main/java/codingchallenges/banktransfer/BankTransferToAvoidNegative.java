package codingchallenges.banktransfer;

public class BankTransferToAvoidNegative {

    /**
     * String containing "A" or "B" indicating the recipient of a bank transfer.
     * String BA with value [1,2] means
     * -- B receives 1 from A
     * -- A receives 2 from B
     *
     * @param R example BAABA
     * @param V example [2,4,1,1,2]
     * @return Initial balance for each bank account A and B need to be so that they never go into negative balance.
     */
    public static int[] solution(String R, int[] V){
        int initialA = 0;
        int initialB = 0;
        int balA = 0;
        int balB = 0;

        for(int i=0; i < V.length; i++){
            if(R.charAt(i) == 'A'){
                balA = balA + V[i];
                balB = balB - V[i];
                if(balB < 0){
                    initialB += (-balB);
                    balB = 0;
                }
            } else if (R.charAt(i) == 'B') {
                balB = balB + V[i];
                balA = balA - V[i];
                if(balA < 0){
                    initialA += (-balA);
                    balA = 0;
                }
            }
        }

        return new int[]{initialA,initialB};
    }
}
