package multithreading.concurrencychallengesandsolutions;

import java.util.Random;

public class DeadlockSolution {

    public static void main(String[] args) {
        Intersection intersection = new Intersection();

        Thread trainAThread = new Thread(new TrainA(intersection));
        Thread trainBThread = new Thread(new TrainB(intersection));

        trainAThread.start();
        trainBThread.start();
    }
    private static class TrainB implements Runnable {
        private final Intersection intersection;
        private Random random = new Random();
        private TrainB(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(random.nextInt(5));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                intersection.takeTrackB();
            }
        }
    }
    private static class TrainA implements Runnable {

        private final Intersection intersection;
        private Random random = new Random();
        private TrainA(Intersection intersection) {
            this.intersection = intersection;
        }

        @Override
        public void run() {

            while (true) {
                try {
                    Thread.sleep(random.nextInt(5));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                intersection.takeTrackA();
            }
        }
    }

    private static class Intersection {

        private Object trackA = new Object();
        private Object trackB = new Object();

        public void takeTrackA(){

            synchronized (trackA) {
                System.out.println("Track A is locked by [" + Thread.currentThread().getName() + "]");

                synchronized (trackB) {
                    System.out.println("Train is passing through Track A");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        public void takeTrackB(){

            synchronized (trackA) { // keep the order of lock the same across the codebase
                System.out.println("Track B is locked by [" + Thread.currentThread().getName() + "]");

                synchronized (trackB) { // keep the order of lock the same across the codebase
                    System.out.println("Train is passing through track B");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

    }
}
