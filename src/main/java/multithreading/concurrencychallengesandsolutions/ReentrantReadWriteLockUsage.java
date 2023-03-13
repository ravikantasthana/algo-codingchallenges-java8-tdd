package multithreading.concurrencychallengesandsolutions;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockUsage {

    private static final int MAX_PRICE = 1000;
    public static void main(String[] args) {

        InventoryDatabase inventoryDatabase = new InventoryDatabase();
        Random random = new Random();

        for (int i = 0; i < 100_000; i++) {
            inventoryDatabase.addItem(random.nextInt(MAX_PRICE));
        }

        Thread writer = new Thread(() -> {
           while (true) {
               inventoryDatabase.addItem(random.nextInt(MAX_PRICE));
               inventoryDatabase.removeItem(random.nextInt(MAX_PRICE));
               try {
                   Thread.sleep(10);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
        });

        writer.setDaemon(true);
        writer.start();

        int numberOfReaderThreads = 7;
        List<Thread> readers = new ArrayList<>();

        for (int i = 0; i < numberOfReaderThreads; i++) {
            Thread reader = new Thread(() -> {
                for (int j = 0; j < 100_000; j++) {
                    int upperBoundPrice = random.nextInt(MAX_PRICE);
                    int lowerBoundPrice = upperBoundPrice > 0 ? random.nextInt(upperBoundPrice) : 0;
                    inventoryDatabase.getNumberOfItemsInPriceRance(lowerBoundPrice, upperBoundPrice);
                }
            });

            reader.setDaemon(true);
            readers.add(reader);
        }

        long startingReadingTime = System.currentTimeMillis();

        readers.stream().forEach(Thread::start);
        for (Thread reader : readers) {
            try {
                reader.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long endingReadingTime = System.currentTimeMillis();
        System.out.println(String.format("Reading took %d ms", endingReadingTime - startingReadingTime));
    }

    private static class InventoryDatabase {
        private TreeMap<Integer, Integer> priceToCountMap = new TreeMap<>();

        private ReentrantLock reentrantLock = new ReentrantLock();

        private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        private Lock readLock = readWriteLock.readLock();
        private Lock writeLock = readWriteLock.writeLock();

        private int getNumberOfItemsInPriceRance(int lowerBound, int upperBound){

            //reentrantLock.lock();
            readLock.lock();
            try {
                Integer fromKey = priceToCountMap.ceilingKey(lowerBound);
                Integer toKey = priceToCountMap.floorKey(upperBound);

                if (fromKey == null || toKey == null) {
                    return 0;
                }

                NavigableMap<Integer, Integer> priceRanges = priceToCountMap.subMap(fromKey, true, toKey, true);

                return priceRanges.values().stream().mapToInt(Integer::intValue).sum();
            } finally {
                readLock.unlock();
              //reentrantLock.unlock();
            }
        }

        private void addItem(int price) {
//            reentrantLock.lock();
            writeLock.lock();
            try {
                Integer numberOfItemsForPrice = priceToCountMap.get(price);
                if (numberOfItemsForPrice == null) {
                    priceToCountMap.put(price, 1);
                } else {
                    priceToCountMap.put(price, numberOfItemsForPrice + 1);
                }
            }finally {
//                reentrantLock.unlock();
                writeLock.unlock();
            }
        }
        private void removeItem(int price) {
//            reentrantLock.lock();
            writeLock.lock();
            try {
                Integer numberOfItemsForPrice = priceToCountMap.get(price);
                if (numberOfItemsForPrice != null) {
                    if (numberOfItemsForPrice == 1) {
                        priceToCountMap.remove(price);
                    } else {
                        priceToCountMap.put(price, numberOfItemsForPrice - 1);
                    }
                }
            } finally {
//                reentrantLock.unlock();
                writeLock.unlock();
            }
        }

    }

}
