package multithreading.concurrencychallengesandsolutions;

import java.util.Random;

public class AtomicOperationsAndVolatile {

    public static void main(String[] args) {

        Metrics metrics = new Metrics();
        BusinessLogic businessLogic1 = new BusinessLogic(metrics);
        BusinessLogic businessLogic2 = new BusinessLogic(metrics);
        MetricsPrinter metricsPrinter = new MetricsPrinter(metrics);

        businessLogic1.start();
        businessLogic2.start();
        metricsPrinter.start();
    }

    private static class MetricsPrinter extends Thread {

        private final Metrics metrics;

        private MetricsPrinter(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Metrics Average is: " + metrics.getAverages());
            }
        }
    }
    private static class BusinessLogic extends Thread {
        private final Metrics metrics;
        private final Random random = new Random();
        public BusinessLogic(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while (true) {
                long start = System.currentTimeMillis();
                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                long end = System.currentTimeMillis();
                metrics.addSample(end - start);
            }
        }

    }
    private static class Metrics {
        private long count = 0; // how long some operation took
        private volatile double averages = 0.0; // volatile insures that read and write to averages will be atomic

        public synchronized void addSample(long sample) {
            double currentSum = averages * count;
            count++;
            averages = (currentSum + sample)/count;
        }
        public double getAverages() {
            return averages;
        }
    }
}
