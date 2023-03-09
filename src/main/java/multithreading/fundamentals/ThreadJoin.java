package multithreading.fundamentals;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class ThreadJoin {

    public static void main(String[] args) throws InterruptedException {

        List<Long> inputNumbers = List.of(100000000L,3435L, 35435L, 2324L, 4656L, 23L, 5556L);
        List<FactorialThread> threads = inputNumbers.stream().map(i -> new FactorialThread(i)).collect(Collectors.toList());

        // start each thread
        threads.stream().forEach(t -> {
                //t.setDaemon(true); //set daemon true so that threads run in the background and do not stop for the application from finishing.
                t.start();
        });

        // join the thread so that main thread waits for all other threads to finish
        for (FactorialThread thread : threads) {
            thread.join(2000); // method will only return when this thread has terminated or the wait time has elapsed.
        }

        for(int i = 0; i < inputNumbers.size(); i++) {
            FactorialThread factorialThread = threads.get(i);
            if(factorialThread.isFinished){
                System.out.println("Factorial of " + inputNumbers.get(i) + " is: " + factorialThread.getFactorial());
            }else {
                System.out.println("Factorial of " + inputNumbers.get(i) + " is still being performed.");
            }
        }

        // at this point interrupt any thread that is still alive because they have timed out
        threads.stream().forEach(t -> {
            if (t.isAlive()) t.interrupt();
        });
    }

    private static class FactorialThread extends Thread {

        private final long factorialOf;
        private boolean isFinished = false;
        private BigInteger factorial = BigInteger.ZERO;

        public FactorialThread(long factorialOf) {
            this.factorialOf = factorialOf;
        }

        @Override
        public void run() {
            factorial = factorial(factorialOf);
            isFinished = true;
        }

        public boolean isFinished(){
            return isFinished;
        }

        public BigInteger getFactorial() {
            return factorial;
        }

        public BigInteger factorial(long n){
            BigInteger acc = BigInteger.ONE;
            for (long l = 1; l <= n; l++) {
                if(this.isInterrupted()) {
                    System.out.println("Performing factorial of " + n + " is INTERRUPTED while still at " + l);
                    System.exit(0);
                }
                acc = acc.multiply(BigInteger.valueOf(l));
            }
            return acc;

            /*return LongStream.rangeClosed(1,n)
                    .mapToObj(BigInteger::valueOf)
                    .reduce(BigInteger.ONE,BigInteger::multiply);*/
        }

    }
}
