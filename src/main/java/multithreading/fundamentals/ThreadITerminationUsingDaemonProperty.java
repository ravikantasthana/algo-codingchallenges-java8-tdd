package multithreading.fundamentals;

import java.math.BigInteger;

public class ThreadITerminationUsingDaemonProperty {

    public static void main(String[] args) throws InterruptedException {
        LongRunningComputation longRunningComputation = new LongRunningComputation(BigInteger.valueOf(20000), BigInteger.valueOf(10000000));
        Thread thread = new Thread(longRunningComputation);
        thread.setDaemon(true); // set daemon property to TRUE
        thread.start();
        Thread.sleep(100);
        // even though thread is not handling the interrupt signal, but the main thread will stop
        // because it will not wait for daemon thread to finish.
        //thread.interrupt();
    }

    private record LongRunningComputation(BigInteger base, BigInteger power) implements Runnable {

            @Override
            public void run() {
                System.out.println(base + "^" + power + " = " + power(base, power));
            }

            private BigInteger power(BigInteger base, BigInteger power) {
                BigInteger result = BigInteger.ONE;

                for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
                    result = result.multiply(base);
                }

                return result;
            }
    }
}
