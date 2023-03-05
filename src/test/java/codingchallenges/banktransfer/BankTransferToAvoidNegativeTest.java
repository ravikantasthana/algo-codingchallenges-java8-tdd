package codingchallenges.banktransfer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class BankTransferToAvoidNegativeTest {

    @Test
    void bankTransferToAvoidNegative_success(){
        Assertions.assertThat(BankTransferToAvoidNegative.solution("BA", new int[]{1,2})).isEqualTo(new int[]{1,1});
    }

    @Test
    void bankTransferToAvoidNegative_success2(){
        Assertions.assertThat(BankTransferToAvoidNegative.solution("BAABA", new int[]{2,4,1,1,2})).isEqualTo(new int[]{2,4});
    }

    @Test
    void bankTransferToAvoidNegative_failure(){
        Assertions.assertThat(BankTransferToAvoidNegative.solution("BAABA", new int[]{2,4,1,1,2})).isNotEqualTo(new int[]{1,1});
    }

}