package multithreading.concurrencychallengesandsolutions;

public class SynchronizationWithLockObject {

    public static void main(String[] args) throws InterruptedException {

        InventoryCounter inventoryCounter = new InventoryCounter();
        Thread incrementingThread = new IncrementingThread(inventoryCounter);
        Thread decrementingThread = new DecrementingThread(inventoryCounter);

        incrementingThread.start();
        decrementingThread.start();

        incrementingThread.join();
        decrementingThread.join();

        System.out.println("Number of Inventory Items = " + inventoryCounter.getItems());
    }

    private static class IncrementingThread extends Thread {

        private final InventoryCounter inventoryCounter;

        public IncrementingThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0 ; i < 10_000; i++){
                inventoryCounter.increment();
            }
        }
    }

    private static class DecrementingThread extends Thread {

        private final InventoryCounter inventoryCounter;

        public DecrementingThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10_000; i++) {
                inventoryCounter.decrement();
            }
        }
    }

    private static class InventoryCounter {

        private int items;
        Object lock = new Object();
        public void increment(){
            synchronized (this.lock) {
                items++;
            }
        }

        public void decrement(){
            synchronized (this.lock) {
                items--;
            }
        }

        public int getItems() {
            synchronized (this.lock) {
                return items;
            }
        }
    }

}
