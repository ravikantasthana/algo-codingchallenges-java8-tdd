package multithreading.fundamentals;

import java.math.BigInteger;

public class ThreadInterruptionExplicit {

    public static void main(String[] args) {
        LongRunningComputation longRunningComputation = new LongRunningComputation(BigInteger.valueOf(20000), BigInteger.valueOf(10000000));
        Thread thread = new Thread(longRunningComputation);
        thread.start();

        thread.interrupt(); // will not have any effect unless interrupt is handled explicitly inside the hotspot code
    }

    private record LongRunningComputation(BigInteger base, BigInteger power) implements Runnable {

            @Override
            public void run() {
                System.out.println(base + "^" + power + " = " + power(base, power));
            }

            private BigInteger power(BigInteger base, BigInteger power) {
                BigInteger result = BigInteger.ONE;

                for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
                    //this is the hotspot code
                    if(Thread.currentThread().isInterrupted()){
                        System.out.println("Computation Interrupted");
                        return BigInteger.ZERO;
                    }
                    result = result.multiply(base);
                }

                return result;
            }
    }
}
