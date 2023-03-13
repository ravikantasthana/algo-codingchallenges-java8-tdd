package multithreading.concurrencychallengesandsolutions;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class SemaphoreBarrier {

    public static void main(String[] args) {
        int numberOfWorkers = 200;
        Barrier barrier = new Barrier(numberOfWorkers);
        IntStream.range(0,200).mapToObj(value -> new Thread(new CoordinatedWorkRunner(barrier)))
                .forEach(Thread::start);
    }

    private static class Barrier {
        private final int numberOfWorkers;
        private Semaphore semaphore = new Semaphore(0);
        private int counter = 0;
        private Lock lock = new ReentrantLock();

        public Barrier(int numberOfWorkers) {
            this.numberOfWorkers = numberOfWorkers;
        }

        public void barrier() {
            lock.lock();
            boolean isLastWorker = false;
            try {
                counter++;
                if(counter == numberOfWorkers){
                    isLastWorker = true;
                }
            } finally {
                lock.unlock();
            }

            if (isLastWorker) {
                semaphore.release(numberOfWorkers - 1);
            }else {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static class CoordinatedWorkRunner implements Runnable {

        private final Barrier barrier;

        private CoordinatedWorkRunner(Barrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
                task();
        }

        private void task() {
            //performing part1
            System.out.println(Thread.currentThread().getName() + " - part 1 of the work is finished");

            barrier.barrier();

            //performing part2
            System.out.println(Thread.currentThread().getName() + " - part 2 of the work is finished");
        }

    }
}
